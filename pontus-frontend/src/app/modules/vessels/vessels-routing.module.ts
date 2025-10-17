import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { VesselListComponent } from './components/vessel-list/vessel-list.component';
import { VesselFormComponent } from './components/vessel-form/vessel-form.component';

const routes: Routes = [
  {
    path: '',
    component: VesselListComponent
  },
  {
    path: 'new',
    component: VesselFormComponent
  },
  {
    path: 'edit/:id',
    component: VesselFormComponent
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class VesselsRoutingModule { }
