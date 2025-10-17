import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { 
  Dock, 
  DockCreateRequest, 
  DockUpdateRequest, 
  DockAssignmentRequest,
  DockFilters 
} from '../models/dock.model';
import { ApiResponse } from '../models/vessel.model';
import { AuthService } from './auth.service';

@Injectable({
  providedIn: 'root'
})
export class DockService {
  private readonly API_URL = 'http://localhost:8080/docks';

  constructor(
    private http: HttpClient,
    private authService: AuthService
  ) {}

  getAllDocks(): Observable<ApiResponse<Dock[]>> {
    return this.http.get<ApiResponse<Dock[]>>(this.API_URL, {
      headers: this.authService.getAuthHeaders()
    });
  }

  getDockById(id: number): Observable<ApiResponse<Dock>> {
    return this.http.get<ApiResponse<Dock>>(`${this.API_URL}/${id}`, {
      headers: this.authService.getAuthHeaders()
    });
  }

  createDock(dock: DockCreateRequest): Observable<ApiResponse<Dock>> {
    return this.http.post<ApiResponse<Dock>>(this.API_URL, dock, {
      headers: this.authService.getAuthHeaders()
    });
  }

  updateDock(id: number, dock: DockUpdateRequest): Observable<ApiResponse<Dock>> {
    return this.http.put<ApiResponse<Dock>>(`${this.API_URL}/${id}`, dock, {
      headers: this.authService.getAuthHeaders()
    });
  }

  deleteDock(id: number): Observable<ApiResponse<void>> {
    return this.http.delete<ApiResponse<void>>(`${this.API_URL}/${id}`, {
      headers: this.authService.getAuthHeaders()
    });
  }

  assignVesselToDock(dockId: number, assignment: DockAssignmentRequest): Observable<ApiResponse<Dock>> {
    return this.http.post<ApiResponse<Dock>>(`${this.API_URL}/${dockId}/assign`, assignment, {
      headers: this.authService.getAuthHeaders()
    });
  }

  unassignVesselFromDock(dockId: number): Observable<ApiResponse<Dock>> {
    return this.http.post<ApiResponse<Dock>>(`${this.API_URL}/${dockId}/unassign`, {}, {
      headers: this.authService.getAuthHeaders()
    });
  }

  getAvailableDocks(): Observable<ApiResponse<Dock[]>> {
    return this.http.get<ApiResponse<Dock[]>>(`${this.API_URL}/available`, {
      headers: this.authService.getAuthHeaders()
    });
  }

  getOccupiedDocks(): Observable<ApiResponse<Dock[]>> {
    return this.http.get<ApiResponse<Dock[]>>(`${this.API_URL}/occupied`, {
      headers: this.authService.getAuthHeaders()
    });
  }

  searchDocks(filters: DockFilters): Observable<ApiResponse<Dock[]>> {
    let params = new URLSearchParams();
    
    if (filters.isOccupied !== undefined) {
      params.append('isOccupied', filters.isOccupied.toString());
    }
    if (filters.handlesDangerous !== undefined) {
      params.append('handlesDangerous', filters.handlesDangerous.toString());
    }
    if (filters.search) {
      params.append('search', filters.search);
    }
    if (filters.minLength !== undefined) {
      params.append('minLength', filters.minLength.toString());
    }
    if (filters.maxLength !== undefined) {
      params.append('maxLength', filters.maxLength.toString());
    }

    const queryString = params.toString();
    const url = queryString ? `${this.API_URL}/search?${queryString}` : `${this.API_URL}/search`;

    return this.http.get<ApiResponse<Dock[]>>(url, {
      headers: this.authService.getAuthHeaders()
    });
  }
}
