import { Component, Input, Output, EventEmitter } from '@angular/core';
import { Router } from '@angular/router';
import { DeliveryService } from '../../../../services/delivery.service';
import { AuthService } from '../../../../services/auth.service';
import { Delivery } from '../../../../models/delivery.model';

@Component({
  selector: 'app-delivery-table',
  templateUrl: './delivery-table.component.html',
  styleUrls: ['./delivery-table.component.scss']
})
export class DeliveryTableComponent {
  @Input() deliveries: Delivery[] = [];
  @Input() loading = false;
  @Output() deliveryDeleted = new EventEmitter<number>();

  constructor(
    private deliveryService: DeliveryService,
    private authService: AuthService,
    private router: Router
  ) {}

  editDelivery(delivery: Delivery): void {
    this.router.navigate(['/deliveries/edit', delivery.id]);
  }

  deleteDelivery(delivery: Delivery): void {
    if (confirm(`Are you sure you want to delete delivery to "${delivery.destination}"?`)) {
      this.deliveryService.deleteDelivery(delivery.id).subscribe({
        next: () => {
          this.deliveryDeleted.emit(delivery.id);
        },
        error: (error) => {
          console.error('Error deleting delivery:', error);
          alert('Failed to delete delivery. Please try again.');
        }
      });
    }
  }

  canUpdate(): boolean {
    return this.authService.canUpdate();
  }

  canDelete(): boolean {
    return this.authService.canDelete();
  }

  trackByDeliveryId(index: number, delivery: Delivery): number {
    return delivery.id;
  }

  formatDate(dateString: string | undefined): string {
    if (!dateString) return '-';
    return new Date(dateString).toLocaleDateString();
  }
}
