import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ValidatorFn, AbstractControl, ValidationErrors } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { VesselService } from '../../../../services/vessel.service';
import { Vessel, VesselCreateRequest, VesselUpdateRequest, VesselStatus, VesselType } from '../../../../models/vessel.model';

@Component({
  selector: 'app-vessel-form',
  templateUrl: './vessel-form.component.html',
  styleUrls: ['./vessel-form.component.scss']
})
export class VesselFormComponent implements OnInit {
  vesselForm!: FormGroup;
  isEditMode = false;
  vesselId: number | null = null;
  loading = false;
  submitting = false;
  error: string | null = null;

  // Enum options for dropdowns
  vesselStatuses = Object.values(VesselStatus);
  vesselTypes = Object.values(VesselType);

  // Common flag countries for dropdown
  flagCountries = [
    'Panama', 'Liberia', 'Marshall Islands', 'Hong Kong', 'Singapore',
    'Bahamas', 'Malta', 'Cyprus', 'China', 'Greece', 'Japan', 'Norway',
    'United Kingdom', 'Germany', 'Italy', 'Netherlands', 'Denmark',
    'United States', 'South Korea', 'India', 'Turkey', 'Russia'
  ].sort();

  constructor(
    private fb: FormBuilder,
    private vesselService: VesselService,
    private route: ActivatedRoute,
    private router: Router
  ) {
    this.initializeForm();
  }

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      if (params['id']) {
        this.isEditMode = true;
        this.vesselId = +params['id'];
        this.loadVessel();
      }
    });
  }

  private initializeForm(): void {
    this.vesselForm = this.fb.group({
      name: ['', [Validators.required, Validators.minLength(2), Validators.maxLength(100)]],
      imoNumber: ['', [Validators.required, Validators.pattern(/^IMO\d{7}$/)]],
      vesselType: ['', Validators.required],
      length: ['', [Validators.required, Validators.min(1), Validators.max(500)]],
      flagCountry: ['', [Validators.required, Validators.maxLength(50)]],
      status: [VesselStatus.SCHEDULED, Validators.required],
      scheduledArrival: [''],
      scheduledDeparture: [''],
      actualArrival: [''],
      actualDeparture: ['']
    });

    // Add custom validators for date consistency
    this.vesselForm.setValidators(this.dateConsistencyValidator());
  }

  private dateConsistencyValidator(): ValidatorFn {
    return (control: AbstractControl): ValidationErrors | null => {
      const form = control as FormGroup;
    const scheduledArrival = form.get('scheduledArrival')?.value;
    const scheduledDeparture = form.get('scheduledDeparture')?.value;
    const actualArrival = form.get('actualArrival')?.value;
    const actualDeparture = form.get('actualDeparture')?.value;

    const errors: any = {};

    // Scheduled departure must be after scheduled arrival
    if (scheduledArrival && scheduledDeparture) {
      if (new Date(scheduledDeparture) <= new Date(scheduledArrival)) {
        errors.scheduledDepartureBeforeArrival = true;
      }
    }

    // Actual departure must be after actual arrival
    if (actualArrival && actualDeparture) {
      if (new Date(actualDeparture) <= new Date(actualArrival)) {
        errors.actualDepartureBeforeArrival = true;
      }
    }

    // Actual arrival should be after scheduled arrival (warning, not error)
    if (scheduledArrival && actualArrival) {
      if (new Date(actualArrival) < new Date(scheduledArrival)) {
        errors.actualArrivalBeforeScheduled = true;
      }
    }

    return Object.keys(errors).length > 0 ? errors : null;
    };
  }

  private loadVessel(): void {
    if (!this.vesselId) return;

    this.loading = true;
    this.error = null;

    this.vesselService.getVessel(this.vesselId).subscribe({
      next: (vessel) => {
        this.populateForm(vessel);
        this.loading = false;
      },
      error: (error) => {
        this.error = 'Failed to load vessel data';
        this.loading = false;
        console.error('Error loading vessel:', error);
      }
    });
  }

  private populateForm(vessel: Vessel): void {
    this.vesselForm.patchValue({
      name: vessel.name,
      imoNumber: vessel.imoNumber,
      vesselType: vessel.vesselType,
      length: vessel.length,
      flagCountry: vessel.flagCountry,
      status: vessel.status,
      scheduledArrival: vessel.scheduledArrival ? this.formatDateForInput(vessel.scheduledArrival) : '',
      scheduledDeparture: vessel.scheduledDeparture ? this.formatDateForInput(vessel.scheduledDeparture) : '',
      actualArrival: vessel.actualArrival ? this.formatDateForInput(vessel.actualArrival) : '',
      actualDeparture: vessel.actualDeparture ? this.formatDateForInput(vessel.actualDeparture) : ''
    });
  }

  private formatDateForInput(dateString: string): string {
    const date = new Date(dateString);
    return date.toISOString().slice(0, 16); // Format for datetime-local input
  }

  onSubmit(): void {
    if (this.vesselForm.invalid) {
      this.markFormGroupTouched();
      return;
    }

    this.submitting = true;
    this.error = null;

    const formValue = this.vesselForm.value;
    
    if (this.isEditMode && this.vesselId) {
      const updateRequest: VesselUpdateRequest = {
        name: formValue.name,
        vesselType: formValue.vesselType,
        length: formValue.length,
        flagCountry: formValue.flagCountry,
        status: formValue.status,
        scheduledArrival: formValue.scheduledArrival || undefined,
        scheduledDeparture: formValue.scheduledDeparture || undefined,
        actualArrival: formValue.actualArrival || undefined,
        actualDeparture: formValue.actualDeparture || undefined
      };

      this.vesselService.updateVessel(this.vesselId, updateRequest).subscribe({
        next: () => {
          this.router.navigate(['/vessels']);
        },
        error: (error) => {
          this.error = error.error?.message || 'Failed to update vessel';
          this.submitting = false;
        }
      });
    } else {
      const createRequest: VesselCreateRequest = {
        name: formValue.name,
        imoNumber: formValue.imoNumber,
        vesselType: formValue.vesselType,
        length: formValue.length,
        flagCountry: formValue.flagCountry,
        status: formValue.status,
        scheduledArrival: formValue.scheduledArrival || undefined,
        scheduledDeparture: formValue.scheduledDeparture || undefined,
        actualArrival: formValue.actualArrival || undefined,
        actualDeparture: formValue.actualDeparture || undefined
      };

      this.vesselService.createVessel(createRequest).subscribe({
        next: () => {
          this.router.navigate(['/vessels']);
        },
        error: (error) => {
          this.error = error.error?.message || 'Failed to create vessel';
          this.submitting = false;
        }
      });
    }
  }

  onCancel(): void {
    this.router.navigate(['/vessels']);
  }

  private markFormGroupTouched(): void {
    Object.keys(this.vesselForm.controls).forEach(key => {
      const control = this.vesselForm.get(key);
      control?.markAsTouched();
    });
  }

  isFieldInvalid(fieldName: string): boolean {
    const field = this.vesselForm.get(fieldName);
    return !!(field && field.invalid && (field.dirty || field.touched));
  }

  getFieldError(fieldName: string): string {
    const field = this.vesselForm.get(fieldName);
    if (!field || !field.errors) return '';

    const errors = field.errors;
    
    if (errors['required']) return `${this.getFieldLabel(fieldName)} is required`;
    if (errors['minlength']) return `${this.getFieldLabel(fieldName)} must be at least ${errors['minlength'].requiredLength} characters`;
    if (errors['maxlength']) return `${this.getFieldLabel(fieldName)} must not exceed ${errors['maxlength'].requiredLength} characters`;
    if (errors['pattern']) return `${this.getFieldLabel(fieldName)} format is invalid`;
    if (errors['min']) return `${this.getFieldLabel(fieldName)} must be at least ${errors['min'].min}`;
    if (errors['max']) return `${this.getFieldLabel(fieldName)} must not exceed ${errors['max'].max}`;

    return 'Invalid value';
  }

  private getFieldLabel(fieldName: string): string {
    const labels: { [key: string]: string } = {
      name: 'Vessel Name',
      imoNumber: 'IMO Number',
      vesselType: 'Vessel Type',
      length: 'Length',
      flagCountry: 'Flag Country',
      status: 'Status',
      scheduledArrival: 'Scheduled Arrival',
      scheduledDeparture: 'Scheduled Departure',
      actualArrival: 'Actual Arrival',
      actualDeparture: 'Actual Departure'
    };
    return labels[fieldName] || fieldName;
  }

  getFormErrors(): string[] {
    const formErrors = this.vesselForm.errors;
    if (!formErrors) return [];

    const errors: string[] = [];
    if (formErrors['scheduledDepartureBeforeArrival']) {
      errors.push('Scheduled departure must be after scheduled arrival');
    }
    if (formErrors['actualDepartureBeforeArrival']) {
      errors.push('Actual departure must be after actual arrival');
    }
    if (formErrors['actualArrivalBeforeScheduled']) {
      errors.push('Warning: Actual arrival is before scheduled arrival');
    }

    return errors;
  }
}
