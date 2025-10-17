import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { DockListComponent } from './components/dock-list/dock-list.component';
import { DockFormComponent } from './components/dock-form/dock-form.component';

const routes: Routes = [
  {
    path: '',
    component: DockListComponent
  },
  {
    path: 'new',
    component: DockFormComponent
  },
  {
    path: 'edit/:id',
    component: DockFormComponent
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class DocksRoutingModule { }
