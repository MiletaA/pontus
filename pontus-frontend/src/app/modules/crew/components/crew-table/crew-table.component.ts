import { Component, Input, Output, EventEmitter } from '@angular/core';
import { Router } from '@angular/router';
import { CrewService } from '../../../../services/crew.service';
import { AuthService } from '../../../../services/auth.service';
import { CrewMember } from '../../../../models/crew.model';

@Component({
  selector: 'app-crew-table',
  templateUrl: './crew-table.component.html',
  styleUrls: ['./crew-table.component.scss']
})
export class CrewTableComponent {
  @Input() crewMembers: CrewMember[] = [];
  @Input() loading = false;
  @Output() crewMemberDeleted = new EventEmitter<number>();

  constructor(
    private crewService: CrewService,
    private authService: AuthService,
    private router: Router
  ) {}

  editCrewMember(crewMember: CrewMember): void {
    this.router.navigate(['/crew/edit', crewMember.id]);
  }

  deleteCrewMember(crewMember: CrewMember): void {
    if (confirm(`Are you sure you want to delete crew member "${crewMember.firstName} ${crewMember.lastName}"?`)) {
      this.crewService.deleteCrewMember(crewMember.id).subscribe({
        next: () => {
          this.crewMemberDeleted.emit(crewMember.id);
        },
        error: (error) => {
          console.error('Error deleting crew member:', error);
          alert('Failed to delete crew member. Please try again.');
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

  trackByCrewMemberId(index: number, crewMember: CrewMember): number {
    return crewMember.id;
  }
}
