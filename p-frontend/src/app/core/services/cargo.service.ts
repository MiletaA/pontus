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

export interface Cargo {
  id: number;
  vesselId: number;
  description: string;
  weightTons: number;
  isDangerous: boolean;
  customsStatus: string;
  cargoType?: string;
  origin?: string;
  destination?: string;
  createdAt: string;
  updatedAt?: string;
}

export interface CargoCreateRequest {
  vesselId: number;
  description: string;
  weightTons: number;
  isDangerous?: boolean;
  customsStatus: string;
  cargoType?: string;
  origin?: string;
  destination?: string;
}

@Injectable({
  providedIn: 'root'
})
export class CargoService {
  private apiUrl = environment.apiUrl + '/cargo';

  constructor(private http: HttpClient) {}

  createCargo(cargo: CargoCreateRequest): Observable<Cargo> {
    return this.http.post<Cargo>(this.apiUrl, cargo);
  }

  getCargoById(id: number): Observable<Cargo> {
    return this.http.get<Cargo>(`${this.apiUrl}/${id}`);
  }

  getCargoByCode(cargoCode: string): Observable<Cargo> {
    return this.http.get<Cargo>(`${this.apiUrl}/code/${cargoCode}`);
  }

  getAllCargo(page: number = 0, size: number = 20, sort: string = 'id'): Observable<PageResponse<Cargo>> {
    // The backend might return a list directly, so we need to handle both cases
    return this.http.get<any>(this.apiUrl).pipe(
      map((response: any) => {
        if (Array.isArray(response)) {
          // Backend returns array directly
          return {
            content: response,
            totalElements: response.length,
            totalPages: 1,
            size: response.length,
            number: 0,
            first: true,
            last: true
          };
        } else {
          // Backend returns PageResponse
          return response as PageResponse<Cargo>;
        }
      })
    );
  }

  getCargoByVessel(vesselId: number, page: number = 0, size: number = 20): Observable<PageResponse<Cargo>> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());
    return this.http.get<PageResponse<Cargo>>(`${this.apiUrl}/vessel/${vesselId}`, { params });
  }

  getCargoByStatus(status: string, page: number = 0, size: number = 20): Observable<PageResponse<Cargo>> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());
    return this.http.get<PageResponse<Cargo>>(`${this.apiUrl}/status/${status}`, { params });
  }

  getCargoByType(cargoType: string, page: number = 0, size: number = 20): Observable<PageResponse<Cargo>> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());
    return this.http.get<PageResponse<Cargo>>(`${this.apiUrl}/type/${cargoType}`, { params });
  }

  getHazardousCargo(page: number = 0, size: number = 20): Observable<PageResponse<Cargo>> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());
    return this.http.get<PageResponse<Cargo>>(`${this.apiUrl}/hazardous`, { params });
  }

  getCargoByCustomsStatus(customsStatus: string, page: number = 0, size: number = 20): Observable<PageResponse<Cargo>> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());
    return this.http.get<PageResponse<Cargo>>(`${this.apiUrl}/customs-status/${customsStatus}`, { params });
  }

  updateCargo(id: number, cargo: Partial<CargoCreateRequest>): Observable<Cargo> {
    return this.http.put<Cargo>(`${this.apiUrl}/${id}`, cargo);
  }

  deleteCargo(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  updateCargoStatus(id: number, status: string): Observable<Cargo> {
    const params = new HttpParams().set('status', status);
    return this.http.patch<Cargo>(`${this.apiUrl}/${id}/status`, null, { params });
  }

  updateCustomsStatus(id: number, customsStatus: string): Observable<Cargo> {
    const params = new HttpParams().set('customsStatus', customsStatus);
    return this.http.patch<Cargo>(`${this.apiUrl}/${id}/customs-status`, null, { params });
  }
}
