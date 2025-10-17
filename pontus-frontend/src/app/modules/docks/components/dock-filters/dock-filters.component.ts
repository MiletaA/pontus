import { Component, Output, EventEmitter } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { DockFilters } from '../../../../models/dock.model';

@Component({
  selector: 'app-dock-filters',
  templateUrl: './dock-filters.component.html',
  styleUrls: ['./dock-filters.component.scss']
})
export class DockFiltersComponent {
  @Output() filtersChanged = new EventEmitter<DockFilters>();

  filterForm: FormGroup;

  constructor(private formBuilder: FormBuilder) {
    this.filterForm = this.formBuilder.group({
      search: [''],
      isOccupied: [''],
      handlesDangerous: [''],
      minLength: [''],
      maxLength: ['']
    });

    // Subscribe to form changes
    this.filterForm.valueChanges.subscribe(value => {
      this.emitFilters(value);
    });
  }

  private emitFilters(formValue: any): void {
    const filters: DockFilters = {};

    if (formValue.search?.trim()) {
      filters.search = formValue.search.trim();
    }

    if (formValue.isOccupied !== '') {
      filters.isOccupied = formValue.isOccupied === 'true';
    }

    if (formValue.handlesDangerous !== '') {
      filters.handlesDangerous = formValue.handlesDangerous === 'true';
    }

    if (formValue.minLength) {
      filters.minLength = parseFloat(formValue.minLength);
    }

    if (formValue.maxLength) {
      filters.maxLength = parseFloat(formValue.maxLength);
    }

    this.filtersChanged.emit(filters);
  }

  clearFilters(): void {
    this.filterForm.reset({
      search: '',
      isOccupied: '',
      handlesDangerous: '',
      minLength: '',
      maxLength: ''
    });
  }
}
