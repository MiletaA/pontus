import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterOutlet, RouterModule, Router } from '@angular/router';
import { AuthService } from './services/auth.service';
import { User } from './models/auth.model';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, RouterOutlet, RouterModule],
  template: `
    <div class="main-container">
      <!-- Navigation Bar -->
      <nav class="navbar navbar-expand-lg navbar-dark bg-primary">
        <div class="container">
          <a class="navbar-brand" routerLink="/">
            <i class="fas fa-anchor me-2"></i>
            Pontus Port Management
          </a>
          
          <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
            <span class="navbar-toggler-icon"></span>
          </button>
          
          <div class="collapse navbar-collapse" id="navbarNav">
            <ul class="navbar-nav me-auto">
              <li class="nav-item">
                <a class="nav-link" routerLink="/dashboard" routerLinkActive="active">
                  <i class="fas fa-tachometer-alt me-1"></i>
                  Dashboard
                </a>
              </li>
              <li class="nav-item">
                <a class="nav-link" routerLink="/vessels" routerLinkActive="active">
                  <i class="fas fa-ship me-1"></i>
                  Vessels
                </a>
              </li>
              <li class="nav-item">
                <a class="nav-link" routerLink="/docks" routerLinkActive="active">
                  <i class="fas fa-warehouse me-1"></i>
                  Docks
                </a>
              </li>
              <li class="nav-item">
                <a class="nav-link" routerLink="/cargo" routerLinkActive="active">
                  <i class="fas fa-boxes me-1"></i>
                  Cargo
                </a>
              </li>
              <li class="nav-item">
                <a class="nav-link" routerLink="/crew" routerLinkActive="active">
                  <i class="fas fa-users me-1"></i>
                  Crew
                </a>
              </li>
              <li class="nav-item">
                <a class="nav-link" routerLink="/deliveries" routerLinkActive="active">
                  <i class="fas fa-truck me-1"></i>
                  Deliveries
                </a>
              </li>
            </ul>
            
            <ul class="navbar-nav">
              <!-- Services Dropdown -->
              <li class="nav-item dropdown">
                <a class="nav-link dropdown-toggle" href="#" role="button" data-bs-toggle="dropdown">
                  <i class="fas fa-cog me-1"></i>
                  Services
                </a>
                <ul class="dropdown-menu">
                  <li><a class="dropdown-item" href="http://localhost:8081/swagger-ui.html" target="_blank">
                    <i class="fas fa-ship me-2"></i>Vessel Service API
                  </a></li>
                  <li><a class="dropdown-item" href="http://localhost:8081/actuator/health" target="_blank">
                    <i class="fas fa-heartbeat me-2"></i>Service Health
                  </a></li>
                </ul>
              </li>
              
              <!-- User Profile Dropdown (only if authenticated) -->
              <li class="nav-item dropdown" *ngIf="isAuthenticated()">
                <a class="nav-link dropdown-toggle" href="#" role="button" data-bs-toggle="dropdown">
                  <i class="fas fa-user-circle me-1"></i>
                  {{ getUserDisplayName() }}
                </a>
                <ul class="dropdown-menu dropdown-menu-end">
                  <li>
                    <div class="dropdown-header">
                      <div class="fw-bold">{{ getUserDisplayName() }}</div>
                      <small class="text-muted">{{ getUserRole() }}</small>
                      <small class="text-muted d-block">{{ currentUser?.email }}</small>
                    </div>
                  </li>
                  <li><hr class="dropdown-divider"></li>
                  <li>
                    <a class="dropdown-item" href="#" routerLink="/profile">
                      <i class="fas fa-user me-2"></i>Profile Settings
                    </a>
                  </li>
                  <li>
                    <a class="dropdown-item" href="#" (click)="logout()">
                      <i class="fas fa-sign-out-alt me-2"></i>Logout
                    </a>
                  </li>
                </ul>
              </li>
              
              <!-- Login Link (only if not authenticated) -->
              <li class="nav-item" *ngIf="!isAuthenticated()">
                <a class="nav-link" routerLink="/auth/login">
                  <i class="fas fa-sign-in-alt me-1"></i>
                  Login
                </a>
              </li>
            </ul>
          </div>
        </div>
      </nav>

      <!-- Main Content -->
      <div class="content-wrapper">
        <router-outlet></router-outlet>
      </div>

      <!-- Footer -->
      <footer class="bg-light py-3 mt-auto">
        <div class="container text-center">
          <small class="text-muted">
            © 2024 Pontus Port Management System | 
            <a href="http://localhost:8081/swagger-ui.html" target="_blank" class="text-decoration-none">API Documentation</a>
          </small>
        </div>
      </footer>
    </div>
  `,
  styles: [`
    .navbar-brand {
      font-weight: 700;
      font-size: 1.5rem;
    }
    
    .nav-link {
      font-weight: 500;
      transition: all 0.2s ease;
    }
    
    .nav-link:hover {
      transform: translateY(-1px);
    }
    
    .nav-link.active {
      background-color: rgba(255, 255, 255, 0.1);
      border-radius: 6px;
    }
    
    .dropdown-menu {
      border: none;
      box-shadow: 0 4px 20px rgba(0, 0, 0, 0.15);
      border-radius: 8px;
    }
    
    .dropdown-item {
      padding: 0.75rem 1rem;
      transition: all 0.2s ease;
    }
    
    .dropdown-item:hover {
      background-color: #f8f9fa;
      transform: translateX(5px);
    }
    
    footer {
      border-top: 1px solid #dee2e6;
    }
    
    .dropdown-header {
      padding: 0.75rem 1rem;
      background-color: #f8f9fa;
      border-bottom: 1px solid #dee2e6;
    }
    
    .dropdown-header .fw-bold {
      font-size: 1rem;
      color: #495057;
    }
    
    .dropdown-header .text-muted {
      font-size: 0.875rem;
    }
    
    .dropdown-menu-end {
      min-width: 250px;
    }
    
    .user-avatar {
      width: 32px;
      height: 32px;
      border-radius: 50%;
      background: linear-gradient(135deg, #007bff, #0056b3);
      display: inline-flex;
      align-items: center;
      justify-content: center;
      color: white;
      font-weight: 600;
      margin-right: 0.5rem;
    }
  `]
})
export class AppComponent implements OnInit, OnDestroy {
  title = 'Pontus Port Management System';
  currentUser: User | null = null;
  private userSubscription: Subscription = new Subscription();

  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.userSubscription = this.authService.currentUser$.subscribe(
      user => this.currentUser = user
    );
  }

  ngOnDestroy(): void {
    this.userSubscription.unsubscribe();
  }

  logout(): void {
    this.authService.logout();
    this.router.navigate(['/auth/login']);
  }

  getUserDisplayName(): string {
    if (!this.currentUser) return 'Guest';
    return this.currentUser.firstName && this.currentUser.lastName 
      ? `${this.currentUser.firstName} ${this.currentUser.lastName}`
      : this.currentUser.username;
  }

  getUserRole(): string {
    if (!this.currentUser) return '';
    return this.currentUser.role.replace('_', ' ').toLowerCase()
      .replace(/\b\w/g, l => l.toUpperCase());
  }

  isAuthenticated(): boolean {
    return this.authService.isAuthenticated();
  }
}
