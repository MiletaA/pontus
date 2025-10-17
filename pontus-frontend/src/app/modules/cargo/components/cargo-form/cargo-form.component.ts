import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { CargoService } from '../../../../services/cargo.service';
import { CargoCreateRequest, CargoUpdateRequest, CustomsStatus, CUSTOMS_STATUSES } from '../../../../models/cargo.model';

@Component({
  selector: 'app-cargo-form',
  templateUrl: './cargo-form.component.html',
  styleUrls: ['./cargo-form.component.scss']
})
export class CargoFormComponent implements OnInit {
  cargoForm!: FormGroup;
  loading = false;
  error = '';
  isEditMode = false;
  cargoId?: number;
  customsStatuses = CUSTOMS_STATUSES;

  constructor(
    private formBuilder: FormBuilder,
    private cargoService: CargoService,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.initializeForm();
    this.checkEditMode();
  }

  private initializeForm(): void {
    this.cargoForm = this.formBuilder.group({
      vesselId: ['', [Validators.required]],
      description: ['', [Validators.required, Validators.maxLength(1000)]],
      weight: ['', [Validators.required, Validators.min(0.01)]],
      isDangerous: [false],
      customsStatus: [CustomsStatus.PENDING, [Validators.required]],
      origin: ['', [Validators.maxLength(100)]],
      destination: ['', [Validators.maxLength(100)]]
    });
  }

  private checkEditMode(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.isEditMode = true;
      this.cargoId = +id;
      this.loadCargo();
    }
  }

  private loadCargo(): void {
    if (!this.cargoId) return;

    this.loading = true;
    this.cargoService.getCargoById(this.cargoId).subscribe({
      next: (response) => {
        if (response.data) {
          this.cargoForm.patchValue({
            vesselId: response.data.vesselId,
            description: response.data.description,
            weight: response.data.weight,
            isDangerous: response.data.isDangerous,
            customsStatus: response.data.customsStatus,
            origin: response.data.origin,
            destination: response.data.destination
          });
        }
        this.loading = false;
      },
      error: (error) => {
        this.error = 'Failed to load cargo details';
        this.loading = false;
        console.error('Error loading cargo:', error);
      }
    });
  }

  get f() {
    return this.cargoForm.controls;
  }

  onSubmit(): void {
    if (this.cargoForm.invalid) {
      this.markFormGroupTouched();
      return;
    }

    this.loading = true;
    this.error = '';

    if (this.isEditMode && this.cargoId) {
      this.updateCargo();
    } else {
      this.createCargo();
    }
  }

  private createCargo(): void {
    const createRequest: CargoCreateRequest = {
      vesselId: this.f['vesselId'].value,
      description: this.f['description'].value,
      weight: this.f['weight'].value,
      isDangerous: this.f['isDangerous'].value,
      customsStatus: this.f['customsStatus'].value,
      origin: this.f['origin'].value || undefined,
      destination: this.f['destination'].value || undefined
    };

    this.cargoService.createCargo(createRequest).subscribe({
      next: (response) => {
        this.loading = false;
        this.router.navigate(['/cargo']);
      },
      error: (error) => {
        this.loading = false;
        this.error = error.error?.message || 'Failed to create cargo';
      }
    });
  }

  private updateCargo(): void {
    if (!this.cargoId) return;

    const updateRequest: CargoUpdateRequest = {
      vesselId: this.f['vesselId'].value,
      description: this.f['description'].value,
      weight: this.f['weight'].value,
      isDangerous: this.f['isDangerous'].value,
      customsStatus: this.f['customsStatus'].value,
      origin: this.f['origin'].value || undefined,
      destination: this.f['destination'].value || undefined
    };

    this.cargoService.updateCargo(this.cargoId, updateRequest).subscribe({
      next: (response) => {
        this.loading = false;
        this.router.navigate(['/cargo']);
      },
      error: (error) => {
        this.loading = false;
        this.error = error.error?.message || 'Failed to update cargo';
      }
    });
  }

  private markFormGroupTouched(): void {
    Object.keys(this.cargoForm.controls).forEach(key => {
      const control = this.cargoForm.get(key);
      control?.markAsTouched();
    });
  }

  cancel(): void {
    this.router.navigate(['/cargo']);
  }
}
