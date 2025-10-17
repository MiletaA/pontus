import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { DockService } from '../../../../services/dock.service';
import { AuthService } from '../../../../services/auth.service';
import { Dock, DockFilters } from '../../../../models/dock.model';

@Component({
  selector: 'app-dock-list',
  templateUrl: './dock-list.component.html',
  styleUrls: ['./dock-list.component.scss']
})
export class DockListComponent implements OnInit {
  docks: Dock[] = [];
  filteredDocks: Dock[] = [];
  loading = true;
  error = '';
  filters: DockFilters = {};

  constructor(
    private dockService: DockService,
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.loadDocks();
  }

  loadDocks(): void {
    this.loading = true;
    this.error = '';

    this.dockService.getAllDocks().subscribe({
      next: (response) => {
        this.docks = response.data || [];
        this.filteredDocks = [...this.docks];
        this.loading = false;
      },
      error: (error) => {
        this.error = 'Failed to load docks';
        this.loading = false;
        console.error('Error loading docks:', error);
      }
    });
  }

  onFiltersChanged(filters: DockFilters): void {
    this.filters = filters;
    this.applyFilters();
  }

  private applyFilters(): void {
    this.filteredDocks = this.docks.filter(dock => {
      // Occupation status filter
      if (this.filters.isOccupied !== undefined && dock.isOccupied !== this.filters.isOccupied) {
        return false;
      }

      // Dangerous cargo handling filter
      if (this.filters.handlesDangerous !== undefined && dock.handlesDangerous !== this.filters.handlesDangerous) {
        return false;
      }

      // Length filters
      if (this.filters.minLength !== undefined && dock.maxLength < this.filters.minLength) {
        return false;
      }
      if (this.filters.maxLength !== undefined && dock.maxLength > this.filters.maxLength) {
        return false;
      }

      // Search filter
      if (this.filters.search) {
        const searchTerm = this.filters.search.toLowerCase();
        return dock.name.toLowerCase().includes(searchTerm) ||
               dock.description?.toLowerCase().includes(searchTerm);
      }

      return true;
    });
  }

  onDockDeleted(dockId: number): void {
    this.docks = this.docks.filter(dock => dock.id !== dockId);
    this.applyFilters();
  }

  canCreate(): boolean {
    return this.authService.canCreate();
  }

  canUpdate(): boolean {
    return this.authService.canUpdate();
  }

  canDelete(): boolean {
    return this.authService.canDelete();
  }

  createNewDock(): void {
    this.router.navigate(['/docks/new']);
  }

  refreshData(): void {
    this.loadDocks();
  }

  getAvailableDocksCount(): number {
    return this.docks.filter(d => !d.isOccupied).length;
  }

  getOccupiedDocksCount(): number {
    return this.docks.filter(d => d.isOccupied).length;
  }

  getDangerousCargoDocksCount(): number {
    return this.docks.filter(d => d.handlesDangerous).length;
  }
}
