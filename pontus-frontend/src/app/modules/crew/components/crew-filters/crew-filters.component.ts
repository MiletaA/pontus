import { Component, Output, EventEmitter } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { CrewFilters, CREW_RANKS, COMMON_NATIONALITIES } from '../../../../models/crew.model';

@Component({
  selector: 'app-crew-filters',
  templateUrl: './crew-filters.component.html',
  styleUrls: ['./crew-filters.component.scss']
})
export class CrewFiltersComponent {
  @Output() filtersChanged = new EventEmitter<CrewFilters>();

  filterForm: FormGroup;
  ranks = CREW_RANKS;
  nationalities = COMMON_NATIONALITIES;

  constructor(private formBuilder: FormBuilder) {
    this.filterForm = this.formBuilder.group({
      search: [''],
      nationality: [''],
      rank: [''],
      vesselId: ['']
    });

    this.filterForm.valueChanges.subscribe(value => {
      this.emitFilters(value);
    });
  }

  private emitFilters(formValue: any): void {
    const filters: CrewFilters = {};

    if (formValue.search?.trim()) {
      filters.search = formValue.search.trim();
    }
    if (formValue.nationality) {
      filters.nationality = formValue.nationality;
    }
    if (formValue.rank) {
      filters.rank = formValue.rank;
    }
    if (formValue.vesselId) {
      filters.vesselId = parseInt(formValue.vesselId);
    }

    this.filtersChanged.emit(filters);
  }

  clearFilters(): void {
    this.filterForm.reset({
      search: '',
      nationality: '',
      rank: '',
      vesselId: ''
    });
  }
}
