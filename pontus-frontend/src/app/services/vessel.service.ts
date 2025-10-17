import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse, HttpParams } from '@angular/common/http';
import { Observable, BehaviorSubject, throwError } from 'rxjs';
import { catchError, tap, map } from 'rxjs/operators';
import { 
  Vessel, 
  VesselCreateRequest, 
  VesselUpdateRequest, 
  VesselFilters,
  VesselStatus,
  ApiResponse 
} from '../models/vessel.model';

@Injectable({
  providedIn: 'root'
})
export class VesselService {
  private readonly baseUrl = 'http://localhost:8080/vessels';
  
  // State management
  private vesselsSubject = new BehaviorSubject<Vessel[]>([]);
  public vessels$ = this.vesselsSubject.asObservable();
  
  private loadingSubject = new BehaviorSubject<boolean>(false);
  public loading$ = this.loadingSubject.asObservable();

  constructor(private http: HttpClient) {}

  /**
   * Get all vessels
   */
  getAllVessels(): Observable<ApiResponse<Vessel[]>> {
    this.loadingSubject.next(true);
    return this.http.get<ApiResponse<Vessel[]>>(this.baseUrl).pipe(
      tap((response: ApiResponse<Vessel[]>) => {
        this.vesselsSubject.next(response.data || []);
        this.loadingSubject.next(false);
      }),
      catchError(this.handleError.bind(this))
    );
  }

  /**
   * Get vessel by ID
   */
  getVessel(id: number): Observable<Vessel> {
    return this.http.get<Vessel>(`${this.baseUrl}/${id}`).pipe(
      catchError(this.handleError.bind(this))
    );
  }

  /**
   * Get vessel by ID (alias)
   */
  getVesselById(id: number): Observable<Vessel> {
    return this.getVessel(id);
  }

  /**
   * Get vessel by IMO number
   */
  getVesselByImo(imoNumber: string): Observable<Vessel> {
    return this.http.get<Vessel>(`${this.baseUrl}/imo/${imoNumber}`).pipe(
      catchError(this.handleError.bind(this))
    );
  }

  /**
   * Create new vessel
   */
  createVessel(vessel: VesselCreateRequest): Observable<Vessel> {
    this.loadingSubject.next(true);
    return this.http.post<Vessel>(this.baseUrl, vessel).pipe(
      tap(() => {
        this.refreshVessels();
        this.loadingSubject.next(false);
      }),
      catchError(this.handleError.bind(this))
    );
  }

  /**
   * Update existing vessel
   */
  updateVessel(id: number, vessel: VesselUpdateRequest): Observable<Vessel> {
    this.loadingSubject.next(true);
    return this.http.put<Vessel>(`${this.baseUrl}/${id}`, vessel).pipe(
      tap(() => {
        this.refreshVessels();
        this.loadingSubject.next(false);
      }),
      catchError(this.handleError.bind(this))
    );
  }

  /**
   * Delete vessel
   */
  deleteVessel(id: number): Observable<void> {
    this.loadingSubject.next(true);
    return this.http.delete<void>(`${this.baseUrl}/${id}`).pipe(
      tap(() => {
        this.refreshVessels();
        this.loadingSubject.next(false);
      }),
      catchError(this.handleError.bind(this))
    );
  }

  /**
   * Get vessels by status
   */
  getVesselsByStatus(status: VesselStatus): Observable<Vessel[]> {
    return this.http.get<Vessel[]>(`${this.baseUrl}/status/${status}`).pipe(
      catchError(this.handleError.bind(this))
    );
  }

  /**
   * Get vessels by type
   */
  getVesselsByType(vesselType: string): Observable<Vessel[]> {
    return this.http.get<Vessel[]>(`${this.baseUrl}/type/${vesselType}`).pipe(
      catchError(this.handleError.bind(this))
    );
  }

  /**
   * Get vessels expected to arrive
   */
  getExpectedArrivals(): Observable<Vessel[]> {
    return this.http.get<Vessel[]>(`${this.baseUrl}/expected-arrivals`).pipe(
      catchError(this.handleError.bind(this))
    );
  }

  /**
   * Get vessels expected to depart
   */
  getExpectedDepartures(): Observable<Vessel[]> {
    return this.http.get<Vessel[]>(`${this.baseUrl}/expected-departures`).pipe(
      catchError(this.handleError.bind(this))
    );
  }

