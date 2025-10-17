import { Component, Input, Output, EventEmitter } from '@angular/core';
import { Router } from '@angular/router';
import { DockService } from '../../../../services/dock.service';
import { AuthService } from '../../../../services/auth.service';
import { Dock } from '../../../../models/dock.model';

@Component({
  selector: 'app-dock-table',
  templateUrl: './dock-table.component.html',
  styleUrls: ['./dock-table.component.scss']
})
export class DockTableComponent {
  @Input() docks: Dock[] = [];
  @Input() loading = false;
  @Output() dockDeleted = new EventEmitter<number>();

  constructor(
    private dockService: DockService,
    private authService: AuthService,
    private router: Router
  ) {}

  editDock(dock: Dock): void {
    this.router.navigate(['/docks/edit', dock.id]);
  }

  deleteDock(dock: Dock): void {
    if (confirm(`Are you sure you want to delete dock "${dock.name}"?`)) {
      this.dockService.deleteDock(dock.id).subscribe({
        next: () => {
          this.dockDeleted.emit(dock.id);
        },
        error: (error) => {
          console.error('Error deleting dock:', error);
          alert('Failed to delete dock. Please try again.');
        }
      });
    }
  }

  assignVessel(dock: Dock): void {
    // TODO: Implement vessel assignment modal/dialog
    console.log('Assign vessel to dock:', dock);
  }

  unassignVessel(dock: Dock): void {
    if (confirm(`Are you sure you want to unassign the vessel from dock "${dock.name}"?`)) {
      this.dockService.unassignVesselFromDock(dock.id).subscribe({
        next: () => {
          // Refresh the dock data
          window.location.reload();
        },
        error: (error) => {
          console.error('Error unassigning vessel:', error);
          alert('Failed to unassign vessel. Please try again.');
        }
      });
    }
  }

  canUpdate(): boolean {
    return this.authService.canUpdate();
  }

  canDelete(): boolean {
    return this.authService.canDelete();
  }

  trackByDockId(index: number, dock: Dock): number {
    return dock.id;
  }
}
