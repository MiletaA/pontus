import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterModule, ActivatedRoute } from '@angular/router';
import { DeliveryService, Delivery } from '../../../core/services/delivery.service';

@Component({
  selector: 'app-delivery-edit',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterModule],
  templateUrl: './delivery-edit.component.html',
  styleUrls: ['./delivery-edit.component.scss']
})
export class DeliveryEditComponent implements OnInit {
  deliveryForm!: FormGroup;
  deliveryId!: number;
  delivery?: Delivery;
  loading = false;
  loadingDelivery = true;
  error = '';

  constructor(
    private fb: FormBuilder,
    private deliveryService: DeliveryService,
    private router: Router,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.deliveryId = +this.route.snapshot.params['id'];
    this.loadDelivery();
  }

  loadDelivery(): void {
    this.loadingDelivery = true;
    this.deliveryService.getDeliveryById(this.deliveryId).subscribe({
      next: (delivery) => {
        this.delivery = delivery;
        this.initForm(delivery);
        this.loadingDelivery = false;
      },
      error: (error) => {
        console.error('Error loading delivery:', error);
        this.error = 'Failed to load delivery details';
        this.loadingDelivery = false;
      }
    });
  }

  initForm(delivery: Delivery): void {
    this.deliveryForm = this.fb.group({
      cargoId: [delivery.cargoId, [Validators.required, Validators.min(1)]],
      destinationAddress: [delivery.destinationAddress, Validators.required],
      deliveryStatus: [delivery.deliveryStatus, Validators.required],
      vehicleRegistration: [delivery.vehicleRegistration, Validators.required],
      driverName: [delivery.driverName, Validators.required],
      deliveryTime: [this.formatDateTimeForInput(delivery.deliveryTime)],
      scheduledDeliveryTime: [this.formatDateTimeForInput(delivery.scheduledDeliveryTime)],
      notes: [delivery.notes || '']
    });
  }

  formatDateTimeForInput(dateTime?: string): string {
    if (!dateTime) return '';
    // Convert to format required by datetime-local input
    return dateTime.slice(0, 16);
  }

  onSubmit(): void {
    if (this.deliveryForm.invalid) {
      Object.keys(this.deliveryForm.controls).forEach(key => {
        this.deliveryForm.get(key)?.markAsTouched();
      });
      return;
    }

    this.loading = true;
    this.error = '';

    this.deliveryService.updateDelivery(this.deliveryId, this.deliveryForm.value).subscribe({
      next: (delivery) => {
        this.router.navigate(['/deliveries']);
      },
      error: (error) => {
        console.error('Error updating delivery:', error);
        this.error = error.error?.message || 'Failed to update delivery. Please try again.';
        this.loading = false;
      }
    });
  }
}
