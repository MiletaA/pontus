import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';

import { VesselsRoutingModule } from './vessels-routing.module';
import { VesselListComponent } from './components/vessel-list/vessel-list.component';
import { VesselTableComponent } from './components/vessel-table/vessel-table.component';
import { VesselFormComponent } from './components/vessel-form/vessel-form.component';
import { VesselStatusBadgeComponent } from './components/vessel-status-badge/vessel-status-badge.component';
import { VesselFiltersComponent } from './components/vessel-filters/vessel-filters.component';

@NgModule({
  declarations: [
    VesselListComponent,
    VesselFormComponent,
    VesselTableComponent,
    VesselStatusBadgeComponent,
    VesselFiltersComponent
  ],
  imports: [
    CommonModule,
    ReactiveFormsModule,
    RouterModule,
    VesselsRoutingModule
  ],
  exports: [
    VesselListComponent,
    VesselFormComponent,
    VesselTableComponent,
    VesselStatusBadgeComponent,
    VesselFiltersComponent
  ]
})
export class VesselsModule { }
