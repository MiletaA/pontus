import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { DeliveryListComponent } from './components/delivery-list/delivery-list.component';
import { DeliveryFormComponent } from './components/delivery-form/delivery-form.component';

const routes: Routes = [
  {
    path: '',
    component: DeliveryListComponent
  },
  {
    path: 'new',
    component: DeliveryFormComponent
  },
  {
    path: 'edit/:id',
    component: DeliveryFormComponent
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class DeliveriesRoutingModule { }
