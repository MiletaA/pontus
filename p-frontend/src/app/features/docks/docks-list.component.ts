import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterModule, NavigationEnd } from '@angular/router';
import { FormBuilder, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { Subscription } from 'rxjs';
import { filter } from 'rxjs/operators';
import { DockService, Dock, PageResponse } from '../../core/services/dock.service';
import { DataTableComponent, TableColumn, PageInfo } from '../../shared/components/data-table/data-table.component';

@Component({
  selector: 'app-docks-list',
  standalone: true,
  imports: [CommonModule, RouterModule, ReactiveFormsModule, DataTableComponent],
  templateUrl: './docks-list.component.html',
  styleUrls: ['./docks-list.component.scss']
})
export class DocksListComponent implements OnInit, OnDestroy {
  docks: Dock[] = [];
  loading = false;
  error = '';
  private routerSubscription: Subscription = new Subscription();
  
  // Filter form
  filterForm: FormGroup;
  
  // Table configuration
  columns: TableColumn[] = [
    { key: 'name', label: 'Dock Name', sortable: true },
    { key: 'maxLength', label: 'Max Length (m)', sortable: true },
    { key: 'isOccupied', label: 'Status', sortable: true },
    { key: 'assignedVesselId', label: 'Assigned Vessel', sortable: false },
    { key: 'handlesDangerous', label: 'Dangerous Cargo', sortable: true },
    { key: 'description', label: 'Description', sortable: false }
  ];
  
  pageInfo: PageInfo = {
    page: 0,
    size: 20,
    totalElements: 0,
    totalPages: 0
  };

  constructor(
    private dockService: DockService,
    private router: Router,
    private fb: FormBuilder
  ) {
    this.filterForm = this.fb.group({
      search: [''],
      status: [''],
      handlesDangerous: ['']
    });
  }

  ngOnInit(): void {
    this.loadDocks();
    
    // Setup filter listeners
    this.filterForm.valueChanges.subscribe(() => {
      this.pageInfo.page = 0;
      this.loadDocks();
    });
    
    // Listen for navigation events to refresh data when returning from create/edit pages
    this.routerSubscription = this.router.events
      .pipe(filter(event => event instanceof NavigationEnd))
      .subscribe((event: NavigationEnd) => {
        if (event.url === '/docks') {
          this.loadDocks();
        }
      });
  }

  ngOnDestroy(): void {
    this.routerSubscription.unsubscribe();
  }

  loadDocks(): void {
    this.loading = true;
    this.error = '';
    
    this.dockService.getAllDocks(this.pageInfo.page, this.pageInfo.size).subscribe({
      next: (response: PageResponse<Dock>) => {
        let docks = response.content;
        
        // Apply client-side filtering
        const filters = this.filterForm.value;
        
        if (filters.search) {
          const searchTerm = filters.search.toLowerCase();
          docks = docks.filter(dock => 
            dock.name.toLowerCase().includes(searchTerm) ||
            (dock.description && dock.description.toLowerCase().includes(searchTerm))
          );
        }
        
        if (filters.status !== '') {
          const isOccupied = filters.status === 'true';
          docks = docks.filter(dock => dock.isOccupied === isOccupied);
        }
        
        if (filters.handlesDangerous !== '') {
          const handlesDangerous = filters.handlesDangerous === 'true';
          docks = docks.filter(dock => dock.handlesDangerous === handlesDangerous);
        }
        
        this.docks = docks;
        this.pageInfo.totalElements = response.totalElements;
        this.pageInfo.totalPages = response.totalPages;
        this.loading = false;
      },
      error: (error) => {
        console.error('Error loading docks:', error);
        this.error = `Failed to load docks: ${error.error?.message || error.message || 'Unknown error'}`;
        this.docks = []; // Ensure docks is always an array
        this.loading = false;
      }
    });
  }

  onPageChange(page: number): void {
    this.pageInfo.page = page;
    this.loadDocks();
  }

  onCreateDock(): void {
    this.router.navigate(['/docks/create']);
  }

  onEditDock(dock: Dock): void {
    this.router.navigate(['/docks/edit', dock.id]);
  }

  onDeleteDock(dock: Dock): void {
    if (confirm(`Are you sure you want to delete dock "${dock.name}"?`)) {
      this.dockService.deleteDock(dock.id).subscribe({
        next: () => {
          this.loadDocks(); // Reload the list
        },
        error: (error) => {
          console.error('Error deleting dock:', error);
          alert('Failed to delete dock. Please try again.');
        }
      });
    }
  }
}
