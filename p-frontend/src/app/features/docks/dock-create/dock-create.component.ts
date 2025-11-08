import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { DockService } from '../../../core/services/dock.service';

@Component({
  selector: 'app-dock-create',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './dock-create.component.html',
  styleUrls: ['./dock-create.component.scss']
})
export class DockCreateComponent implements OnInit {
  dock = {
    name: '',
    maxLength: 0,
    isOccupied: false,
    assignedVesselId: null as number | null,
    scheduledFrom: '',
    scheduledTo: '',
    handlesDangerous: false,
    description: ''
  };
  
  loading = false;
  error = '';

  constructor(
    private dockService: DockService,
    private router: Router
  ) {}

  ngOnInit(): void {
    // Initialize component
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

    this.dockService.createDock(dockData).subscribe({
      next: (response) => {
        console.log('Dock created successfully:', response);
        this.router.navigate(['/docks']);
      },
      error: (error) => {
        console.error('Error creating dock:', error);
        this.error = error.error?.message || 'Failed to create dock. Please try again.';
        this.loading = false;
      }
    });
  }

  onCancel(): void {
    this.router.navigate(['/docks']);
  }
}
