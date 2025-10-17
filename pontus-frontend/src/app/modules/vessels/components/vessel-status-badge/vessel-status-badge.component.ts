import { Component, Input } from '@angular/core';
import { VesselStatus } from '../../../../models/vessel.model';

@Component({
  selector: 'app-vessel-status-badge',
  templateUrl: './vessel-status-badge.component.html',
  styleUrls: ['./vessel-status-badge.component.scss']
})
export class VesselStatusBadgeComponent {
  @Input() status!: VesselStatus;
  @Input() size: 'sm' | 'md' | 'lg' = 'md';

  getStatusClass(): string {
    const baseClass = 'badge';
    const sizeClass = this.size === 'sm' ? 'badge-sm' : this.size === 'lg' ? 'badge-lg' : '';
    const statusClass = `status-${this.status}`;
    
    return `${baseClass} ${sizeClass} ${statusClass}`.trim();
  }

  getStatusIcon(): string {
    switch (this.status) {
      case VesselStatus.SCHEDULED:
        return 'fas fa-calendar-alt';
      case VesselStatus.UNDERWAY:
        return 'fas fa-ship';
      case VesselStatus.ANCHORED:
        return 'fas fa-anchor';
      case VesselStatus.BERTHED:
        return 'fas fa-warehouse';
      case VesselStatus.DEPARTED:
        return 'fas fa-arrow-right';
      case VesselStatus.DELAYED:
        return 'fas fa-clock';
      case VesselStatus.CANCELLED:
        return 'fas fa-times-circle';
      default:
        return 'fas fa-question-circle';
    }
  }

  getStatusText(): string {
    switch (this.status) {
      case VesselStatus.SCHEDULED:
        return 'Scheduled';
      case VesselStatus.UNDERWAY:
        return 'Underway';
      case VesselStatus.ANCHORED:
        return 'Anchored';
      case VesselStatus.BERTHED:
        return 'Berthed';
      case VesselStatus.DEPARTED:
        return 'Departed';
      case VesselStatus.DELAYED:
        return 'Delayed';
      case VesselStatus.CANCELLED:
        return 'Cancelled';
      default:
        return 'Unknown';
    }
  }
}
