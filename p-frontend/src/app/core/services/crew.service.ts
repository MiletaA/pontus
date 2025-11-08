import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
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

export interface CrewMember {
  id: number;
  vesselId: number;
  name: string;
  nationality: string;
  position: string;
  dateOfBirth: string;
  passportNumber: string;
  certificate?: string;
  certificateExpiry?: string;
  createdAt: string;
  updatedAt?: string;
}

export interface CrewCreateRequest {
  vesselId: number;
  name: string;
  nationality: string;
  position: string;
  dateOfBirth: string;
  passportNumber: string;
  certificate?: string;
  certificateExpiry?: string;
}

@Injectable({
  providedIn: 'root'
})
export class CrewService {
  private apiUrl = environment.apiUrl + '/crew';

  constructor(private http: HttpClient) {}

  createCrewMember(crew: CrewCreateRequest): Observable<CrewMember> {
    return this.http.post<CrewMember>(this.apiUrl, crew);
  }

  getCrewMemberById(id: number): Observable<CrewMember> {
    return this.http.get<CrewMember>(`${this.apiUrl}/${id}`);
  }

  getCrewMemberByPassport(passportNumber: string): Observable<CrewMember> {
    return this.http.get<CrewMember>(`${this.apiUrl}/passport/${passportNumber}`);
  }

  getAllCrew(page: number = 0, size: number = 20, sort: string = 'id'): Observable<PageResponse<CrewMember>> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString())
      .set('sort', sort);
    return this.http.get<PageResponse<CrewMember>>(this.apiUrl, { params });
  }

  getCrewByVessel(vesselId: number): Observable<CrewMember[]> {
    return this.http.get<CrewMember[]>(`${this.apiUrl}/vessel/${vesselId}`);
  }

  getCrewByStatus(status: string, page: number = 0, size: number = 20): Observable<PageResponse<CrewMember>> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());
    return this.http.get<PageResponse<CrewMember>>(`${this.apiUrl}/status/${status}`, { params });
  }

  getCrewByPosition(position: string, page: number = 0, size: number = 20): Observable<PageResponse<CrewMember>> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());
    return this.http.get<PageResponse<CrewMember>>(`${this.apiUrl}/position/${position}`, { params });
  }

  getCrewByRank(rank: string, page: number = 0, size: number = 20): Observable<PageResponse<CrewMember>> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());
    return this.http.get<PageResponse<CrewMember>>(`${this.apiUrl}/rank/${rank}`, { params });
  }

  getCrewByNationality(nationality: string, page: number = 0, size: number = 20): Observable<PageResponse<CrewMember>> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());
    return this.http.get<PageResponse<CrewMember>>(`${this.apiUrl}/nationality/${nationality}`, { params });
  }

  getCrewWithExpiringDocuments(): Observable<CrewMember[]> {
    return this.http.get<CrewMember[]>(`${this.apiUrl}/expiring-documents`);
  }

  updateCrewMember(id: number, crew: Partial<CrewCreateRequest>): Observable<CrewMember> {
    return this.http.put<CrewMember>(`${this.apiUrl}/${id}`, crew);
  }

  deleteCrewMember(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  assignCrewToVessel(crewId: number, vesselId: number): Observable<CrewMember> {
    const params = new HttpParams().set('vesselId', vesselId.toString());
    return this.http.patch<CrewMember>(`${this.apiUrl}/${crewId}/assign-vessel`, null, { params });
  }

  removeCrewFromVessel(crewId: number): Observable<CrewMember> {
    return this.http.patch<CrewMember>(`${this.apiUrl}/${crewId}/remove-vessel`, null);
  }

  updateCrewStatus(crewId: number, status: string): Observable<CrewMember> {
    const params = new HttpParams().set('status', status);
    return this.http.patch<CrewMember>(`${this.apiUrl}/${crewId}/status`, null, { params });
  }
}
