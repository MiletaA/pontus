import { Component, Input, Output, EventEmitter } from '@angular/core';
import { Router } from '@angular/router';
import { CargoService } from '../../../../services/cargo.service';
import { AuthService } from '../../../../services/auth.service';
import { Cargo } from '../../../../models/cargo.model';

@Component({
  selector: 'app-cargo-table',
  templateUrl: './cargo-table.component.html',
  styleUrls: ['./cargo-table.component.scss']
})
export class CargoTableComponent {
  @Input() cargo: Cargo[] = [];
  @Input() loading = false;
  @Output() cargoDeleted = new EventEmitter<number>();

  constructor(
    private cargoService: CargoService,
    private authService: AuthService,
    private router: Router
  ) {}

  editCargo(cargo: Cargo): void {
    this.router.navigate(['/cargo/edit', cargo.id]);
  }

  deleteCargo(cargo: Cargo): void {
    if (confirm(`Are you sure you want to delete cargo "${cargo.description}"?`)) {
      this.cargoService.deleteCargo(cargo.id).subscribe({
        next: () => {
          this.cargoDeleted.emit(cargo.id);
        },
        error: (error) => {
          console.error('Error deleting cargo:', error);
          alert('Failed to delete cargo. Please try again.');
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

  trackByCargoId(index: number, cargo: Cargo): number {
    return cargo.id;
  }
}
