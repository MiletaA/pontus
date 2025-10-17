import { Component, OnInit, OnDestroy } from '@angular/core';
import { Router } from '@angular/router';
import { Subject, takeUntil, combineLatest } from 'rxjs';
import { VesselService } from '../../../../services/vessel.service';
import { Vessel, VesselStatus, VesselType } from '../../../../models/vessel.model';
import { VesselFilters } from '../vessel-filters/vessel-filters.component';

@Component({
  selector: 'app-vessel-list',
  templateUrl: './vessel-list.component.html',
  styleUrls: ['./vessel-list.component.scss']
})
export class VesselListComponent implements OnInit, OnDestroy {
  vessels: Vessel[] = [];
  filteredVessels: Vessel[] = [];
  loading = false;
  error: string | null = null;
  
  // Filters
  filters: VesselFilters = {};
  vesselStatuses = Object.values(VesselStatus);
  vesselTypes = Object.values(VesselType);
  
  // Statistics
  statistics: any = {};
  
  // View mode
  viewMode: 'table' | 'cards' = 'table';
  
  private destroy$ = new Subject<void>();

  constructor(
    private vesselService: VesselService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.loadVessels();
    this.subscribeToVessels();
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  private subscribeToVessels(): void {
    combineLatest([
      this.vesselService.vessels$,
      this.vesselService.loading$
    ]).pipe(
      takeUntil(this.destroy$)
    ).subscribe(([vessels, loading]) => {
      this.vessels = vessels;
      this.loading = loading;
      this.applyFilters();
      this.updateStatistics();
    });
  }

  loadVessels(): void {
    this.error = null;
    this.vesselService.getAllVessels().subscribe({
      error: (error) => {
        this.error = error.message;
        this.loading = false;
      }
    });
  }

  onFiltersChanged(filters: VesselFilters): void {
    this.filters = filters;
    this.applyFilters();
  }

  private applyFilters(): void {
    this.filteredVessels = this.filterVessels(this.vessels, this.filters);
  }

  private filterVessels(vessels: Vessel[], filters: VesselFilters): Vessel[] {
    return vessels.filter(vessel => {
      if (filters.status && vessel.status !== filters.status) {
        return false;
      }
      if (filters.vesselType && vessel.vesselType !== filters.vesselType) {
        return false;
      }
      if (filters.flagCountry && vessel.flagCountry !== filters.flagCountry) {
        return false;
      }
      if (filters.searchTerm) {
        const searchTerm = filters.searchTerm.toLowerCase();
        return vessel.name.toLowerCase().includes(searchTerm) ||
               vessel.imoNumber.toLowerCase().includes(searchTerm);
      }
      return true;
    });
  }

  getCurrentTime(): Date {
    return new Date();
  }

  private updateStatistics(): void {
    // Calculate basic statistics
    this.statistics = {
      total: this.vessels.length,
      berthed: this.vessels.filter(v => v.status === VesselStatus.BERTHED).length,
      scheduled: this.vessels.filter(v => v.status === VesselStatus.SCHEDULED).length,
      departed: this.vessels.filter(v => v.status === VesselStatus.DEPARTED).length,
      underway: this.vessels.filter(v => v.status === VesselStatus.UNDERWAY).length
    };
  }

  onCreateVessel(): void {
    this.router.navigate(['/vessels/create']);
  }

  onEditVessel(vessel: Vessel): void {
    this.router.navigate(['/vessels/edit', vessel.id]);
  }

  onViewVessel(vessel: Vessel): void {
    this.router.navigate(['/vessels/detail', vessel.id]);
  }

  onDeleteVessel(vessel: Vessel): void {
    if (confirm(`Are you sure you want to delete vessel "${vessel.name}"?`)) {
      this.vesselService.deleteVessel(vessel.id).subscribe({
        next: () => {
          // Success handled by service state management
        },
        error: (error) => {
          this.error = error.message;
        }
      });
    }
  }

  onRefresh(): void {
    this.loadVessels();
  }

  toggleViewMode(): void {
    this.viewMode = this.viewMode === 'table' ? 'cards' : 'table';
  }

  exportToCSV(): void {
    const csvData = this.convertToCSV(this.filteredVessels);
    const blob = new Blob([csvData], { type: 'text/csv' });
    const url = window.URL.createObjectURL(blob);
    const link = document.createElement('a');
    link.href = url;
    link.download = `vessels-${new Date().toISOString().split('T')[0]}.csv`;
    link.click();
    window.URL.revokeObjectURL(url);
  }

  private convertToCSV(vessels: Vessel[]): string {
    const headers = [
      'ID', 'Name', 'IMO Number', 'Type', 'Length', 'Flag Country', 
      'Status', 'Scheduled Arrival', 'Scheduled Departure', 
      'Actual Arrival', 'Actual Departure'
    ];
    
    const csvRows = [
      headers.join(','),
      ...vessels.map(vessel => [
        vessel.id,
        `"${vessel.name}"`,
        vessel.imoNumber,
        vessel.vesselType,
        vessel.length,
        `"${vessel.flagCountry}"`,
        vessel.status,
        vessel.scheduledArrival || '',
        vessel.scheduledDeparture || '',
        vessel.actualArrival || '',
        vessel.actualDeparture || ''
      ].join(','))
    ];
    
    return csvRows.join('\n');
  }
}
