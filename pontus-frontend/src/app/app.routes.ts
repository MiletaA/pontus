import { Routes } from '@angular/router';
import { AuthGuard } from './guards/auth.guard';

export const routes: Routes = [
  {
    path: '',
    redirectTo: '/auth/login',
    pathMatch: 'full'
  },
  {
    path: 'auth',
    loadChildren: () => import('./modules/auth/auth.module').then(m => m.AuthModule)
  },
  {
    path: 'dashboard',
    loadChildren: () => import('./modules/dashboard/dashboard.module').then(m => m.DashboardModule),
    canActivate: [AuthGuard]
  },
  {
    path: 'vessels',
    loadChildren: () => import('./modules/vessels/vessels.module').then(m => m.VesselsModule),
    canActivate: [AuthGuard]
  },
  {
    path: 'docks',
    loadChildren: () => import('./modules/docks/docks.module').then(m => m.DocksModule),
    canActivate: [AuthGuard]
  },
  {
    path: 'cargo',
    loadChildren: () => import('./modules/cargo/cargo.module').then(m => m.CargoModule),
    canActivate: [AuthGuard]
  },
  {
    path: 'crew',
    loadChildren: () => import('./modules/crew/crew.module').then(m => m.CrewModule),
    canActivate: [AuthGuard]
  },
  {
    path: 'deliveries',
    loadChildren: () => import('./modules/deliveries/deliveries.module').then(m => m.DeliveriesModule),
    canActivate: [AuthGuard]
  },
  {
    path: '**',
    redirectTo: '/auth/login'
  }
];
