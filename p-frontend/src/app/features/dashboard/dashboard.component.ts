import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { forkJoin, of } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { VesselService } from '../../core/services/vessel.service';
import { DockService } from '../../core/services/dock.service';
import { CargoService } from '../../core/services/cargo.service';
import { CrewService } from '../../core/services/crew.service';
import { DeliveryService } from '../../core/services/delivery.service';
import { AuthService } from '../../core/services/auth.service';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent implements OnInit {
  currentUser: any = null;
  loading = true;
  error = '';
  
  statistics = {
    vesselsInPort: 0,
    availableDocks: 0,
    pendingDeliveries: 0,
    activeCargo: 0,
    crewMembers: 0,
    expectedArrivals: 0
  };
  
  recentVessels: any[] = [];
  upcomingArrivals: any[] = [];
  pendingDeliveries: any[] = [];

  constructor(
    private authService: AuthService,
    private vesselService: VesselService,
    private dockService: DockService,
    private cargoService: CargoService,
    private crewService: CrewService,
    private deliveryService: DeliveryService
  ) {}

  ngOnInit(): void {
    this.currentUser = this.authService.getUserData();
    this.loadDashboardData();
  }

  loadDashboardData(): void {
    this.loading = true;
    this.error = '';

    // Load basic statistics - use simple counts for now
    this.vesselService.getAllVessels(0, 1).pipe(
      catchError(() => of({content: [], totalElements: 0}))
    ).subscribe(vessels => {
      this.statistics.vesselsInPort = vessels.totalElements;
      this.statistics.expectedArrivals = 0; // Will show "No data" message
    });

    this.dockService.getAllDocks(0, 1).pipe(
      catchError(() => of({content: [], totalElements: 0}))
    ).subscribe(docks => {
      this.statistics.availableDocks = docks.totalElements;
    });

    this.cargoService.getAllCargo(0, 1).pipe(
      catchError(() => of({content: [], totalElements: 0}))
    ).subscribe(cargo => {
      this.statistics.activeCargo = cargo.totalElements;
    });

    this.crewService.getAllCrew(0, 1).pipe(
      catchError(() => of({content: [], totalElements: 0}))
    ).subscribe(crew => {
      this.statistics.crewMembers = crew.totalElements;
    });

    this.deliveryService.getAllDeliveries(0, 1).pipe(
      catchError(() => of({content: [], totalElements: 0}))
    ).subscribe(deliveries => {
      this.statistics.pendingDeliveries = deliveries.totalElements;
      this.loading = false;
    });
  }

  getStatusClass(status: string): string {
    switch (status?.toUpperCase()) {
      case 'IN_PORT':
      case 'DOCKED':
      case 'DELIVERED':
        return 'success';
      case 'EXPECTED':
      case 'IN_TRANSIT':
      case 'PENDING':
        return 'warning';
      case 'DEPARTED':
        return 'info';
      case 'DELAYED':
      case 'CANCELLED':
        return 'danger';
      default:
        return 'secondary';
    }
  }

  formatDateTime(dateString: string): string {
    if (!dateString) return 'N/A';
    const date = new Date(dateString);
    return date.toLocaleDateString() + ' ' + date.toLocaleTimeString();
  }
}
