import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { DeliveryService } from '../../../../services/delivery.service';
import { DeliveryCreateRequest, DeliveryUpdateRequest, DeliveryStatus } from '../../../../models/delivery.model';

@Component({
  selector: 'app-delivery-form',
  templateUrl: './delivery-form.component.html',
  styleUrls: ['./delivery-form.component.scss']
})
export class DeliveryFormComponent implements OnInit {
  deliveryForm!: FormGroup;
  loading = false;
  error = '';
  isEditMode = false;
  deliveryId?: number;
  deliveryStatuses = Object.values(DeliveryStatus);

  constructor(
    private formBuilder: FormBuilder,
    private deliveryService: DeliveryService,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.initializeForm();
    this.checkEditMode();
  }

  private initializeForm(): void {
    this.deliveryForm = this.formBuilder.group({
      cargoId: ['', [Validators.required]],
      destination: ['', [Validators.required, Validators.maxLength(200)]],
      driverName: ['', [Validators.required, Validators.maxLength(100)]],
      vehiclePlate: ['', [Validators.required, Validators.maxLength(20)]],
      scheduledDate: ['', [Validators.required]],
      actualDate: [''],
      status: [DeliveryStatus.SCHEDULED, [Validators.required]],
      notes: ['', [Validators.maxLength(1000)]]
    });
  }

  private checkEditMode(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.isEditMode = true;
      this.deliveryId = +id;
      this.loadDelivery();
    }
  }

  private loadDelivery(): void {
    if (!this.deliveryId) return;

    this.loading = true;
    this.deliveryService.getDeliveryById(this.deliveryId).subscribe({
      next: (response) => {
        if (response.data) {
          this.deliveryForm.patchValue({
            cargoId: response.data.cargoId,
            destination: response.data.destination,
            driverName: response.data.driverName,
            vehiclePlate: response.data.vehiclePlate,
            scheduledDate: response.data.scheduledDate ? 
              new Date(response.data.scheduledDate).toISOString().split('T')[0] : '',
            actualDate: response.data.actualDate ? 
              new Date(response.data.actualDate).toISOString().split('T')[0] : '',
            status: response.data.status,
            notes: response.data.notes
          });
        }
        this.loading = false;
      },
      error: (error) => {
        this.error = 'Failed to load delivery details';
        this.loading = false;
        console.error('Error loading delivery:', error);
      }
    });
  }

  get f() {
    return this.deliveryForm.controls;
  }

  onSubmit(): void {
    if (this.deliveryForm.invalid) {
      this.markFormGroupTouched();
      return;
    }

    this.loading = true;
    this.error = '';

    if (this.isEditMode && this.deliveryId) {
      this.updateDelivery();
    } else {
      this.createDelivery();
    }
  }

  private createDelivery(): void {
    const createRequest: DeliveryCreateRequest = {
      cargoId: parseInt(this.f['cargoId'].value),
      destination: this.f['destination'].value,
      driverName: this.f['driverName'].value,
      vehiclePlate: this.f['vehiclePlate'].value,
      scheduledDate: this.f['scheduledDate'].value,
      notes: this.f['notes'].value || undefined
    };

    this.deliveryService.createDelivery(createRequest).subscribe({
      next: (response) => {
        this.loading = false;
        this.router.navigate(['/deliveries']);
      },
      error: (error) => {
        this.loading = false;
        this.error = error.error?.message || 'Failed to create delivery';
      }
    });
  }

  private updateDelivery(): void {
    if (!this.deliveryId) return;

    const updateRequest: DeliveryUpdateRequest = {
      cargoId: parseInt(this.f['cargoId'].value),
      destination: this.f['destination'].value,
      driverName: this.f['driverName'].value,
      vehiclePlate: this.f['vehiclePlate'].value,
      scheduledDate: this.f['scheduledDate'].value,
      actualDate: this.f['actualDate'].value || undefined,
      status: this.f['status'].value,
      notes: this.f['notes'].value || undefined
    };

    this.deliveryService.updateDelivery(this.deliveryId, updateRequest).subscribe({
      next: (response) => {
        this.loading = false;
        this.router.navigate(['/deliveries']);
      },
      error: (error) => {
        this.loading = false;
        this.error = error.error?.message || 'Failed to update delivery';
      }
    });
  }

  private markFormGroupTouched(): void {
    Object.keys(this.deliveryForm.controls).forEach(key => {
      const control = this.deliveryForm.get(key);
      control?.markAsTouched();
    });
  }

  cancel(): void {
    this.router.navigate(['/deliveries']);
  }
}
