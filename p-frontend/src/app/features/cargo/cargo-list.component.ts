import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterModule, NavigationEnd } from '@angular/router';
import { FormBuilder, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { Subscription } from 'rxjs';
import { filter } from 'rxjs/operators';
import { CargoService, Cargo, PageResponse } from '../../core/services/cargo.service';
import { DataTableComponent, TableColumn, PageInfo } from '../../shared/components/data-table/data-table.component';

@Component({
  selector: 'app-cargo-list',
  standalone: true,
  imports: [CommonModule, RouterModule, ReactiveFormsModule, DataTableComponent],
  templateUrl: './cargo-list.component.html',
  styleUrls: ['./cargo-list.component.scss']
})
export class CargoListComponent implements OnInit, OnDestroy {
  cargoList: Cargo[] = [];
  loading = false;
  error = '';
  
  // Filter form
  filterForm: FormGroup;
  cargoTypes: string[] = [];
  customsStatuses: string[] = [];
  
  private routerSubscription: Subscription = new Subscription();
  
  // Table configuration
  columns: TableColumn[] = [
    { key: 'vesselId', label: 'Vessel ID', sortable: true },
    { key: 'description', label: 'Description', sortable: false },
    { key: 'weightTons', label: 'Weight (tons)', sortable: true },
    { key: 'isDangerous', label: 'Dangerous', sortable: true },
    { key: 'customsStatus', label: 'Customs Status', sortable: true },
    { key: 'cargoType', label: 'Type', sortable: true },
    { key: 'origin', label: 'Origin', sortable: false },
    { key: 'destination', label: 'Destination', sortable: false }
  ];
  
  pageInfo: PageInfo = {
    page: 0,
    size: 20,
    totalElements: 0,
    totalPages: 0
  };

  constructor(
    private cargoService: CargoService,
    private router: Router,
    private fb: FormBuilder
  ) {
    this.filterForm = this.fb.group({
      search: [''],
      cargoType: [''],
      customsStatus: [''],
      isDangerous: ['']
    });
  }

  ngOnInit(): void {
    this.loadCargo();
    this.loadFilterOptions();
    
    // Setup filter listeners
    this.filterForm.valueChanges.subscribe(() => {
      this.pageInfo.page = 0;
      this.loadCargo();
    });
    
    // Listen for navigation events to refresh data when returning from create/edit pages
    this.routerSubscription = this.router.events
      .pipe(filter(event => event instanceof NavigationEnd))
      .subscribe((event: NavigationEnd) => {
        if (event.url === '/cargo') {
          this.loadCargo();
        }
      });
  }

  ngOnDestroy(): void {
    this.routerSubscription.unsubscribe();
  }

  loadCargo(): void {
    this.loading = true;
    this.error = '';
    
    this.cargoService.getAllCargo(this.pageInfo.page, this.pageInfo.size).subscribe({
      next: (response: PageResponse<Cargo>) => {
        let cargo = response.content;
        
        // Apply client-side filtering
        const filters = this.filterForm.value;
        
        if (filters.search) {
          const searchTerm = filters.search.toLowerCase();
          cargo = cargo.filter(c => 
            (c.description && c.description.toLowerCase().includes(searchTerm)) ||
            (c.origin && c.origin.toLowerCase().includes(searchTerm)) ||
            (c.destination && c.destination.toLowerCase().includes(searchTerm))
          );
        }
        
        if (filters.cargoType) {
          cargo = cargo.filter(c => c.cargoType === filters.cargoType);
        }
        
        if (filters.customsStatus) {
          cargo = cargo.filter(c => c.customsStatus === filters.customsStatus);
        }
        
        if (filters.isDangerous !== '') {
          const isDangerous = filters.isDangerous === 'true';
          cargo = cargo.filter(c => c.isDangerous === isDangerous);
        }
        
        this.cargoList = cargo;
        this.pageInfo.totalElements = response.totalElements;
        this.pageInfo.totalPages = response.totalPages;
        this.loading = false;
      },
      error: (error) => {
        console.error('Error loading cargo:', error);
        this.error = `Failed to load cargo: ${error.error?.message || error.message || 'Unknown error'}`;
        this.cargoList = []; // Ensure array is always initialized
        this.loading = false;
      }
    });
  }

  loadFilterOptions(): void {
    // Fetch cargo to extract unique types and customs statuses
    this.cargoService.getAllCargo(0, 1000).subscribe({
      next: (response: PageResponse<Cargo>) => {
        console.log('Fetched cargo data:', response);
        console.log('Total cargo items fetched:', response.content?.length || 0);
        
        if (!response.content || response.content.length === 0) {
          console.warn('No cargo data returned from API, using defaults');
          this.cargoTypes = ['CONTAINER', 'BULK', 'LIQUID', 'REFRIGERATED', 'HAZMAT', 'GENERAL'];
          this.customsStatuses = ['PENDING', 'CLEARED', 'INSPECTION', 'HOLD', 'RELEASED'];
          return;
        }
        
        const uniqueTypes = new Set<string>();
        const uniqueStatuses = new Set<string>();
        
        response.content.forEach(cargo => {
          if (cargo.cargoType) {
            uniqueTypes.add(cargo.cargoType);
          }
          if (cargo.customsStatus) {
            uniqueStatuses.add(cargo.customsStatus);
          }
        });
        
        // Sort alphabetically
        this.cargoTypes = Array.from(uniqueTypes).sort();
        this.customsStatuses = Array.from(uniqueStatuses).sort();
        
        console.log('Extracted cargo types from database:', this.cargoTypes);
        console.log('Extracted customs statuses from database:', this.customsStatuses);
        
        // If extraction resulted in empty arrays, use defaults
        if (this.cargoTypes.length === 0) {
          console.warn('No cargo types found in database, using defaults');
          this.cargoTypes = ['CONTAINER', 'BULK', 'LIQUID', 'REFRIGERATED', 'HAZMAT', 'GENERAL'];
        }
        if (this.customsStatuses.length === 0) {
          console.warn('No customs statuses found in database, using defaults');
          this.customsStatuses = ['PENDING', 'CLEARED', 'INSPECTION', 'HOLD', 'RELEASED'];
        }
      },
      error: (error) => {
        console.error('Error loading filter options:', error);
        console.warn('Using fallback defaults due to API error');
        // Fallback to common values if loading fails
        this.cargoTypes = ['CONTAINER', 'BULK', 'LIQUID', 'REFRIGERATED', 'HAZMAT', 'GENERAL'];
        this.customsStatuses = ['PENDING', 'CLEARED', 'INSPECTION', 'HOLD', 'RELEASED'];
      }
    });
  }

  onPageChange(page: number): void {
    this.pageInfo.page = page;
    this.loadCargo();
  }

  onCreateCargo(): void {
    this.router.navigate(['/cargo/create']);
  }

  onEditCargo(cargo: Cargo): void {
    this.router.navigate(['/cargo/edit', cargo.id]);
  }

  onDeleteCargo(cargo: Cargo): void {
    if (confirm(`Are you sure you want to delete this cargo shipment?`)) {
      this.cargoService.deleteCargo(cargo.id).subscribe({
        next: () => {
          this.loadCargo(); // Reload the list
        },
        error: (error) => {
          console.error('Error deleting cargo:', error);
          alert('Failed to delete cargo. Please try again.');
        }
      });
    }
  }

  getStatusClass(status: string): string {
    switch (status?.toUpperCase()) {
      case 'DELIVERED': return 'success';
      case 'IN_TRANSIT': return 'warning';
      case 'PENDING': return 'info';
      case 'CANCELLED': return 'danger';
      default: return 'secondary';
    }
  }

  getTypeLabel(type: string): string {
    switch (type) {
      case 'CONTAINER': return 'Container';
      case 'BULK': return 'Bulk';
      case 'LIQUID': return 'Liquid';
      case 'REFRIGERATED': return 'Refrigerated';
      case 'HAZMAT': return 'Hazmat';
      case 'GENERAL': return 'General';
      default: return type?.replace('_', ' ');
    }
  }

  getStatusLabel(status: string): string {
    switch (status) {
      case 'PENDING': return 'Pending';
      case 'CLEARED': return 'Cleared';
      case 'INSPECTION': return 'Inspection';
      case 'HOLD': return 'Hold';
      case 'RELEASED': return 'Released';
      default: return status?.replace('_', ' ');
    }
  }
}
