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

export interface Delivery {
  id: number;
  cargoId: number;
  destinationAddress: string;
  deliveryStatus: 'SCHEDULED' | 'IN_TRANSIT' | 'DELIVERED' | 'FAILED' | 'CANCELLED';
  vehicleRegistration: string;
  driverName: string;
  deliveryTime?: string;
  scheduledDeliveryTime?: string;
  notes?: string;
  createdAt: string;
  updatedAt?: string;
}

export interface DeliveryCreateRequest {
  cargoId: number;
  destinationAddress: string;
  deliveryStatus: string;
  vehicleRegistration: string;
  driverName: string;
  deliveryTime?: string;
  scheduledDeliveryTime?: string;
  notes?: string;
}

@Injectable({
  providedIn: 'root'
})
export class DeliveryService {
  private apiUrl = environment.apiUrl + '/deliveries';

  constructor(private http: HttpClient) {}

  createDelivery(delivery: DeliveryCreateRequest): Observable<Delivery> {
    return this.http.post<Delivery>(this.apiUrl, delivery);
  }

  getDeliveryById(id: number): Observable<Delivery> {
    return this.http.get<Delivery>(`${this.apiUrl}/${id}`);
  }

  getDeliveryByCode(deliveryCode: string): Observable<Delivery> {
    return this.http.get<Delivery>(`${this.apiUrl}/code/${deliveryCode}`);
  }

  getDeliveryByTrackingNumber(trackingNumber: string): Observable<Delivery> {
    return this.http.get<Delivery>(`${this.apiUrl}/tracking/${trackingNumber}`);
  }

  getAllDeliveries(page: number = 0, size: number = 20, sort: string = 'id'): Observable<PageResponse<Delivery>> {
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
          return response as PageResponse<Delivery>;
        }
      })
    );
  }

  getDeliveriesByVessel(vesselId: number, page: number = 0, size: number = 20): Observable<PageResponse<Delivery>> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());
    return this.http.get<PageResponse<Delivery>>(`${this.apiUrl}/vessel/${vesselId}`, { params });
  }

  getDeliveriesByCargo(cargoId: number): Observable<Delivery[]> {
    return this.http.get<Delivery[]>(`${this.apiUrl}/cargo/${cargoId}`);
  }

  getDeliveriesByStatus(status: string, page: number = 0, size: number = 20): Observable<PageResponse<Delivery>> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());
    return this.http.get<PageResponse<Delivery>>(`${this.apiUrl}/status/${status}`, { params });
  }

  getDeliveriesByCarrier(carrier: string, page: number = 0, size: number = 20): Observable<PageResponse<Delivery>> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());
    return this.http.get<PageResponse<Delivery>>(`${this.apiUrl}/carrier/${carrier}`, { params });
  }

  getPendingDeliveries(): Observable<Delivery[]> {
    return this.http.get<Delivery[]>(`${this.apiUrl}/pending`);
  }

  getCompletedDeliveries(page: number = 0, size: number = 20): Observable<PageResponse<Delivery>> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());
    return this.http.get<PageResponse<Delivery>>(`${this.apiUrl}/completed`, { params });
  }

  getDelayedDeliveries(): Observable<Delivery[]> {
    return this.http.get<Delivery[]>(`${this.apiUrl}/delayed`);
  }

  updateDelivery(id: number, delivery: Partial<DeliveryCreateRequest>): Observable<Delivery> {
    return this.http.put<Delivery>(`${this.apiUrl}/${id}`, delivery);
  }

  deleteDelivery(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  updateDeliveryStatus(id: number, status: string): Observable<Delivery> {
    const params = new HttpParams().set('status', status);
    return this.http.patch<Delivery>(`${this.apiUrl}/${id}/status`, null, { params });
  }

  confirmPickup(id: number, actualPickup: string): Observable<Delivery> {
    const params = new HttpParams().set('actualPickup', actualPickup);
    return this.http.patch<Delivery>(`${this.apiUrl}/${id}/confirm-pickup`, null, { params });
  }

  confirmDelivery(id: number, actualDelivery: string, deliveryProof?: string): Observable<Delivery> {
    let params = new HttpParams().set('actualDelivery', actualDelivery);
    if (deliveryProof) {
      params = params.set('deliveryProof', deliveryProof);
    }
    return this.http.patch<Delivery>(`${this.apiUrl}/${id}/confirm-delivery`, null, { params });
  }
}
