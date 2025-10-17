import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { DockService } from '../../../../services/dock.service';
import { DockCreateRequest, DockUpdateRequest } from '../../../../models/dock.model';

@Component({
  selector: 'app-dock-form',
  templateUrl: './dock-form.component.html',
  styleUrls: ['./dock-form.component.scss']
})
export class DockFormComponent implements OnInit {
  dockForm!: FormGroup;
  loading = false;
  error = '';
  isEditMode = false;
  dockId?: number;

  constructor(
    private formBuilder: FormBuilder,
    private dockService: DockService,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.initializeForm();
    this.checkEditMode();
  }

  private initializeForm(): void {
    this.dockForm = this.formBuilder.group({
      name: ['', [Validators.required, Validators.maxLength(100)]],
      maxLength: ['', [Validators.required, Validators.min(0.01)]],
      handlesDangerous: [false],
      description: ['', [Validators.maxLength(500)]]
    });
  }

  private checkEditMode(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.isEditMode = true;
      this.dockId = +id;
      this.loadDock();
    }
  }

  private loadDock(): void {
    if (!this.dockId) return;

    this.loading = true;
    this.dockService.getDockById(this.dockId).subscribe({
      next: (response) => {
        if (response.data) {
          this.dockForm.patchValue({
            name: response.data.name,
            maxLength: response.data.maxLength,
            handlesDangerous: response.data.handlesDangerous,
            description: response.data.description
          });
        }
        this.loading = false;
      },
      error: (error) => {
        this.error = 'Failed to load dock details';
        this.loading = false;
        console.error('Error loading dock:', error);
      }
    });
  }

  get f() {
    return this.dockForm.controls;
  }

  onSubmit(): void {
    if (this.dockForm.invalid) {
      this.markFormGroupTouched();
      return;
    }

    this.loading = true;
    this.error = '';

    if (this.isEditMode && this.dockId) {
      this.updateDock();
    } else {
      this.createDock();
    }
  }

  private createDock(): void {
    const createRequest: DockCreateRequest = {
      name: this.f['name'].value,
      maxLength: this.f['maxLength'].value,
      handlesDangerous: this.f['handlesDangerous'].value,
      description: this.f['description'].value || undefined
    };

    this.dockService.createDock(createRequest).subscribe({
      next: (response) => {
        this.loading = false;
        this.router.navigate(['/docks']);
      },
      error: (error) => {
        this.loading = false;
        this.error = error.error?.message || 'Failed to create dock';
      }
    });
  }

  private updateDock(): void {
    if (!this.dockId) return;

    const updateRequest: DockUpdateRequest = {
      name: this.f['name'].value,
      maxLength: this.f['maxLength'].value,
      handlesDangerous: this.f['handlesDangerous'].value,
      description: this.f['description'].value || undefined
    };

    this.dockService.updateDock(this.dockId, updateRequest).subscribe({
      next: (response) => {
        this.loading = false;
        this.router.navigate(['/docks']);
      },
      error: (error) => {
        this.loading = false;
        this.error = error.error?.message || 'Failed to update dock';
      }
    });
  }

  private markFormGroupTouched(): void {
    Object.keys(this.dockForm.controls).forEach(key => {
      const control = this.dockForm.get(key);
      control?.markAsTouched();
    });
  }

  cancel(): void {
    this.router.navigate(['/docks']);
  }
}
