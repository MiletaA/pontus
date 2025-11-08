import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { CrewService } from '../../../core/services/crew.service';

@Component({
  selector: 'app-crew-create',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './crew-create.component.html',
  styleUrls: ['./crew-create.component.scss']
})
export class CrewCreateComponent implements OnInit {
  crew = {
    vesselId: 0,
    name: '',
    nationality: '',
    position: '',
    dateOfBirth: '',
    passportNumber: '',
    certificate: '',
    certificateExpiry: ''
  };
  
  loading = false;
  error = '';
  today = new Date().toISOString().split('T')[0];
  maxBirthDate = '';
  
  // Dynamic options
  positions: string[] = [];
  nationalities: string[] = [];

  constructor(
    private crewService: CrewService,
    private router: Router
  ) {}

  ngOnInit(): void {
    // Set max birth date to 18 years ago
    const eighteenYearsAgo = new Date();
    eighteenYearsAgo.setFullYear(eighteenYearsAgo.getFullYear() - 18);
    this.maxBirthDate = eighteenYearsAgo.toISOString().split('T')[0];
    
    this.loadFilterOptions();
  }
  
  loadFilterOptions(): void {
    // Fetch crew to extract unique positions and nationalities
    this.crewService.getAllCrew(0, 100).subscribe({
      next: (response) => {
        console.log('Fetched crew data:', response);
        const uniquePositions = new Set<string>();
        const uniqueNationalities = new Set<string>();
        
        response.content.forEach(member => {
          if (member.position) {
            uniquePositions.add(member.position);
          }
          if (member.nationality) {
            uniqueNationalities.add(member.nationality);
          }
        });
        
        // Sort alphabetically
        this.positions = Array.from(uniquePositions).sort();
        this.nationalities = Array.from(uniqueNationalities).sort();
        
        console.log('Extracted positions from database:', this.positions);
        console.log('Extracted nationalities from database:', this.nationalities);
        
        // If no data, provide defaults
        if (this.positions.length === 0) {
          console.warn('No positions found in database, using defaults');
          this.positions = ['CAPTAIN', 'ENGINEER', 'SAILOR', 'COOK', 'RADIO_OPERATOR', 'DECK_OFFICER', 'ENGINE_OFFICER'];
        }
        if (this.nationalities.length === 0) {
          console.warn('No nationalities found in database, using defaults');
          this.nationalities = ['American', 'British', 'German', 'French', 'Italian', 'Spanish', 'Greek', 'Norwegian', 'Danish', 'Swedish'];
        }
      },
      error: (error) => {
        console.error('Error loading filter options:', error);
        console.warn('Using fallback defaults due to error');
        // Fallback to defaults
        this.positions = ['CAPTAIN', 'ENGINEER', 'SAILOR', 'COOK', 'RADIO_OPERATOR', 'DECK_OFFICER', 'ENGINE_OFFICER'];
        this.nationalities = ['American', 'British', 'German', 'French', 'Italian', 'Spanish', 'Greek', 'Norwegian', 'Danish', 'Swedish'];
      }
    });
  }
  
  getPositionLabel(position: string): string {
    switch (position) {
      case 'CAPTAIN': return 'Captain';
      case 'ENGINEER': return 'Engineer';
      case 'SAILOR': return 'Sailor';
      case 'COOK': return 'Cook';
      case 'RADIO_OPERATOR': return 'Radio Operator';
      case 'DECK_OFFICER': return 'Deck Officer';
      case 'ENGINE_OFFICER': return 'Engine Officer';
      default: return position?.replace('_', ' ').replace(/\b\w/g, l => l.toUpperCase());
    }
  }

  onSubmit(): void {
    if (this.loading) return;
    
    this.loading = true;
    this.error = '';

    // Convert form data to proper format
    const crewData = {
      ...this.crew,
      vesselId: Number(this.crew.vesselId),
      certificate: this.crew.certificate || undefined,
      certificateExpiry: this.crew.certificateExpiry || undefined
    };

    this.crewService.createCrewMember(crewData).subscribe({
      next: (response) => {
        console.log('Crew member created successfully:', response);
        this.router.navigate(['/crew']);
      },
      error: (error) => {
        console.error('Error creating crew member:', error);
        this.error = error.error?.message || 'Failed to create crew member. Please try again.';
        this.loading = false;
      }
    });
  }

  onCancel(): void {
    this.router.navigate(['/crew']);
  }
}
