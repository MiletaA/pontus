import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { environment } from '../../../environments/environment';

export interface PageResponse<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  size: number;
  number: number;
  first: boolean;
  last: boolean;
}

export interface Dock {
  id: number;
  name: string;
  maxLength: number;
  isOccupied: boolean;
  assignedVesselId?: number;
  scheduledFrom?: string;
  scheduledTo?: string;
  handlesDangerous: boolean;
  description?: string;
  createdAt: string;
  updatedAt?: string;
}

export interface DockCreateRequest {
  name: string;
  maxLength: number;
  isOccupied?: boolean;
  assignedVesselId?: number;
  scheduledFrom?: string;
  scheduledTo?: string;
  handlesDangerous?: boolean;
  description?: string;
}

@Injectable({
  providedIn: 'root'
})
export class DockService {
  private apiUrl = environment.apiUrl + '/docks';

  constructor(private http: HttpClient) {}

  createDock(dock: DockCreateRequest): Observable<Dock> {
    return this.http.post<Dock>(this.apiUrl, dock);
  }

  getDockById(id: number): Observable<Dock> {
    return this.http.get<Dock>(`${this.apiUrl}/${id}`);
  }

  getDockByCode(dockCode: string): Observable<Dock> {
    return this.http.get<Dock>(`${this.apiUrl}/code/${dockCode}`);
  }

  getAllDocks(page: number = 0, size: number = 20, sort: string = 'id'): Observable<PageResponse<Dock>> {
    // The backend returns a list directly, so we need to transform it to PageResponse
    return this.http.get<Dock[]>(this.apiUrl).pipe(
      map((docks: Dock[]) => ({
        content: docks,
        totalElements: docks.length,
        totalPages: 1,
        size: docks.length,
        number: 0,
        first: true,
        last: true
      }))
    );
  }

  getAvailableDocks(): Observable<Dock[]> {
    return this.http.get<Dock[]>(`${this.apiUrl}/available`);
  }

  getDocksByStatus(status: string, page: number = 0, size: number = 20): Observable<PageResponse<Dock>> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());
    return this.http.get<PageResponse<Dock>>(`${this.apiUrl}/status/${status}`, { params });
  }

  getDocksByType(dockType: string, page: number = 0, size: number = 20): Observable<PageResponse<Dock>> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());
    return this.http.get<PageResponse<Dock>>(`${this.apiUrl}/type/${dockType}`, { params });
  }

  updateDock(id: number, dock: Partial<DockCreateRequest>): Observable<Dock> {
    return this.http.put<Dock>(`${this.apiUrl}/${id}`, dock);
  }

  deleteDock(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  assignVesselToDock(dockId: number, vesselId: number): Observable<Dock> {
    const params = new HttpParams().set('vesselId', vesselId.toString());
    return this.http.patch<Dock>(`${this.apiUrl}/${dockId}/assign-vessel`, null, { params });
  }

  releaseVesselFromDock(dockId: number): Observable<Dock> {
    return this.http.patch<Dock>(`${this.apiUrl}/${dockId}/release-vessel`, null);
  }
}
