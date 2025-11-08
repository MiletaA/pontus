import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

export interface Vessel {
  id: number;
  name: string;
  imoNumber: string;
  vesselType: string;
  length: number;
  flagCountry: string;
  status: string;
  scheduledArrival?: string;
  scheduledDeparture?: string;
  actualArrival?: string;
  actualDeparture?: string;
}

export interface VesselCreateRequest {
  name: string;
  imoNumber: string;
  vesselType: string;
  length: number;
  flagCountry: string;
  status: string;
  scheduledArrival?: string | null;
  scheduledDeparture?: string | null;
  actualArrival?: string | null;
  actualDeparture?: string | null;
  currentPort?: string;
  lastPort?: string;
  nextPort?: string;
  eta?: string;
  etd?: string;
}

export interface PageResponse<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  size: number;
  number: number;
  first: boolean;
  last: boolean;
}

@Injectable({
  providedIn: 'root'
})
export class VesselService {
  private apiUrl = environment.apiUrl + '/vessels';

  constructor(private http: HttpClient) {}

  createVessel(vessel: VesselCreateRequest): Observable<Vessel> {
    return this.http.post<Vessel>(this.apiUrl, vessel);
  }

  getVesselById(id: number): Observable<Vessel> {
    return this.http.get<Vessel>(`${this.apiUrl}/${id}`);
  }

  getVesselByImoNumber(imoNumber: string): Observable<Vessel> {
    return this.http.get<Vessel>(`${this.apiUrl}/imo/${imoNumber}`);
  }

  getAllVessels(page: number = 0, size: number = 20, sort: string = 'id'): Observable<PageResponse<Vessel>> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString())
      .set('sort', sort);
    return this.http.get<PageResponse<Vessel>>(this.apiUrl, { params });
  }

  getVesselsByStatus(status: string, page: number = 0, size: number = 20): Observable<PageResponse<Vessel>> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());
    return this.http.get<PageResponse<Vessel>>(`${this.apiUrl}/status/${status}`, { params });
  }

  getVesselsByType(vesselType: string, page: number = 0, size: number = 20): Observable<PageResponse<Vessel>> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());
    return this.http.get<PageResponse<Vessel>>(`${this.apiUrl}/type/${vesselType}`, { params });
  }

  getVesselsExpectedToArrive(): Observable<Vessel[]> {
    return this.http.get<Vessel[]>(`${this.apiUrl}/expected-arrivals`);
  }

  getVesselsExpectedToDepart(): Observable<Vessel[]> {
    return this.http.get<Vessel[]>(`${this.apiUrl}/expected-departures`);
  }

  getVesselsCurrentlyInPort(): Observable<Vessel[]> {
    return this.http.get<Vessel[]>(`${this.apiUrl}/in-port`);
  }

  updateVessel(id: number, vessel: Partial<VesselCreateRequest>): Observable<Vessel> {
    return this.http.put<Vessel>(`${this.apiUrl}/${id}`, vessel);
  }

  deleteVessel(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  updateVesselArrival(id: number, actualArrival: string): Observable<Vessel> {
    const params = new HttpParams().set('actualArrival', actualArrival);
    return this.http.patch<Vessel>(`${this.apiUrl}/${id}/arrival`, null, { params });
  }

  updateVesselDeparture(id: number, actualDeparture: string): Observable<Vessel> {
    const params = new HttpParams().set('actualDeparture', actualDeparture);
    return this.http.patch<Vessel>(`${this.apiUrl}/${id}/departure`, null, { params });
  }
}
