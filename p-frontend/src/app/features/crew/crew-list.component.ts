import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterModule, NavigationEnd } from '@angular/router';
import { FormBuilder, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { CrewService, CrewMember, PageResponse } from '../../core/services/crew.service';
import { DataTableComponent, TableColumn, PageInfo } from '../../shared/components/data-table/data-table.component';
import { Subscription } from 'rxjs';
import { filter } from 'rxjs/operators';

@Component({
  selector: 'app-crew-list',
  standalone: true,
  imports: [CommonModule, RouterModule, ReactiveFormsModule, DataTableComponent],
  templateUrl: './crew-list.component.html',
  styleUrls: ['./crew-list.component.scss']
})
export class CrewListComponent implements OnInit, OnDestroy {
  crewList: CrewMember[] = [];
  loading = false;
  error = '';
  
  // Table configuration
  columns: TableColumn[] = [
    { key: 'name', label: 'Name', sortable: true },
    { key: 'position', label: 'Position', sortable: true },
    { key: 'nationality', label: 'Nationality', sortable: true },
    { key: 'vesselId', label: 'Vessel ID', sortable: true },
    { key: 'passportNumber', label: 'Passport', sortable: false },
    { key: 'certificate', label: 'Certificate', sortable: true },
    { key: 'certificateExpiry', label: 'Cert. Expiry', sortable: true }
  ];
  
  pageInfo: PageInfo = {
    page: 0,
    size: 20,
    totalElements: 0,
    totalPages: 0
  };
  
  // Filter form
  filterForm: FormGroup;
  crewPositions: string[] = [];
  nationalities: string[] = [];
  
  // Router subscription for refresh
  private routerSubscription: Subscription = new Subscription();

  constructor(
    private crewService: CrewService,
    private router: Router,
    private fb: FormBuilder
  ) {
    this.filterForm = this.fb.group({
      search: [''],
      position: [''],
      nationality: ['']
    });
  }

  ngOnInit(): void {
    this.loadCrew();
    this.loadFilterOptions();
    
    // Setup filter listeners
    this.filterForm.valueChanges.subscribe(() => {
      this.pageInfo.page = 0;
      this.loadCrew();
    });
    
    // Listen for navigation events to refresh data when returning from create/edit pages
    this.routerSubscription = this.router.events
      .pipe(filter(event => event instanceof NavigationEnd))
      .subscribe((event: NavigationEnd) => {
        if (event.url === '/crew') {
          this.loadCrew();
        }
      });
  }

  ngOnDestroy(): void {
    this.routerSubscription.unsubscribe();
  }

  loadCrew(): void {
    this.loading = true;
    this.error = '';
    
    this.crewService.getAllCrew(this.pageInfo.page, this.pageInfo.size).subscribe({
      next: (response: PageResponse<CrewMember>) => {
        let crew = response.content;
        
        // Apply client-side filtering
        const filters = this.filterForm.value;
        
        if (filters.search) {
          const searchTerm = filters.search.toLowerCase().trim();
          crew = crew.filter(member => 
            member.name?.toLowerCase().includes(searchTerm) ||
            member.position?.toLowerCase().includes(searchTerm) ||
            member.nationality?.toLowerCase().includes(searchTerm) ||
            member.passportNumber?.toLowerCase().includes(searchTerm) ||
            (member.certificate && member.certificate.toLowerCase().includes(searchTerm))
          );
        }
        
        if (filters.position) {
          crew = crew.filter(member => 
            member.position?.toUpperCase().trim() === filters.position.toUpperCase().trim()
          );
        }
        
        if (filters.nationality) {
          crew = crew.filter(member => 
            member.nationality?.toLowerCase().trim() === filters.nationality.toLowerCase().trim()
          );
        }
        
        this.crewList = crew;
        this.pageInfo.totalElements = response.totalElements;
        this.pageInfo.totalPages = response.totalPages;
        this.loading = false;
      },
      error: (error) => {
        this.error = 'Failed to load crew members. Please try again.';
        this.loading = false;
        console.error('Error loading crew:', error);
        this.crewList = [];
      }
    });
  }

  loadFilterOptions(): void {
    // Fetch all crew members to extract unique nationalities and positions
    // Using a larger page size to get more data
    this.crewService.getAllCrew(0, 100).subscribe({
      next: (response: PageResponse<CrewMember>) => {
        // Extract unique nationalities from crew data
        const uniqueNationalities = new Set<string>();
        const uniquePositions = new Set<string>();
        
        response.content.forEach(member => {
          if (member.nationality) {
            uniqueNationalities.add(member.nationality);
          }
          if (member.position) {
            uniquePositions.add(member.position);
          }
        });
        
        // Sort alphabetically
        this.nationalities = Array.from(uniqueNationalities).sort();
        this.crewPositions = Array.from(uniquePositions).sort();
        
        console.log('Loaded nationalities from database:', this.nationalities);
        console.log('Loaded positions from database:', this.crewPositions);
      },
      error: (error) => {
        console.error('Error loading filter options:', error);
        // Fallback to common values if loading fails
        this.nationalities = ['American', 'British', 'German', 'French', 'Italian', 'Spanish', 'Greek', 'Norwegian', 'Danish', 'Swedish', 'Dutch', 'Belgian', 'Polish', 'Filipino', 'Indian'];
        this.crewPositions = ['CAPTAIN', 'ENGINEER', 'SAILOR', 'COOK', 'RADIO_OPERATOR', 'DECK_OFFICER', 'ENGINE_OFFICER'];
      }
    });
  }

  onPageChange(page: number): void {
    this.pageInfo.page = page;
    this.loadCrew();
  }

  onCreateCrew(): void {
    this.router.navigate(['/crew/create']);
  }

  onEditCrew(crew: CrewMember): void {
    this.router.navigate(['/crew/edit', crew.id]);
  }

  onDeleteCrew(crew: CrewMember): void {
    if (confirm(`Are you sure you want to delete crew member "${crew.name}"?`)) {
      this.crewService.deleteCrewMember(crew.id).subscribe({
        next: () => {
          this.loadCrew(); // Reload the list
        },
        error: (error) => {
          console.error('Error deleting crew member:', error);
          alert('Failed to delete crew member. Please try again.');
        }
      });
    }
  }

  getPositionLabel(position: string): string {
    return position.replace('_', ' ').replace(/\b\w/g, l => l.toUpperCase());
  }

  getStatusClass(status: string): string {
    switch (status?.toUpperCase()) {
      case 'ACTIVE': return 'success';
      case 'ON_LEAVE': return 'warning';
      case 'INACTIVE': return 'danger';
      default: return 'secondary';
    }
  }
}
