import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule } from '@angular/forms';
import { DeliveriesRoutingModule } from './deliveries-routing.module';

import { DeliveryListComponent } from './components/delivery-list/delivery-list.component';
import { DeliveryFormComponent } from './components/delivery-form/delivery-form.component';
import { DeliveryTableComponent } from './components/delivery-table/delivery-table.component';
import { DeliveryFiltersComponent } from './components/delivery-filters/delivery-filters.component';
import { DeliveryStatusBadgeComponent } from './components/delivery-status-badge/delivery-status-badge.component';

@NgModule({
  declarations: [
    DeliveryListComponent,
    DeliveryFormComponent,
    DeliveryTableComponent,
    DeliveryFiltersComponent,
    DeliveryStatusBadgeComponent
  ],
  imports: [
    CommonModule,
    ReactiveFormsModule,
    DeliveriesRoutingModule
  ]
})
export class DeliveriesModule { }
