import { Component, Input, Output, EventEmitter, TemplateRef } from '@angular/core';
import { CommonModule } from '@angular/common';

export interface TableColumn {
  key: string;
  label: string;
  sortable?: boolean;
  template?: TemplateRef<any>;
}

export interface PageInfo {
  page: number;
  size: number;
  totalElements: number;
  totalPages: number;
}

@Component({
  selector: 'app-data-table',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './data-table.component.html',
  styleUrls: ['./data-table.component.scss']
})
export class DataTableComponent {
  @Input() title: string = '';
  @Input() data: any[] = [];
  @Input() columns: TableColumn[] = [];
  @Input() pageInfo?: PageInfo;
  @Input() showActions: boolean = true;
  @Input() showCreateButton: boolean = true;
  @Input() emptyMessage?: string;
  
  @Output() onPageChange = new EventEmitter<number>();
  @Output() onSort = new EventEmitter<TableColumn>();
  @Output() onCreate = new EventEmitter<void>();
  @Output() onEdit = new EventEmitter<any>();
  @Output() onDelete = new EventEmitter<any>();

  trackByFn(index: number, item: any): any {
    return item.id || index;
  }

  getColumnValue(item: any, key: string): any {
    return key.split('.').reduce((obj, prop) => obj?.[prop], item);
  }

  getStartRecord(): number {
    if (!this.pageInfo || !this.data || this.data.length === 0) return 0;
    return this.pageInfo.page * this.pageInfo.size + 1;
  }

  getEndRecord(): number {
    if (!this.pageInfo || !this.data || this.data.length === 0) return 0;
    return Math.min((this.pageInfo.page + 1) * this.pageInfo.size, this.pageInfo.totalElements);
  }

  getVisiblePages(): number[] {
    if (!this.pageInfo) return [];
    
    const current = this.pageInfo.page;
    const total = this.pageInfo.totalPages;
    const delta = 2;
    
    let start = Math.max(0, current - delta);
    let end = Math.min(total - 1, current + delta);
    
    if (end - start < 4) {
      if (start === 0) {
        end = Math.min(total - 1, start + 4);
      } else if (end === total - 1) {
        start = Math.max(0, end - 4);
      }
    }
    
    const pages = [];
    for (let i = start; i <= end; i++) {
      pages.push(i);
    }
    return pages;
  }
}
