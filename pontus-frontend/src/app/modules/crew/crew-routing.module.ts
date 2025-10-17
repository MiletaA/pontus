import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { CrewListComponent } from './components/crew-list/crew-list.component';
import { CrewFormComponent } from './components/crew-form/crew-form.component';

const routes: Routes = [
  {
    path: '',
    component: CrewListComponent
  },
  {
    path: 'new',
    component: CrewFormComponent
  },
  {
    path: 'edit/:id',
    component: CrewFormComponent
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class CrewRoutingModule { }
