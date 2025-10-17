import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { CargoListComponent } from './components/cargo-list/cargo-list.component';
import { CargoFormComponent } from './components/cargo-form/cargo-form.component';

const routes: Routes = [
  {
    path: '',
    component: CargoListComponent
  },
  {
    path: 'new',
    component: CargoFormComponent
  },
  {
    path: 'edit/:id',
    component: CargoFormComponent
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class CargoRoutingModule { }
