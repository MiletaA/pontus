import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { VesselService } from '../../../core/services/vessel.service';

@Component({
  selector: 'app-vessel-create',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './vessel-create.component.html',
  styleUrls: ['./vessel-create.component.scss']
})
export class VesselCreateComponent implements OnInit {
  vessel = {
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
  
  // Dynamic options
  vesselTypes: string[] = [];
  vesselStatuses: string[] = [];

  constructor(
    private vesselService: VesselService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.loadFilterOptions();
  }
  
  loadFilterOptions(): void {
    // Fetch vessels to extract unique types and statuses - increased to 1000 to get more variety
    this.vesselService.getAllVessels(0, 1000).subscribe({
      next: (response) => {
        console.log('Fetched vessel data:', response);
        console.log('Total vessels fetched:', response.content.length);
        
        const uniqueTypes = new Set<string>();
        const uniqueStatuses = new Set<string>();
        
        response.content.forEach(vessel => {
          if (vessel.vesselType) {
            uniqueTypes.add(vessel.vesselType);
          }
          if (vessel.status) {
            uniqueStatuses.add(vessel.status);
          }
        });
        
        // Sort alphabetically
        this.vesselTypes = Array.from(uniqueTypes).sort();
        this.vesselStatuses = Array.from(uniqueStatuses).sort();
        
        console.log('Extracted vessel types from database:', this.vesselTypes);
        console.log('Extracted vessel statuses from database:', this.vesselStatuses);
        
        // If no data, provide defaults
        if (this.vesselTypes.length === 0) {
          console.warn('No vessel types found in database, using defaults');
          this.vesselTypes = ['CONTAINER_SHIP', 'BULK_CARRIER', 'TANKER', 'GENERAL_CARGO', 'RO_RO', 'LNG_CARRIER', 'CRUISE_SHIP'];
        }
        if (this.vesselStatuses.length === 0) {
          console.warn('No vessel statuses found in database, using defaults');
          this.vesselStatuses = ['SCHEDULED', 'UNDERWAY', 'ANCHORED', 'BERTHED', 'DEPARTED'];
        }
      },
      error: (error) => {
        console.error('Error loading filter options:', error);
        console.warn('Using fallback defaults due to error');
        // Fallback to defaults
        this.vesselTypes = ['CONTAINER_SHIP', 'BULK_CARRIER', 'TANKER', 'GENERAL_CARGO', 'RO_RO', 'LNG_CARRIER', 'CRUISE_SHIP'];
        this.vesselStatuses = ['SCHEDULED', 'UNDERWAY', 'ANCHORED', 'BERTHED', 'DEPARTED'];
      }
    });
  }
  
  getTypeLabel(type: string): string {
    switch (type) {
      case 'CONTAINER_SHIP': return 'Container Ship';
      case 'BULK_CARRIER': return 'Bulk Carrier';
      case 'TANKER': return 'Tanker';
      case 'GENERAL_CARGO': return 'General Cargo';
      case 'RO_RO': return 'RO-RO';
      case 'LNG_CARRIER': return 'LNG Carrier';
      case 'CRUISE_SHIP': return 'Cruise Ship';
      case 'CARGO_SHIP': return 'Cargo Ship';
      case 'SUPPLY_VESSEL': return 'Supply Vessel';
      case 'OTHER': return 'Other';
      default: return type?.replace('_', ' ');
    }
  }
  
  getStatusLabel(status: string): string {
    switch (status) {
      case 'SCHEDULED': return 'Scheduled';
      case 'UNDERWAY': return 'Underway';
      case 'ANCHORED': return 'Anchored';
      case 'BERTHED': return 'Berthed';
      case 'DEPARTED': return 'Departed';
      default: return status?.replace('_', ' ');
    }
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

    this.vesselService.createVessel(vesselData).subscribe({
      next: (response) => {
        console.log('Vessel created successfully:', response);
        this.router.navigate(['/vessels']);
      },
      error: (error) => {
        console.error('Error creating vessel:', error);
        this.error = error.error?.message || 'Failed to create vessel. Please try again.';
        this.loading = false;
      }
    });
  }

  onCancel(): void {
    this.router.navigate(['/vessels']);
  }
}
