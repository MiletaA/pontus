import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, ActivatedRoute, RouterModule } from '@angular/router';
import { DockService, Dock } from '../../../core/services/dock.service';

@Component({
  selector: 'app-dock-edit',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './dock-edit.component.html',
  styleUrls: ['./dock-edit.component.scss']
})
export class DockEditComponent implements OnInit {
  dock: Dock = {
    id: 0,
    name: '',
    maxLength: 0,
    isOccupied: false,
    assignedVesselId: undefined,
    scheduledFrom: '',
    scheduledTo: '',
    handlesDangerous: false,
    description: '',
    createdAt: '',
    updatedAt: ''
  };
  
  loading = false;
  error = '';
  dockId: number = 0;

  constructor(
    private dockService: DockService,
    private router: Router,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.dockId = Number(this.route.snapshot.paramMap.get('id'));
    if (this.dockId) {
      this.loadDock();
    } else {
      this.error = 'Invalid dock ID';
    }
  }

  loadDock(): void {
    this.loading = true;
    this.dockService.getDockById(this.dockId).subscribe({
      next: (dock) => {
        this.dock = {
          ...dock,
          scheduledFrom: this.formatDateForInput(dock.scheduledFrom || null),
          scheduledTo: this.formatDateForInput(dock.scheduledTo || null)
        };
        this.loading = false;
      },
      error: (error) => {
        console.error('Error loading dock:', error);
        this.error = 'Failed to load dock data';
        this.loading = false;
      }
    });
  }

  formatDateForInput(dateString: string | null): string {
    if (!dateString) return '';
    const date = new Date(dateString);
    return date.toISOString().slice(0, 16); // Format for datetime-local input
  }

  onSubmit(): void {
    if (this.loading) return;
    
    this.loading = true;
    this.error = '';

    // Convert form data to proper format
    const dockData = {
      ...this.dock,
      maxLength: parseFloat(this.dock.maxLength.toString()) || 0,
      assignedVesselId: this.dock.assignedVesselId || undefined,
      scheduledFrom: this.dock.scheduledFrom || undefined,
      scheduledTo: this.dock.scheduledTo || undefined,
      description: this.dock.description || undefined
    };

    this.dockService.updateDock(this.dockId, dockData).subscribe({
      next: (response) => {
        console.log('Dock updated successfully:', response);
        this.router.navigate(['/docks']);
      },
      error: (error) => {
        console.error('Error updating dock:', error);
        this.error = error.error?.message || 'Failed to update dock. Please try again.';
        this.loading = false;
      }
    });
  }

  onCancel(): void {
    this.router.navigate(['/docks']);
  }
}
