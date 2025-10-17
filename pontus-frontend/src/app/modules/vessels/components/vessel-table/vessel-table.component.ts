import { Component, Input, Output, EventEmitter } from '@angular/core';
import { Vessel } from '../../../../models/vessel.model';

@Component({
  selector: 'app-vessel-table',
  templateUrl: './vessel-table.component.html',
  styleUrls: ['./vessel-table.component.scss']
})
export class VesselTableComponent {
  @Input() vessels: Vessel[] = [];
  @Output() editVessel = new EventEmitter<Vessel>();
  @Output() viewVessel = new EventEmitter<Vessel>();
  @Output() deleteVessel = new EventEmitter<Vessel>();

  // Sorting
  sortColumn: string = '';
  sortDirection: 'asc' | 'desc' = 'asc';

  onEdit(vessel: Vessel): void {
    this.editVessel.emit(vessel);
  }

  onView(vessel: Vessel): void {
    this.viewVessel.emit(vessel);
  }

  onDelete(vessel: Vessel): void {
    this.deleteVessel.emit(vessel);
  }

  sortBy(column: string): void {
    if (this.sortColumn === column) {
      this.sortDirection = this.sortDirection === 'asc' ? 'desc' : 'asc';
    } else {
      this.sortColumn = column;
      this.sortDirection = 'asc';
    }

    this.vessels.sort((a, b) => {
      let aValue: any = this.getColumnValue(a, column);
      let bValue: any = this.getColumnValue(b, column);

      // Handle null/undefined values
      if (aValue == null && bValue == null) return 0;
      if (aValue == null) return this.sortDirection === 'asc' ? 1 : -1;
      if (bValue == null) return this.sortDirection === 'asc' ? -1 : 1;

      // Convert to comparable values
      if (typeof aValue === 'string') {
        aValue = aValue.toLowerCase();
        bValue = bValue.toLowerCase();
      }

      let comparison = 0;
      if (aValue > bValue) {
        comparison = 1;
      } else if (aValue < bValue) {
        comparison = -1;
      }

      return this.sortDirection === 'asc' ? comparison : -comparison;
    });
  }

  private getColumnValue(vessel: Vessel, column: string): any {
    switch (column) {
      case 'id': return vessel.id;
      case 'name': return vessel.name;
      case 'imoNumber': return vessel.imoNumber;
      case 'vesselType': return vessel.vesselType;
      case 'length': return vessel.length;
      case 'flagCountry': return vessel.flagCountry;
      case 'status': return vessel.status;
      case 'scheduledArrival': return vessel.scheduledArrival;
      case 'scheduledDeparture': return vessel.scheduledDeparture;
      case 'actualArrival': return vessel.actualArrival;
      case 'actualDeparture': return vessel.actualDeparture;
      default: return '';
    }
  }

  getSortIcon(column: string): string {
    if (this.sortColumn !== column) {
      return 'fas fa-sort text-muted';
    }
    return this.sortDirection === 'asc' ? 'fas fa-sort-up text-primary' : 'fas fa-sort-down text-primary';
  }

  formatDate(dateString: string | undefined): string {
    if (!dateString) return '-';
    return new Date(dateString).toLocaleString();
  }

  getStatusClass(status: string): string {
    return `status-${status}`;
  }

  trackByVesselId(index: number, vessel: Vessel): number {
    return vessel.id;
  }

  getFlagCode(country: string): string {
    // Map country names to ISO 3166-1 alpha-2 codes for flag icons
    const countryCodeMap: { [key: string]: string } = {
      'Panama': 'pa',
      'Liberia': 'lr',
      'Marshall Islands': 'mh',
      'Hong Kong': 'hk',
      'Singapore': 'sg',
      'Bahamas': 'bs',
      'Malta': 'mt',
      'Cyprus': 'cy',
      'China': 'cn',
      'Greece': 'gr',
      'Japan': 'jp',
      'Norway': 'no',
      'United Kingdom': 'gb',
      'Germany': 'de',
      'Italy': 'it',
      'Netherlands': 'nl',
      'Denmark': 'dk',
      'United States': 'us',
      'South Korea': 'kr',
      'India': 'in',
      'Turkey': 'tr',
      'Russia': 'ru'
    };
    
    return countryCodeMap[country] || country.toLowerCase().replace(/\s+/g, '-');
  }
}
