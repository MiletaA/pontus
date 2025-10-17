import { Component, Output, EventEmitter } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { DeliveryFilters, DeliveryStatus } from '../../../../models/delivery.model';

@Component({
  selector: 'app-delivery-filters',
  templateUrl: './delivery-filters.component.html',
  styleUrls: ['./delivery-filters.component.scss']
})
export class DeliveryFiltersComponent {
  @Output() filtersChanged = new EventEmitter<DeliveryFilters>();

  filterForm: FormGroup;
  deliveryStatuses = Object.values(DeliveryStatus);

  constructor(private formBuilder: FormBuilder) {
    this.filterForm = this.formBuilder.group({
      search: [''],
      status: [''],
      cargoId: ['']
    });

    this.filterForm.valueChanges.subscribe(value => {
      this.emitFilters(value);
    });
  }

  private emitFilters(formValue: any): void {
    const filters: DeliveryFilters = {};

    if (formValue.search?.trim()) {
      filters.search = formValue.search.trim();
    }
    if (formValue.status) {
      filters.status = formValue.status;
    }
    if (formValue.cargoId) {
      filters.cargoId = parseInt(formValue.cargoId);
    }

    this.filtersChanged.emit(filters);
  }

  clearFilters(): void {
    this.filterForm.reset({
      search: '',
      status: '',
      cargoId: ''
    });
  }
}
