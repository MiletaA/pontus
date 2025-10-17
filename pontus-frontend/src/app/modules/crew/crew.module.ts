import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule } from '@angular/forms';
import { CrewRoutingModule } from './crew-routing.module';

import { CrewListComponent } from './components/crew-list/crew-list.component';
import { CrewFormComponent } from './components/crew-form/crew-form.component';
import { CrewTableComponent } from './components/crew-table/crew-table.component';
import { CrewFiltersComponent } from './components/crew-filters/crew-filters.component';

@NgModule({
  declarations: [
    CrewListComponent,
    CrewFormComponent,
    CrewTableComponent,
    CrewFiltersComponent
  ],
  imports: [
    CommonModule,
    ReactiveFormsModule,
    CrewRoutingModule
  ]
})
export class CrewModule { }
