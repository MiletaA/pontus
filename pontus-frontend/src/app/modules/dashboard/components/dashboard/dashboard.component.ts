import { Component, OnInit } from '@angular/core';
import { forkJoin } from 'rxjs';
import { VesselService } from '../../../../services/vessel.service';
import { DockService } from '../../../../services/dock.service';
import { CargoService } from '../../../../services/cargo.service';
import { CrewService } from '../../../../services/crew.service';
import { DeliveryService } from '../../../../services/delivery.service';
import { AuthService } from '../../../../services/auth.service';

interface DashboardStats {
  vessels: {
    total: number;
    berthed: number;
    underway: number;
    scheduled: number;
  };
  docks: {
    total: number;
    occupied: number;
    available: number;
  };
  cargo: {
    total: number;
    pending: number;
    cleared: number;
    dangerous: number;
  };
  crew: {
    total: number;
    assigned: number;
    unassigned: number;
  };
  deliveries: {
    total: number;
    scheduled: number;
    inTransit: number;
    delivered: number;
  };
}

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent implements OnInit {
  loading = true;
  error = '';
  stats: DashboardStats = {
    vessels: { total: 0, berthed: 0, underway: 0, scheduled: 0 },
    docks: { total: 0, occupied: 0, available: 0 },
    cargo: { total: 0, pending: 0, cleared: 0, dangerous: 0 },
    crew: { total: 0, assigned: 0, unassigned: 0 },
    deliveries: { total: 0, scheduled: 0, inTransit: 0, delivered: 0 }
  };

  currentUser$ = this.authService.currentUser$;

  constructor(
    private vesselService: VesselService,
    private dockService: DockService,
    private cargoService: CargoService,
    private crewService: CrewService,
    private deliveryService: DeliveryService,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    this.loadDashboardData();
  }

  loadDashboardData(): void {
    this.loading = true;
    this.error = '';

    // Load data from all services
    forkJoin({
      vessels: this.vesselService.getAllVessels(),
      docks: this.dockService.getAllDocks(),
      cargo: this.cargoService.getAllCargo(),
      crew: this.crewService.getAllCrewMembers(),
      deliveries: this.deliveryService.getAllDeliveries()
    }).subscribe({
      next: (data) => {
        this.processStats(data);
        this.loading = false;
      },
      error: (error) => {
        this.error = 'Failed to load dashboard data';
        this.loading = false;
        console.error('Dashboard error:', error);
      }
    });
  }

  private processStats(data: any): void {
    // Process vessel stats
    if (data.vessels?.data) {
      const vessels = data.vessels.data;
      this.stats.vessels.total = vessels.length;
      this.stats.vessels.berthed = vessels.filter((v: any) => v.status === 'BERTHED').length;
      this.stats.vessels.underway = vessels.filter((v: any) => v.status === 'UNDERWAY').length;
      this.stats.vessels.scheduled = vessels.filter((v: any) => v.status === 'SCHEDULED').length;
    }

    // Process dock stats
    if (data.docks?.data) {
      const docks = data.docks.data;
      this.stats.docks.total = docks.length;
      this.stats.docks.occupied = docks.filter((d: any) => d.isOccupied).length;
      this.stats.docks.available = docks.filter((d: any) => !d.isOccupied).length;
    }

    // Process cargo stats
    if (data.cargo?.data) {
      const cargo = data.cargo.data;
      this.stats.cargo.total = cargo.length;
      this.stats.cargo.pending = cargo.filter((c: any) => c.customsStatus === 'PENDING').length;
      this.stats.cargo.cleared = cargo.filter((c: any) => c.customsStatus === 'CLEARED').length;
      this.stats.cargo.dangerous = cargo.filter((c: any) => c.isDangerous).length;
    }

    // Process crew stats
    if (data.crew?.data) {
      const crew = data.crew.data;
      this.stats.crew.total = crew.length;
      this.stats.crew.assigned = crew.filter((c: any) => c.vesselId).length;
      this.stats.crew.unassigned = crew.filter((c: any) => !c.vesselId).length;
    }

    // Process delivery stats
    if (data.deliveries?.data) {
      const deliveries = data.deliveries.data;
      this.stats.deliveries.total = deliveries.length;
      this.stats.deliveries.scheduled = deliveries.filter((d: any) => d.status === 'SCHEDULED').length;
      this.stats.deliveries.inTransit = deliveries.filter((d: any) => d.status === 'IN_TRANSIT').length;
      this.stats.deliveries.delivered = deliveries.filter((d: any) => d.status === 'DELIVERED').length;
    }
  }

  refreshData(): void {
    this.loadDashboardData();
  }
}