  /**
   * Get vessels currently in port
   */
  getVesselsInPort(): Observable<Vessel[]> {
    return this.http.get<Vessel[]>(`${this.baseUrl}/in-port`).pipe(
      catchError(this.handleError.bind(this))
    );
  }

  /**
   * Update vessel arrival time
   */
  updateVesselArrival(id: number, actualArrival: string): Observable<Vessel> {
    const params = new HttpParams().set('actualArrival', actualArrival);
    return this.http.patch<Vessel>(`${this.baseUrl}/${id}/arrival`, null, { params }).pipe(
      tap(() => this.refreshVessels()),
      catchError(this.handleError.bind(this))
    );
  }

  /**
   * Update vessel departure time
   */
  updateVesselDeparture(id: number, actualDeparture: string): Observable<Vessel> {
    const params = new HttpParams().set('actualDeparture', actualDeparture);
    return this.http.patch<Vessel>(`${this.baseUrl}/${id}/departure`, null, { params }).pipe(
      tap(() => this.refreshVessels()),
      catchError(this.handleError.bind(this))
    );
  }

  /**
   * Check service health
   */
  checkHealth(): Observable<string> {
    return this.http.get(`${this.baseUrl}/health`, { responseType: 'text' }).pipe(
      catchError(this.handleError.bind(this))
    );
  }

  /**
   * Filter vessels based on criteria
   */
  filterVessels(vessels: Vessel[], filters: VesselFilters): Vessel[] {
    return vessels.filter(vessel => {
      // Status filter
      if (filters.status && vessel.status !== filters.status) {
        return false;
      }

      // Vessel type filter
      if (filters.vesselType && vessel.vesselType !== filters.vesselType) {
        return false;
      }

      // Flag country filter
      if (filters.flagCountry && vessel.flagCountry !== filters.flagCountry) {
        return false;
      }

      // Search filter (name or IMO)
      if (filters.search) {
        const searchLower = filters.search.toLowerCase();
        const nameMatch = vessel.name.toLowerCase().includes(searchLower);
        const imoMatch = vessel.imoNumber.toLowerCase().includes(searchLower);
        if (!nameMatch && !imoMatch) {
          return false;
        }
      }

      return true;
    });
  }

  /**
   * Get vessel statistics
   */
  getVesselStatistics(vessels: Vessel[]): any {
    const stats = {
      total: vessels.length,
      byStatus: {} as Record<VesselStatus, number>,
      byType: {} as Record<string, number>,
      averageLength: 0,
      countries: new Set<string>()
    };

    vessels.forEach(vessel => {
      // Count by status
      stats.byStatus[vessel.status] = (stats.byStatus[vessel.status] || 0) + 1;
      
      // Count by type
      stats.byType[vessel.vesselType] = (stats.byType[vessel.vesselType] || 0) + 1;
      
      // Add to countries set
      stats.countries.add(vessel.flagCountry);
    });

    // Calculate average length
    if (vessels.length > 0) {
      const totalLength = vessels.reduce((sum, vessel) => sum + vessel.length, 0);
      stats.averageLength = Math.round((totalLength / vessels.length) * 100) / 100;
    }

    return {
      ...stats,
      countries: Array.from(stats.countries)
    };
  }

  /**
   * Refresh vessels list
   */
  private refreshVessels(): void {
    this.getAllVessels().subscribe();
  }

  /**
   * Handle HTTP errors
   */
  private handleError(error: HttpErrorResponse): Observable<never> {
    this.loadingSubject.next(false);
    
    let errorMessage = 'An unknown error occurred';
    
    if (error.error instanceof ErrorEvent) {
      // Client-side error
      errorMessage = `Client Error: ${error.error.message}`;
    } else {
      // Server-side error
      if (error.status === 0) {
        errorMessage = 'Unable to connect to the server. Please check if the vessel service is running.';
      } else if (error.status === 404) {
        errorMessage = 'Vessel not found';
      } else if (error.status === 409) {
        errorMessage = 'Vessel with this IMO number already exists';
      } else if (error.status >= 400 && error.status < 500) {
        errorMessage = error.error?.message || `Client Error: ${error.status}`;
      } else if (error.status >= 500) {
        errorMessage = 'Server error. Please try again later.';
      }
    }

    console.error('Vessel Service Error:', error);
    return throwError(() => new Error(errorMessage));
  }

  /**
   * Format date for API
   */
  formatDateForApi(date: Date): string {
    return date.toISOString().slice(0, 19);
  }

  /**
   * Parse date from API
   */
  parseDateFromApi(dateString: string): Date {
    return new Date(dateString);
  }
}
