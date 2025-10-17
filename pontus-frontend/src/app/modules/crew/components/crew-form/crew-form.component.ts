import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { CrewService } from '../../../../services/crew.service';
import { CrewCreateRequest, CrewUpdateRequest, CREW_RANKS, COMMON_NATIONALITIES } from '../../../../models/crew.model';

@Component({
  selector: 'app-crew-form',
  templateUrl: './crew-form.component.html',
  styleUrls: ['./crew-form.component.scss']
})
export class CrewFormComponent implements OnInit {
  crewForm!: FormGroup;
  loading = false;
  error = '';
  isEditMode = false;
  crewMemberId?: number;
  ranks = CREW_RANKS;
  nationalities = COMMON_NATIONALITIES;

  constructor(
    private formBuilder: FormBuilder,
    private crewService: CrewService,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.initializeForm();
    this.checkEditMode();
  }

  private initializeForm(): void {
    this.crewForm = this.formBuilder.group({
      firstName: ['', [Validators.required, Validators.maxLength(50)]],
      lastName: ['', [Validators.required, Validators.maxLength(50)]],
      nationality: ['', [Validators.required]],
      rank: ['', [Validators.required]],
      passportNumber: ['', [Validators.required, Validators.maxLength(20)]],
      certificateNumber: ['', [Validators.maxLength(50)]],
      vesselId: ['']
    });
  }

  private checkEditMode(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.isEditMode = true;
      this.crewMemberId = +id;
      this.loadCrewMember();
    }
  }

  private loadCrewMember(): void {
    if (!this.crewMemberId) return;

    this.loading = true;
    this.crewService.getCrewMemberById(this.crewMemberId).subscribe({
      next: (response) => {
        if (response.data) {
          this.crewForm.patchValue({
            firstName: response.data.firstName,
            lastName: response.data.lastName,
            nationality: response.data.nationality,
            rank: response.data.rank,
            passportNumber: response.data.passportNumber,
            certificateNumber: response.data.certificateNumber,
            vesselId: response.data.vesselId
          });
        }
        this.loading = false;
      },
      error: (error) => {
        this.error = 'Failed to load crew member details';
        this.loading = false;
        console.error('Error loading crew member:', error);
      }
    });
  }

  get f() {
    return this.crewForm.controls;
  }

  onSubmit(): void {
    if (this.crewForm.invalid) {
      this.markFormGroupTouched();
      return;
    }

    this.loading = true;
    this.error = '';

    if (this.isEditMode && this.crewMemberId) {
      this.updateCrewMember();
    } else {
      this.createCrewMember();
    }
  }

  private createCrewMember(): void {
    const createRequest: CrewCreateRequest = {
      firstName: this.f['firstName'].value,
      lastName: this.f['lastName'].value,
      nationality: this.f['nationality'].value,
      rank: this.f['rank'].value,
      passportNumber: this.f['passportNumber'].value,
      certificateNumber: this.f['certificateNumber'].value || undefined,
      vesselId: this.f['vesselId'].value || undefined
    };

    this.crewService.createCrewMember(createRequest).subscribe({
      next: (response) => {
        this.loading = false;
        this.router.navigate(['/crew']);
      },
      error: (error) => {
        this.loading = false;
        this.error = error.error?.message || 'Failed to create crew member';
      }
    });
  }

  private updateCrewMember(): void {
    if (!this.crewMemberId) return;

    const updateRequest: CrewUpdateRequest = {
      firstName: this.f['firstName'].value,
      lastName: this.f['lastName'].value,
      nationality: this.f['nationality'].value,
      rank: this.f['rank'].value,
      passportNumber: this.f['passportNumber'].value,
      certificateNumber: this.f['certificateNumber'].value || undefined,
      vesselId: this.f['vesselId'].value || undefined
    };

    this.crewService.updateCrewMember(this.crewMemberId, updateRequest).subscribe({
      next: (response) => {
        this.loading = false;
        this.router.navigate(['/crew']);
      },
      error: (error) => {
        this.loading = false;
        this.error = error.error?.message || 'Failed to update crew member';
      }
    });
  }

  private markFormGroupTouched(): void {
    Object.keys(this.crewForm.controls).forEach(key => {
      const control = this.crewForm.get(key);
      control?.markAsTouched();
    });
  }

  cancel(): void {
    this.router.navigate(['/crew']);
  }
}
