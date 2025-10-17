import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { CargoService } from '../../../../services/cargo.service';
import { AuthService } from '../../../../services/auth.service';
import { Cargo, CargoFilters } from '../../../../models/cargo.model';

@Component({
  selector: 'app-cargo-list',
  templateUrl: './cargo-list.component.html',
  styleUrls: ['./cargo-list.component.scss']
})
export class CargoListComponent implements OnInit {
  cargo: Cargo[] = [];
  filteredCargo: Cargo[] = [];
  loading = true;
  error = '';
  filters: CargoFilters = {};

  constructor(
    private cargoService: CargoService,
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.loadCargo();
  }

  loadCargo(): void {
    this.loading = true;
    this.error = '';

    this.cargoService.getAllCargo().subscribe({
      next: (response) => {
        this.cargo = response.data || [];
        this.filteredCargo = [...this.cargo];
        this.loading = false;
      },
      error: (error) => {
        this.error = 'Failed to load cargo';
        this.loading = false;
        console.error('Error loading cargo:', error);
      }
    });
  }

  onFiltersChanged(filters: CargoFilters): void {
    this.filters = filters;
    this.applyFilters();
  }

  private applyFilters(): void {
    this.filteredCargo = this.cargo.filter(cargo => {
      if (this.filters.vesselId !== undefined && cargo.vesselId !== this.filters.vesselId) {
        return false;
      }
      if (this.filters.customsStatus && cargo.customsStatus !== this.filters.customsStatus) {
        return false;
      }
      if (this.filters.isDangerous !== undefined && cargo.isDangerous !== this.filters.isDangerous) {
        return false;
      }
      if (this.filters.minWeight !== undefined && cargo.weight < this.filters.minWeight) {
        return false;
      }
      if (this.filters.maxWeight !== undefined && cargo.weight > this.filters.maxWeight) {
        return false;
      }
      if (this.filters.search) {
        const searchTerm = this.filters.search.toLowerCase();
        return cargo.description.toLowerCase().includes(searchTerm) ||
               cargo.origin?.toLowerCase().includes(searchTerm) ||
               cargo.destination?.toLowerCase().includes(searchTerm);
      }
      return true;
    });
  }

  onCargoDeleted(cargoId: number): void {
    this.cargo = this.cargo.filter(cargo => cargo.id !== cargoId);
    this.applyFilters();
  }

  canCreate(): boolean {
    return this.authService.canCreate();
  }

  createNewCargo(): void {
    this.router.navigate(['/cargo/new']);
  }

  refreshData(): void {
    this.loadCargo();
  }

  getClearedCargoCount(): number {
    return this.cargo.filter(c => c.customsStatus === 'CLEARED').length;
  }

  getPendingCargoCount(): number {
    return this.cargo.filter(c => c.customsStatus === 'PENDING').length;
  }

  getDangerousCargoCount(): number {
    return this.cargo.filter(c => c.isDangerous).length;
  }
}
