import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { DeliveryService } from '../../../../services/delivery.service';
import { AuthService } from '../../../../services/auth.service';
import { Delivery, DeliveryFilters } from '../../../../models/delivery.model';

@Component({
  selector: 'app-delivery-list',
  templateUrl: './delivery-list.component.html',
  styleUrls: ['./delivery-list.component.scss']
})
export class DeliveryListComponent implements OnInit {
  deliveries: Delivery[] = [];
  filteredDeliveries: Delivery[] = [];
  loading = true;
  error = '';
  filters: DeliveryFilters = {};

  constructor(
    private deliveryService: DeliveryService,
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.loadDeliveries();
  }

  loadDeliveries(): void {
    this.loading = true;
    this.error = '';

    this.deliveryService.getAllDeliveries().subscribe({
      next: (response) => {
        this.deliveries = response.data || [];
        this.filteredDeliveries = [...this.deliveries];
        this.loading = false;
      },
      error: (error) => {
        this.error = 'Failed to load deliveries';
        this.loading = false;
        console.error('Error loading deliveries:', error);
      }
    });
  }

  onFiltersChanged(filters: DeliveryFilters): void {
    this.filters = filters;
    this.applyFilters();
  }

  private applyFilters(): void {
    this.filteredDeliveries = this.deliveries.filter(delivery => {
      if (this.filters.status && delivery.status !== this.filters.status) {
        return false;
      }
      if (this.filters.cargoId !== undefined && delivery.cargoId !== this.filters.cargoId) {
        return false;
      }
      if (this.filters.search) {
        const searchTerm = this.filters.search.toLowerCase();
        return delivery.destination.toLowerCase().includes(searchTerm) ||
               delivery.driverName.toLowerCase().includes(searchTerm) ||
               delivery.vehiclePlate.toLowerCase().includes(searchTerm) ||
               delivery.id.toString().includes(searchTerm);
      }
      return true;
    });
  }

  onDeliveryDeleted(deliveryId: number): void {
    this.deliveries = this.deliveries.filter(delivery => delivery.id !== deliveryId);
    this.applyFilters();
  }

  canCreate(): boolean {
    return this.authService.canCreate();
  }

  createNewDelivery(): void {
    this.router.navigate(['/deliveries/new']);
  }

  refreshData(): void {
    this.loadDeliveries();
  }

  getPendingDeliveriesCount(): number {
    return this.deliveries.filter(d => d.status === 'SCHEDULED').length;
  }

  getDeliveredDeliveriesCount(): number {
    return this.deliveries.filter(d => d.status === 'DELIVERED').length;
  }
}
