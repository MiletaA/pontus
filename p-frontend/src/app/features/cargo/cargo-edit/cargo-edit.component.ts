import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, ActivatedRoute, RouterModule } from '@angular/router';
import { CargoService, Cargo } from '../../../core/services/cargo.service';

@Component({
  selector: 'app-cargo-edit',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './cargo-edit.component.html',
  styleUrls: ['./cargo-edit.component.scss']
})
export class CargoEditComponent implements OnInit {
  cargo: Cargo = {
    id: 0,
    vesselId: 0,
    description: '',
    weightTons: 0,
    isDangerous: false,
    customsStatus: '',
    cargoType: '',
    origin: '',
    destination: '',
    createdAt: '',
    updatedAt: ''
  };
  
  loading = false;
  error = '';
  cargoId: number = 0;

  constructor(
    private cargoService: CargoService,
    private router: Router,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.cargoId = Number(this.route.snapshot.paramMap.get('id'));
    if (this.cargoId) {
      this.loadCargo();
    } else {
      this.error = 'Invalid cargo ID';
    }
  }

  loadCargo(): void {
    this.loading = true;
    this.cargoService.getCargoById(this.cargoId).subscribe({
      next: (cargo) => {
        this.cargo = cargo;
        this.loading = false;
      },
      error: (error) => {
        console.error('Error loading cargo:', error);
        this.error = 'Failed to load cargo data';
        this.loading = false;
      }
    });
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

    this.cargoService.updateCargo(this.cargoId, cargoData).subscribe({
      next: (response) => {
        console.log('Cargo updated successfully:', response);
        this.router.navigate(['/cargo']);
      },
      error: (error) => {
        console.error('Error updating cargo:', error);
        this.error = error.error?.message || 'Failed to update cargo. Please try again.';
        this.loading = false;
      }
    });
  }

  onCancel(): void {
    this.router.navigate(['/cargo']);
  }
}
