import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterModule, NavigationEnd } from '@angular/router';
import { FormBuilder, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { DeliveryService, Delivery, PageResponse } from '../../core/services/delivery.service';
import { DataTableComponent, TableColumn, PageInfo } from '../../shared/components/data-table/data-table.component';
import { Subscription } from 'rxjs';
import { filter } from 'rxjs/operators';

@Component({
  selector: 'app-deliveries-list',
  standalone: true,
  imports: [CommonModule, RouterModule, ReactiveFormsModule, DataTableComponent],
  templateUrl: './deliveries-list.component.html',
  styleUrls: ['./deliveries-list.component.scss']
})
export class DeliveriesListComponent implements OnInit, OnDestroy {
  deliveries: Delivery[] = [];
  loading = false;
  error = '';
  
  // Table configuration
  columns: TableColumn[] = [
    { key: 'id', label: 'ID', sortable: true },
    { key: 'cargoId', label: 'Cargo ID', sortable: true },
    { key: 'destinationAddress', label: 'Destination', sortable: false },
    { key: 'vehicleRegistration', label: 'Vehicle', sortable: false },
    { key: 'driverName', label: 'Driver', sortable: false },
    { key: 'deliveryStatus', label: 'Status', sortable: true },
    { key: 'scheduledDeliveryTime', label: 'Scheduled', sortable: true }
  ];
  
  pageInfo: PageInfo = {
    page: 0,
    size: 20,
    totalElements: 0,
    totalPages: 0
  };
  
  // Filter form
  filterForm: FormGroup;
  deliveryStatuses: string[] = [];
  
  // Router subscription for refresh
  private routerSubscription: Subscription = new Subscription();

  constructor(
    private deliveryService: DeliveryService,
    private router: Router,
    private fb: FormBuilder
  ) {
    this.filterForm = this.fb.group({
      search: [''],
      status: ['']
    });
  }

  ngOnInit(): void {
    this.loadDeliveries();
    this.loadFilterOptions();
    
    // Setup filter listeners
    this.filterForm.valueChanges.subscribe(() => {
      this.pageInfo.page = 0;
      this.loadDeliveries();
    });
    
    // Listen for navigation events to refresh data when returning from create/edit pages
    this.routerSubscription = this.router.events
      .pipe(filter(event => event instanceof NavigationEnd))
      .subscribe((event: NavigationEnd) => {
        if (event.url === '/deliveries') {
          this.loadDeliveries();
        }
      });
  }

  ngOnDestroy(): void {
    this.routerSubscription.unsubscribe();
  }

  loadDeliveries(): void {
    this.loading = true;
    this.error = '';
    
    this.deliveryService.getAllDeliveries(this.pageInfo.page, this.pageInfo.size).subscribe({
      next: (response: PageResponse<Delivery>) => {
        let deliveries = response.content;
        
        // Apply client-side filtering
        const filters = this.filterForm.value;
        
        if (filters.search) {
          const searchTerm = filters.search.toLowerCase();
          deliveries = deliveries.filter(delivery => 
            delivery.destinationAddress.toLowerCase().includes(searchTerm) ||
            delivery.driverName.toLowerCase().includes(searchTerm) ||
            delivery.vehicleRegistration.toLowerCase().includes(searchTerm) ||
            (delivery.notes && delivery.notes.toLowerCase().includes(searchTerm)) ||
            delivery.id.toString().includes(searchTerm) ||
            delivery.cargoId.toString().includes(searchTerm)
          );
        }
        
        if (filters.status) {
          deliveries = deliveries.filter(delivery => 
            delivery.deliveryStatus === filters.status
          );
        }
        
        this.deliveries = deliveries;
        this.pageInfo.totalElements = response.totalElements;
        this.pageInfo.totalPages = response.totalPages;
        this.loading = false;
      },
      error: (error) => {
        console.error('Error loading deliveries:', error);
        this.error = `Failed to load deliveries: ${error.error?.message || error.message || 'Unknown error'}`;
        this.deliveries = [];
        this.loading = false;
      }
    });
  }

  loadFilterOptions(): void {
    // Fetch deliveries to extract unique statuses
    this.deliveryService.getAllDeliveries(0, 100).subscribe({
      next: (response: PageResponse<Delivery>) => {
        const uniqueStatuses = new Set<string>();
        
        response.content.forEach(delivery => {
          if (delivery.deliveryStatus) {
            uniqueStatuses.add(delivery.deliveryStatus);
          }
        });
        
        // Sort alphabetically
        this.deliveryStatuses = Array.from(uniqueStatuses).sort();
        
        console.log('Loaded delivery statuses from database:', this.deliveryStatuses);
      },
      error: (error) => {
        console.error('Error loading filter options:', error);
        // Fallback to common values if loading fails
        this.deliveryStatuses = ['SCHEDULED', 'IN_TRANSIT', 'DELIVERED', 'FAILED', 'CANCELLED'];
      }
    });
  }

  onPageChange(page: number): void {
    this.pageInfo.page = page;
    this.loadDeliveries();
  }

  onCreateDelivery(): void {
    this.router.navigate(['/deliveries/create']);
  }

  onEditDelivery(delivery: Delivery): void {
    this.router.navigate(['/deliveries/edit', delivery.id]);
  }

  onDeleteDelivery(delivery: Delivery): void {
    if (confirm(`Are you sure you want to delete delivery #${delivery.id}?`)) {
      this.deliveryService.deleteDelivery(delivery.id).subscribe({
        next: () => {
          this.loadDeliveries(); // Reload the list
        },
        error: (error) => {
          console.error('Error deleting delivery:', error);
          alert('Failed to delete delivery. Please try again.');
        }
      });
    }
  }

  getStatusClass(status: string): string {
    switch (status?.toUpperCase()) {
      case 'DELIVERED': return 'success';
      case 'IN_TRANSIT': return 'warning';
      case 'SCHEDULED': return 'info';
      case 'FAILED': return 'danger';
      case 'CANCELLED': return 'secondary';
      default: return 'secondary';
    }
  }

  getStatusLabel(status: string): string {
    switch (status) {
      case 'SCHEDULED': return 'Scheduled';
      case 'IN_TRANSIT': return 'In Transit';
      case 'DELIVERED': return 'Delivered';
      case 'FAILED': return 'Failed';
      case 'CANCELLED': return 'Cancelled';
      default: return status;
    }
  }
}
