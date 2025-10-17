import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule } from '@angular/forms';
import { CargoRoutingModule } from './cargo-routing.module';

import { CargoListComponent } from './components/cargo-list/cargo-list.component';
import { CargoFormComponent } from './components/cargo-form/cargo-form.component';
import { CargoTableComponent } from './components/cargo-table/cargo-table.component';
import { CargoFiltersComponent } from './components/cargo-filters/cargo-filters.component';
import { CargoStatusBadgeComponent } from './components/cargo-status-badge/cargo-status-badge.component';

@NgModule({
  declarations: [
    CargoListComponent,
    CargoFormComponent,
    CargoTableComponent,
    CargoFiltersComponent,
    CargoStatusBadgeComponent
  ],
  imports: [
    CommonModule,
    ReactiveFormsModule,
    CargoRoutingModule
  ]
})
export class CargoModule { }
