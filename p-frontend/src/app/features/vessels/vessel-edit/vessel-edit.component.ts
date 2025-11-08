import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, ActivatedRoute, RouterModule } from '@angular/router';
import { VesselService, Vessel } from '../../../core/services/vessel.service';

@Component({
  selector: 'app-vessel-edit',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './vessel-edit.component.html',
  styleUrls: ['./vessel-edit.component.scss']
})
export class VesselEditComponent implements OnInit {
  vessel: Vessel = {
    id: 0,
    name: '',
    imoNumber: '',
    vesselType: '',
    length: 0,
    flagCountry: '',
    status: '',
    scheduledArrival: '',
    scheduledDeparture: '',
    actualArrival: '',
    actualDeparture: ''
  };
  
  loading = false;
  error = '';
  vesselId: number = 0;

  constructor(
    private vesselService: VesselService,
    private router: Router,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.vesselId = Number(this.route.snapshot.paramMap.get('id'));
    if (this.vesselId) {
      this.loadVessel();
    } else {
      this.error = 'Invalid vessel ID';
    }
  }

  loadVessel(): void {
    this.loading = true;
    this.vesselService.getVesselById(this.vesselId).subscribe({
      next: (vessel) => {
        this.vessel = {
          ...vessel,
          scheduledArrival: this.formatDateForInput(vessel.scheduledArrival || null),
          scheduledDeparture: this.formatDateForInput(vessel.scheduledDeparture || null),
          actualArrival: this.formatDateForInput(vessel.actualArrival || null),
          actualDeparture: this.formatDateForInput(vessel.actualDeparture || null)
        };
        this.loading = false;
      },
      error: (error) => {
        console.error('Error loading vessel:', error);
        this.error = 'Failed to load vessel data';
        this.loading = false;
      }
    });
  }

  formatDateForInput(dateString: string | null): string {
    if (!dateString) return '';
    const date = new Date(dateString);
    return date.toISOString().slice(0, 16); // Format for datetime-local input
  }

  onSubmit(): void {
    if (this.loading) return;
    
    this.loading = true;
    this.error = '';

    // Convert form data to proper format
    const vesselData = {
      ...this.vessel,
      length: parseFloat(this.vessel.length.toString()) || 0,
      scheduledArrival: this.vessel.scheduledArrival || null,
      scheduledDeparture: this.vessel.scheduledDeparture || null,
      actualArrival: this.vessel.actualArrival || null,
      actualDeparture: this.vessel.actualDeparture || null
    };

    this.vesselService.updateVessel(this.vesselId, vesselData).subscribe({
      next: (response) => {
        console.log('Vessel updated successfully:', response);
        this.router.navigate(['/vessels']);
      },
      error: (error) => {
        console.error('Error updating vessel:', error);
        this.error = error.error?.message || 'Failed to update vessel. Please try again.';
        this.loading = false;
      }
    });
  }

  onCancel(): void {
    this.router.navigate(['/vessels']);
  }
}
