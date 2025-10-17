import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule } from '@angular/forms';
import { DocksRoutingModule } from './docks-routing.module';

import { DockListComponent } from './components/dock-list/dock-list.component';
import { DockFormComponent } from './components/dock-form/dock-form.component';
import { DockTableComponent } from './components/dock-table/dock-table.component';
import { DockFiltersComponent } from './components/dock-filters/dock-filters.component';
import { DockStatusBadgeComponent } from './components/dock-status-badge/dock-status-badge.component';

@NgModule({
  declarations: [
    DockListComponent,
    DockFormComponent,
    DockTableComponent,
    DockFiltersComponent,
    DockStatusBadgeComponent
  ],
  imports: [
    CommonModule,
    ReactiveFormsModule,
    DocksRoutingModule
  ]
})
export class DocksModule { }
