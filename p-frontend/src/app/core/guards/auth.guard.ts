import { inject } from '@angular/core';
import { Router, CanActivateFn } from '@angular/router';
import { AuthService } from '../services/auth.service';

export const authGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthService);
  const router = inject(Router);


  if (authService.isAuthenticated()) {
    // Check for required roles if specified in route data
    const requiredRoles = route.data['roles'] as string[];
    if (requiredRoles && requiredRoles.length > 0) {
      if (authService.hasAnyRole(requiredRoles)) {
        return true;
      } else {
        // User doesn't have required role, redirect to unauthorized page
        router.navigate(['/unauthorized']);
        return false;
      }
    }
    return true;
  }
  
  // Not authenticated, redirect to login
  router.navigate(['/login'], { queryParams: { returnUrl: state.url }});
  return false;
};
