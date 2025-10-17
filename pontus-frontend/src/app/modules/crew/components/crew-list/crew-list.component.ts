import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { CrewService } from '../../../../services/crew.service';
import { AuthService } from '../../../../services/auth.service';
import { CrewMember, CrewFilters } from '../../../../models/crew.model';

@Component({
  selector: 'app-crew-list',
  templateUrl: './crew-list.component.html',
  styleUrls: ['./crew-list.component.scss']
})
export class CrewListComponent implements OnInit {
  crewMembers: CrewMember[] = [];
  filteredCrewMembers: CrewMember[] = [];
  loading = true;
  error = '';
  filters: CrewFilters = {};

  constructor(
    private crewService: CrewService,
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.loadCrewMembers();
  }

  loadCrewMembers(): void {
    this.loading = true;
    this.error = '';

    this.crewService.getAllCrewMembers().subscribe({
      next: (response) => {
        this.crewMembers = response.data || [];
        this.filteredCrewMembers = [...this.crewMembers];
        this.loading = false;
      },
      error: (error) => {
        this.error = 'Failed to load crew members';
        this.loading = false;
        console.error('Error loading crew members:', error);
      }
    });
  }

  onFiltersChanged(filters: CrewFilters): void {
    this.filters = filters;
    this.applyFilters();
  }

  private applyFilters(): void {
    this.filteredCrewMembers = this.crewMembers.filter(crewMember => {
      if (this.filters.nationality && crewMember.nationality !== this.filters.nationality) {
        return false;
      }
      if (this.filters.rank && crewMember.rank !== this.filters.rank) {
        return false;
      }
      if (this.filters.vesselId !== undefined && crewMember.vesselId !== this.filters.vesselId) {
        return false;
      }
      if (this.filters.search) {
        const searchTerm = this.filters.search.toLowerCase();
        return crewMember.firstName.toLowerCase().includes(searchTerm) ||
               crewMember.lastName.toLowerCase().includes(searchTerm) ||
               crewMember.passportNumber.toLowerCase().includes(searchTerm) ||
               crewMember.certificateNumber?.toLowerCase().includes(searchTerm);
      }
      return true;
    });
  }

  onCrewMemberDeleted(crewMemberId: number): void {
    this.crewMembers = this.crewMembers.filter(crewMember => crewMember.id !== crewMemberId);
    this.applyFilters();
  }

  canCreate(): boolean {
    return this.authService.canCreate();
  }

  createNewCrewMember(): void {
    this.router.navigate(['/crew/new']);
  }

  refreshData(): void {
    this.loadCrewMembers();
  }

  getAssignedCrewCount(): number {
    return this.crewMembers.filter(c => c.vesselId).length;
  }

  getUnassignedCrewCount(): number {
    return this.crewMembers.filter(c => !c.vesselId).length;
  }
}
