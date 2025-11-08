import { Routes } from '@angular/router';
import { authGuard } from './core/guards/auth.guard';

export const routes: Routes = [
  { 
    path: 'login', 
    loadComponent: () => import('./auth/login/login.component').then(m => m.LoginComponent)
  },
  { 
    path: 'register', 
    loadComponent: () => import('./auth/register/register.component').then(m => m.RegisterComponent)
  },
  {
    path: 'dashboard',
    loadComponent: () => import('./features/dashboard/dashboard.component').then(m => m.DashboardComponent),
    canActivate: [authGuard]
  },
  {
    path: 'vessels',
    loadComponent: () => import('./features/vessels/vessels-list.component').then(m => m.VesselsListComponent),
    canActivate: [authGuard]
  },
  {
    path: 'vessels/create',
    loadComponent: () => import('./features/vessels/vessel-create/vessel-create.component').then(m => m.VesselCreateComponent),
    canActivate: [authGuard]
  },
  {
    path: 'vessels/edit/:id',
    loadComponent: () => import('./features/vessels/vessel-edit/vessel-edit.component').then(m => m.VesselEditComponent),
    canActivate: [authGuard]
  },
  {
    path: 'docks',
    loadComponent: () => import('./features/docks/docks-list.component').then(m => m.DocksListComponent),
    canActivate: [authGuard]
  },
  {
    path: 'docks/create',
    loadComponent: () => import('./features/docks/dock-create/dock-create.component').then(m => m.DockCreateComponent),
    canActivate: [authGuard]
  },
  {
    path: 'docks/edit/:id',
    loadComponent: () => import('./features/docks/dock-edit/dock-edit.component').then(m => m.DockEditComponent),
    canActivate: [authGuard]
  },
  {
    path: 'cargo',
    loadComponent: () => import('./features/cargo/cargo-list.component').then(m => m.CargoListComponent),
    canActivate: [authGuard]
  },
  {
    path: 'cargo/create',
    loadComponent: () => import('./features/cargo/cargo-create/cargo-create.component').then(m => m.CargoCreateComponent),
    canActivate: [authGuard]
  },
  {
    path: 'cargo/edit/:id',
    loadComponent: () => import('./features/cargo/cargo-edit/cargo-edit.component').then(m => m.CargoEditComponent),
    canActivate: [authGuard]
  },
  {
    path: 'crew',
    loadComponent: () => import('./features/crew/crew-list.component').then(m => m.CrewListComponent),
    canActivate: [authGuard]
  },
  {
    path: 'crew/create',
    loadComponent: () => import('./features/crew/crew-create/crew-create.component').then(m => m.CrewCreateComponent),
    canActivate: [authGuard]
  },
  {
    path: 'deliveries',
    loadComponent: () => import('./features/deliveries/deliveries-list.component').then(m => m.DeliveriesListComponent),
    canActivate: [authGuard]
  },
  {
    path: 'deliveries/create',
    loadComponent: () => import('./features/deliveries/delivery-create/delivery-create.component').then(m => m.DeliveryCreateComponent),
    canActivate: [authGuard]
  },
  {
    path: 'deliveries/edit/:id',
    loadComponent: () => import('./features/deliveries/delivery-edit/delivery-edit.component').then(m => m.DeliveryEditComponent),
    canActivate: [authGuard]
  },
  {
    path: '',
    redirectTo: '/login',
    pathMatch: 'full'
  },
  {
    path: '**',
    redirectTo: '/dashboard'
  }
];
