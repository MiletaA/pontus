import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { 
  Delivery, 
  DeliveryCreateRequest, 
  DeliveryUpdateRequest,
  DeliveryFilters,
  DeliveryStatus 
} from '../models/delivery.model';
import { ApiResponse } from '../models/vessel.model';
import { AuthService } from './auth.service';

@Injectable({
  providedIn: 'root'
})
export class DeliveryService {
  private readonly API_URL = 'http://localhost:8080/deliveries';

  constructor(
    private http: HttpClient,
    private authService: AuthService
  ) {}

  getAllDeliveries(): Observable<ApiResponse<Delivery[]>> {
    return this.http.get<ApiResponse<Delivery[]>>(this.API_URL, {
      headers: this.authService.getAuthHeaders()
    });
  }

  getDeliveryById(id: number): Observable<ApiResponse<Delivery>> {
    return this.http.get<ApiResponse<Delivery>>(`${this.API_URL}/${id}`, {
      headers: this.authService.getAuthHeaders()
    });
  }

  createDelivery(delivery: DeliveryCreateRequest): Observable<ApiResponse<Delivery>> {
    return this.http.post<ApiResponse<Delivery>>(this.API_URL, delivery, {
      headers: this.authService.getAuthHeaders()
    });
  }

  updateDelivery(id: number, delivery: DeliveryUpdateRequest): Observable<ApiResponse<Delivery>> {
    return this.http.put<ApiResponse<Delivery>>(`${this.API_URL}/${id}`, delivery, {
      headers: this.authService.getAuthHeaders()
    });
  }

  deleteDelivery(id: number): Observable<ApiResponse<void>> {
    return this.http.delete<ApiResponse<void>>(`${this.API_URL}/${id}`, {
      headers: this.authService.getAuthHeaders()
    });
  }

  getDeliveriesByCargo(cargoId: number): Observable<ApiResponse<Delivery[]>> {
    return this.http.get<ApiResponse<Delivery[]>>(`${this.API_URL}/cargo/${cargoId}`, {
      headers: this.authService.getAuthHeaders()
    });
  }

  updateDeliveryStatus(id: number, status: DeliveryStatus): Observable<ApiResponse<Delivery>> {
    return this.http.patch<ApiResponse<Delivery>>(`${this.API_URL}/${id}/status`, 
      { status }, {
      headers: this.authService.getAuthHeaders()
    });
  }

  markAsDelivered(id: number, actualDate: string): Observable<ApiResponse<Delivery>> {
    return this.http.patch<ApiResponse<Delivery>>(`${this.API_URL}/${id}/delivered`, 
      { actualDate }, {
      headers: this.authService.getAuthHeaders()
    });
  }

  getDeliveriesByStatus(status: DeliveryStatus): Observable<ApiResponse<Delivery[]>> {
    return this.http.get<ApiResponse<Delivery[]>>(`${this.API_URL}/status/${status}`, {
      headers: this.authService.getAuthHeaders()
    });
  }

  getScheduledDeliveries(): Observable<ApiResponse<Delivery[]>> {
    return this.http.get<ApiResponse<Delivery[]>>(`${this.API_URL}/scheduled`, {
      headers: this.authService.getAuthHeaders()
    });
  }

  getInTransitDeliveries(): Observable<ApiResponse<Delivery[]>> {
    return this.http.get<ApiResponse<Delivery[]>>(`${this.API_URL}/in-transit`, {
      headers: this.authService.getAuthHeaders()
    });
  }

  searchDeliveries(filters: DeliveryFilters): Observable<ApiResponse<Delivery[]>> {
    let params = new URLSearchParams();
    
    if (filters.cargoId !== undefined) {
      params.append('cargoId', filters.cargoId.toString());
    }
    if (filters.status) {
      params.append('status', filters.status);
    }
    if (filters.search) {
      params.append('search', filters.search);
    }
    if (filters.scheduledFrom) {
      params.append('scheduledFrom', filters.scheduledFrom);
    }
    if (filters.scheduledTo) {
      params.append('scheduledTo', filters.scheduledTo);
    }

    const queryString = params.toString();
    const url = queryString ? `${this.API_URL}/search?${queryString}` : `${this.API_URL}/search`;

    return this.http.get<ApiResponse<Delivery[]>>(url, {
      headers: this.authService.getAuthHeaders()
    });
  }
}
