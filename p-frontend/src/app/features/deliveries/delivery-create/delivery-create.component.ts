import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { DeliveryService } from '../../../core/services/delivery.service';
import { CargoService } from '../../../core/services/cargo.service';

@Component({
  selector: 'app-delivery-create',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterModule],
  templateUrl: './delivery-create.component.html',
  styleUrls: ['./delivery-create.component.scss']
})
export class DeliveryCreateComponent implements OnInit {
  deliveryForm: FormGroup;
  loading = false;
  error = '';
  
  // Dynamic options
  deliveryStatuses: string[] = [];

  constructor(
    private fb: FormBuilder,
    private deliveryService: DeliveryService,
    private cargoService: CargoService,
    private router: Router
  ) {
    this.deliveryForm = this.fb.group({
      cargoId: ['', [Validators.required, Validators.min(1)]],
      destinationAddress: ['', Validators.required],
      deliveryStatus: ['SCHEDULED', Validators.required],
      vehicleRegistration: ['', Validators.required],
      driverName: ['', Validators.required],
      deliveryTime: [''],
      scheduledDeliveryTime: [''],
      notes: ['']
    });
  }

  ngOnInit(): void {
    this.loadFilterOptions();
  }
  
  loadFilterOptions(): void {
    // Fetch deliveries to extract unique statuses
    this.deliveryService.getAllDeliveries(0, 100).subscribe({
      next: (response) => {
        const uniqueStatuses = new Set<string>();
        
        response.content.forEach(delivery => {
          if (delivery.deliveryStatus) {
            uniqueStatuses.add(delivery.deliveryStatus);
          }
        });
        
        // Sort alphabetically
        this.deliveryStatuses = Array.from(uniqueStatuses).sort();
        
        // If no data, provide defaults
        if (this.deliveryStatuses.length === 0) {
          this.deliveryStatuses = ['SCHEDULED', 'IN_TRANSIT', 'DELIVERED', 'FAILED', 'CANCELLED'];
        }
      },
      error: (error) => {
        console.error('Error loading filter options:', error);
        // Fallback to defaults
        this.deliveryStatuses = ['SCHEDULED', 'IN_TRANSIT', 'DELIVERED', 'FAILED', 'CANCELLED'];
      }
    });
  }
  
  getStatusLabel(status: string): string {
    switch (status) {
      case 'SCHEDULED': return 'Scheduled';
      case 'IN_TRANSIT': return 'In Transit';
      case 'DELIVERED': return 'Delivered';
      case 'FAILED': return 'Failed';
      case 'CANCELLED': return 'Cancelled';
      default: return status?.replace('_', ' ');
    }
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

    this.deliveryService.createDelivery(this.deliveryForm.value).subscribe({
      next: (delivery) => {
        this.router.navigate(['/deliveries']);
      },
      error: (error) => {
        console.error('Error creating delivery:', error);
        this.error = error.error?.message || 'Failed to create delivery. Please try again.';
        this.loading = false;
      }
    });
  }
}
