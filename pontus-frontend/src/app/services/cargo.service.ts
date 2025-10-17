import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { 
  Cargo, 
  CargoCreateRequest, 
  CargoUpdateRequest,
  CargoFilters,
  CustomsStatus 
} from '../models/cargo.model';
import { ApiResponse } from '../models/vessel.model';
import { AuthService } from './auth.service';

@Injectable({
  providedIn: 'root'
})
export class CargoService {
  private readonly API_URL = 'http://localhost:8080/cargo';

  constructor(
    private http: HttpClient,
    private authService: AuthService
  ) {}

  getAllCargo(): Observable<ApiResponse<Cargo[]>> {
    return this.http.get<ApiResponse<Cargo[]>>(this.API_URL, {
      headers: this.authService.getAuthHeaders()
    });
  }

  getCargoById(id: number): Observable<ApiResponse<Cargo>> {
    return this.http.get<ApiResponse<Cargo>>(`${this.API_URL}/${id}`, {
      headers: this.authService.getAuthHeaders()
    });
  }

  createCargo(cargo: CargoCreateRequest): Observable<ApiResponse<Cargo>> {
    return this.http.post<ApiResponse<Cargo>>(this.API_URL, cargo, {
      headers: this.authService.getAuthHeaders()
    });
  }

  updateCargo(id: number, cargo: CargoUpdateRequest): Observable<ApiResponse<Cargo>> {
    return this.http.put<ApiResponse<Cargo>>(`${this.API_URL}/${id}`, cargo, {
      headers: this.authService.getAuthHeaders()
    });
  }

  deleteCargo(id: number): Observable<ApiResponse<void>> {
    return this.http.delete<ApiResponse<void>>(`${this.API_URL}/${id}`, {
      headers: this.authService.getAuthHeaders()
    });
  }

  getCargoByVessel(vesselId: number): Observable<ApiResponse<Cargo[]>> {
    return this.http.get<ApiResponse<Cargo[]>>(`${this.API_URL}/vessel/${vesselId}`, {
      headers: this.authService.getAuthHeaders()
    });
  }

  updateCustomsStatus(id: number, status: CustomsStatus): Observable<ApiResponse<Cargo>> {
    return this.http.patch<ApiResponse<Cargo>>(`${this.API_URL}/${id}/customs-status`, 
      { customsStatus: status }, {
      headers: this.authService.getAuthHeaders()
    });
  }

  getDangerousCargo(): Observable<ApiResponse<Cargo[]>> {
    return this.http.get<ApiResponse<Cargo[]>>(`${this.API_URL}/dangerous`, {
      headers: this.authService.getAuthHeaders()
    });
  }

  getCargoByCustomsStatus(status: CustomsStatus): Observable<ApiResponse<Cargo[]>> {
    return this.http.get<ApiResponse<Cargo[]>>(`${this.API_URL}/customs-status/${status}`, {
      headers: this.authService.getAuthHeaders()
    });
  }

  searchCargo(filters: CargoFilters): Observable<ApiResponse<Cargo[]>> {
    let params = new URLSearchParams();
    
    if (filters.vesselId !== undefined) {
      params.append('vesselId', filters.vesselId.toString());
    }
    if (filters.customsStatus) {
      params.append('customsStatus', filters.customsStatus);
    }
    if (filters.isDangerous !== undefined) {
      params.append('isDangerous', filters.isDangerous.toString());
    }
    if (filters.search) {
      params.append('search', filters.search);
    }
    if (filters.minWeight !== undefined) {
      params.append('minWeight', filters.minWeight.toString());
    }
    if (filters.maxWeight !== undefined) {
      params.append('maxWeight', filters.maxWeight.toString());
    }

    const queryString = params.toString();
    const url = queryString ? `${this.API_URL}/search?${queryString}` : `${this.API_URL}/search`;

    return this.http.get<ApiResponse<Cargo[]>>(url, {
      headers: this.authService.getAuthHeaders()
    });
  }
}
