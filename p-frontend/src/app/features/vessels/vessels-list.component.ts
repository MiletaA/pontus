import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterModule, NavigationEnd } from '@angular/router';
import { FormBuilder, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { VesselService, Vessel, PageResponse } from '../../core/services/vessel.service';
import { AuthService } from '../../core/services/auth.service';
import { DataTableComponent, TableColumn, PageInfo } from '../../shared/components/data-table/data-table.component';
import { Subscription } from 'rxjs';
import { filter } from 'rxjs/operators';

@Component({
  selector: 'app-vessels-list',
  standalone: true,
  imports: [CommonModule, RouterModule, ReactiveFormsModule, DataTableComponent],
  templateUrl: './vessels-list.component.html',
  styleUrls: ['./vessels-list.component.scss']
})
export class VesselsListComponent implements OnInit, OnDestroy {
  vessels: Vessel[] = [];
  loading = false;
  error = '';
  
  // Table configuration
  columns: TableColumn[] = [
    { key: 'name', label: 'Vessel Name', sortable: true },
    { key: 'imoNumber', label: 'IMO Number', sortable: true },
    { key: 'vesselType', label: 'Type', sortable: true },
    { key: 'length', label: 'Length (m)', sortable: true },
    { key: 'flagCountry', label: 'Flag', sortable: true },
    { key: 'status', label: 'Status', sortable: true }
  ];
  
  pageInfo: PageInfo = {
    page: 0,
    size: 10,
    totalElements: 0,
    totalPages: 0
  };
  
  // Filter form
  filterForm: FormGroup;
  vesselStatuses: string[] = [];
  vesselTypes: string[] = [];
  
  // User permissions
  currentUser: any = null;
  canCreate = false;
  canEdit = false;
  canDelete = false;
  
  // Router subscription for refresh
  private routerSubscription: Subscription = new Subscription();


  constructor(
    private vesselService: VesselService,
    private authService: AuthService,
    private fb: FormBuilder,
    private router: Router
  ) {
    this.filterForm = this.fb.group({
      search: [''],
      status: [''],
      vesselType: ['']
    });
  }

  ngOnInit(): void {
    this.currentUser = this.authService.getUserData();
    this.checkPermissions();
    this.loadVessels();
    this.loadFilterOptions();
    
    // Setup filter listeners
    this.filterForm.valueChanges.subscribe(() => {
      this.pageInfo.page = 0;
      this.loadVessels();
    });
    
    // Listen for navigation events to refresh data when returning from create/edit pages
    this.routerSubscription = this.router.events
      .pipe(filter(event => event instanceof NavigationEnd))
      .subscribe((event: NavigationEnd) => {
        if (event.url === '/vessels') {
          // Clear filters and reset to first page to ensure new items are visible
          this.filterForm.patchValue({
            search: '',
            status: '',
            vesselType: ''
          });
          this.pageInfo.page = 0;
          this.loadVessels();
        }
      });
  }

  ngOnDestroy(): void {
    this.routerSubscription.unsubscribe();
  }

  checkPermissions(): void {
    const userRole = this.currentUser?.role;
    this.canCreate = ['ADMIN', 'MANAGER', 'HARBOR_MASTER'].includes(userRole);
    this.canEdit = ['ADMIN', 'MANAGER', 'HARBOR_MASTER'].includes(userRole);
    this.canDelete = userRole === 'ADMIN';
  }

  loadVessels(): void {
    this.loading = true;
    this.error = '';
    
    // Only use the working getAllVessels endpoint
    this.vesselService.getAllVessels(this.pageInfo.page, this.pageInfo.size).subscribe({
      next: (response: PageResponse<Vessel>) => {
        let vessels = response.content;
        
        // Apply client-side filtering
        const filters = this.filterForm.value;
        
        if (filters.search) {
          const searchTerm = filters.search.toLowerCase();
          vessels = vessels.filter(vessel => 
            vessel.name.toLowerCase().includes(searchTerm) ||
            vessel.imoNumber.toLowerCase().includes(searchTerm) ||
            vessel.flagCountry.toLowerCase().includes(searchTerm) ||
            vessel.vesselType.toLowerCase().includes(searchTerm) ||
            vessel.status.toLowerCase().includes(searchTerm) ||
            vessel.length.toString().includes(searchTerm)
          );
        }
        
        if (filters.status) {
          vessels = vessels.filter(vessel => 
            vessel.status === filters.status
          );
        }
        
        if (filters.vesselType) {
          vessels = vessels.filter(vessel => 
            vessel.vesselType === filters.vesselType
          );
        }
        
        this.vessels = vessels;
        this.pageInfo.totalElements = response.totalElements;
        this.pageInfo.totalPages = response.totalPages;
        this.loading = false;
      },
      error: (error) => {
        this.error = 'Failed to load vessels. Please try again.';
        this.loading = false;
        console.error('Error loading vessels:', error);
        // Ensure vessels is always an array
        this.vessels = [];
      }
    });
  }

  loadFilterOptions(): void {
    // Fetch vessels to extract unique statuses and types
    this.vesselService.getAllVessels(0, 100).subscribe({
      next: (response: PageResponse<Vessel>) => {
        const uniqueStatuses = new Set<string>();
        const uniqueTypes = new Set<string>();
        
        response.content.forEach(vessel => {
          if (vessel.status) {
            uniqueStatuses.add(vessel.status);
          }
          if (vessel.vesselType) {
            uniqueTypes.add(vessel.vesselType);
          }
        });
        
        // Sort alphabetically
        this.vesselStatuses = Array.from(uniqueStatuses).sort();
        this.vesselTypes = Array.from(uniqueTypes).sort();
        
        console.log('Loaded vessel statuses from database:', this.vesselStatuses);
        console.log('Loaded vessel types from database:', this.vesselTypes);
      },
      error: (error) => {
        console.error('Error loading filter options:', error);
        // Fallback to common values if loading fails
        this.vesselStatuses = ['SCHEDULED', 'UNDERWAY', 'ANCHORED', 'BERTHED', 'DEPARTED'];
        this.vesselTypes = ['CONTAINER_SHIP', 'BULK_CARRIER', 'TANKER', 'GENERAL_CARGO', 'RO_RO', 'LNG_CARRIER', 'CRUISE_SHIP'];
      }
    });
  }

  onPageChange(page: number): void {
    if (page >= 0 && page < this.pageInfo.totalPages) {
      this.pageInfo.page = page;
      this.loadVessels();
    }
  }

  onCreateVessel(): void {
    this.router.navigate(['/vessels/create']);
  }

  onEditVessel(vessel: Vessel): void {
    this.router.navigate(['/vessels/edit', vessel.id]);
  }

  onDeleteVessel(vessel: Vessel): void {
    this.deleteVessel(vessel.id, vessel.name);
  }

  deleteVessel(id: number, name: string): void {
    if (confirm(`Are you sure you want to delete vessel "${name}"?`)) {
      this.vesselService.deleteVessel(id).subscribe({
        next: () => {
          this.loadVessels();
        },
        error: (error) => {
          alert('Failed to delete vessel. Please try again.');
          console.error('Error deleting vessel:', error);
        }
      });
    }
  }

  getStatusClass(status: string): string {
    switch (status?.toUpperCase()) {
      case 'BERTHED': return 'success';
      case 'UNDERWAY': return 'warning';
      case 'SCHEDULED': return 'info';
      case 'ANCHORED': return 'primary';
      case 'DEPARTED': return 'secondary';
      default: return 'secondary';
    }
  }

  getStatusLabel(status: string): string {
    switch (status) {
      case 'SCHEDULED': return 'Scheduled';
      case 'UNDERWAY': return 'Underway';
      case 'ANCHORED': return 'Anchored';
      case 'BERTHED': return 'Berthed';
      case 'DEPARTED': return 'Departed';
      default: return status.replace('_', ' ');
    }
  }

  getTypeLabel(type: string): string {
    switch (type) {
      case 'CONTAINER_SHIP': return 'Container Ship';
      case 'BULK_CARRIER': return 'Bulk Carrier';
      case 'TANKER': return 'Tanker';
      case 'GENERAL_CARGO': return 'General Cargo';
      case 'RO_RO': return 'RO-RO';
      case 'LNG_CARRIER': return 'LNG Carrier';
      case 'CRUISE_SHIP': return 'Cruise Ship';
      case 'CARGO_SHIP': return 'Cargo Ship';
      case 'SUPPLY_VESSEL': return 'Supply Vessel';
      case 'OTHER': return 'Other';
      default: return type.replace('_', ' ');
    }
  }

  getVesselTypeIcon(type: string): string {
    switch (type?.toUpperCase()) {
      case 'CONTAINER_SHIP':
        return 'bi-box-seam';
      case 'BULK_CARRIER':
        return 'bi-minecart-loaded';
      case 'TANKER':
        return 'bi-droplet';
      case 'CARGO':
        return 'bi-box';
      case 'PASSENGER':
        return 'bi-people';
      case 'NAVAL':
        return 'bi-shield';
      case 'FISHING':
        return 'bi-fish';
      default:
        return 'bi-ship';
    }
  }
}
