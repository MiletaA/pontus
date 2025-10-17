import { Component, Output, EventEmitter } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { CargoFilters, CUSTOMS_STATUSES } from '../../../../models/cargo.model';

@Component({
  selector: 'app-cargo-filters',
  templateUrl: './cargo-filters.component.html',
  styleUrls: ['./cargo-filters.component.scss']
})
export class CargoFiltersComponent {
  @Output() filtersChanged = new EventEmitter<CargoFilters>();

  filterForm: FormGroup;
  customsStatuses = CUSTOMS_STATUSES;

  constructor(private formBuilder: FormBuilder) {
    this.filterForm = this.formBuilder.group({
      search: [''],
      vesselId: [''],
      customsStatus: [''],
      isDangerous: [''],
      minWeight: [''],
      maxWeight: ['']
    });

    this.filterForm.valueChanges.subscribe(value => {
      this.emitFilters(value);
    });
  }

  private emitFilters(formValue: any): void {
    const filters: CargoFilters = {};

    if (formValue.search?.trim()) {
      filters.search = formValue.search.trim();
    }
    if (formValue.vesselId) {
      filters.vesselId = parseInt(formValue.vesselId);
    }
    if (formValue.customsStatus) {
      filters.customsStatus = formValue.customsStatus;
    }
    if (formValue.isDangerous !== '') {
      filters.isDangerous = formValue.isDangerous === 'true';
    }
    if (formValue.minWeight) {
      filters.minWeight = parseFloat(formValue.minWeight);
    }
    if (formValue.maxWeight) {
      filters.maxWeight = parseFloat(formValue.maxWeight);
    }

    this.filtersChanged.emit(filters);
  }

  clearFilters(): void {
    this.filterForm.reset({
      search: '',
      vesselId: '',
      customsStatus: '',
      isDangerous: '',
      minWeight: '',
      maxWeight: ''
    });
  }
}
