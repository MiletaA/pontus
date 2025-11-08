import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { CargoService } from '../../../core/services/cargo.service';

@Component({
  selector: 'app-cargo-create',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './cargo-create.component.html',
  styleUrls: ['./cargo-create.component.scss']
})
export class CargoCreateComponent implements OnInit {
  cargo = {
    vesselId: 0,
    description: '',
    weightTons: 0,
    isDangerous: false,
    customsStatus: '',
    cargoType: '',
    origin: '',
    destination: ''
  };
  
  loading = false;
  error = '';
  
  // Dynamic options
  cargoTypes: string[] = [];
  customsStatuses: string[] = [];

  constructor(
    private cargoService: CargoService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.loadFilterOptions();
  }
  
  loadFilterOptions(): void {
    // Fetch cargo to extract unique types and customs statuses
    this.cargoService.getAllCargo(0, 100).subscribe({
      next: (response) => {
        const uniqueTypes = new Set<string>();
        const uniqueStatuses = new Set<string>();
        
        response.content.forEach(cargo => {
          if (cargo.cargoType) {
            uniqueTypes.add(cargo.cargoType);
          }
          if (cargo.customsStatus) {
            uniqueStatuses.add(cargo.customsStatus);
          }
        });
        
        // Sort alphabetically
        this.cargoTypes = Array.from(uniqueTypes).sort();
        this.customsStatuses = Array.from(uniqueStatuses).sort();
        
        // If no data, provide defaults
        if (this.cargoTypes.length === 0) {
          this.cargoTypes = ['CONTAINER', 'BULK', 'LIQUID', 'REFRIGERATED', 'HAZMAT', 'GENERAL'];
        }
        if (this.customsStatuses.length === 0) {
          this.customsStatuses = ['PENDING', 'CLEARED', 'INSPECTION', 'HOLD', 'RELEASED'];
        }
      },
      error: (error) => {
        console.error('Error loading filter options:', error);
        // Fallback to defaults
        this.cargoTypes = ['CONTAINER', 'BULK', 'LIQUID', 'REFRIGERATED', 'HAZMAT', 'GENERAL'];
        this.customsStatuses = ['PENDING', 'CLEARED', 'INSPECTION', 'HOLD', 'RELEASED'];
      }
    });
  }
  
  getTypeLabel(type: string): string {
    switch (type) {
      case 'CONTAINER': return 'Container';
      case 'BULK': return 'Bulk';
      case 'LIQUID': return 'Liquid';
      case 'REFRIGERATED': return 'Refrigerated';
      case 'HAZMAT': return 'Hazmat';
      case 'GENERAL': return 'General';
      default: return type?.replace('_', ' ');
    }
  }
  
  getStatusLabel(status: string): string {
    switch (status) {
      case 'PENDING': return 'Pending';
      case 'CLEARED': return 'Cleared';
      case 'INSPECTION': return 'Inspection';
      case 'HOLD': return 'Hold';
      case 'RELEASED': return 'Released';
      default: return status?.replace('_', ' ');
    }
  }

  onSubmit(): void {
    if (this.loading) return;
    
    this.loading = true;
    this.error = '';

    // Convert form data to proper format
    const cargoData = {
      ...this.cargo,
      weightTons: parseFloat(this.cargo.weightTons.toString()) || 0,
      cargoType: this.cargo.cargoType || undefined,
      origin: this.cargo.origin || undefined,
      destination: this.cargo.destination || undefined
    };

    this.cargoService.createCargo(cargoData).subscribe({
      next: (response) => {
        console.log('Cargo created successfully:', response);
        this.router.navigate(['/cargo']);
      },
      error: (error) => {
        console.error('Error creating cargo:', error);
        this.error = error.error?.message || 'Failed to create cargo. Please try again.';
        this.loading = false;
      }
    });
  }

  onCancel(): void {
    this.router.navigate(['/cargo']);
  }
}
