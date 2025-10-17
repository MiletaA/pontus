import { Component, Input } from '@angular/core';
import { Dock, DOCK_STATUS_INFO } from '../../../../models/dock.model';

@Component({
  selector: 'app-dock-status-badge',
  templateUrl: './dock-status-badge.component.html',
  styleUrls: ['./dock-status-badge.component.scss']
})
export class DockStatusBadgeComponent {
  @Input() dock!: Dock;

  get statusInfo() {
    return this.dock.isOccupied ? DOCK_STATUS_INFO['occupied'] : DOCK_STATUS_INFO['available'];
  }
}
