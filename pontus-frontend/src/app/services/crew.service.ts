import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { 
  CrewMember, 
  CrewCreateRequest, 
  CrewUpdateRequest,
  CrewFilters 
} from '../models/crew.model';
import { ApiResponse } from '../models/vessel.model';
import { AuthService } from './auth.service';

@Injectable({
  providedIn: 'root'
})
export class CrewService {
  private readonly API_URL = 'http://localhost:8080/crew';

  constructor(
    private http: HttpClient,
    private authService: AuthService
  ) {}

  getAllCrewMembers(): Observable<ApiResponse<CrewMember[]>> {
    return this.http.get<ApiResponse<CrewMember[]>>(this.API_URL, {
      headers: this.authService.getAuthHeaders()
    });
  }

  getCrewMemberById(id: number): Observable<ApiResponse<CrewMember>> {
    return this.http.get<ApiResponse<CrewMember>>(`${this.API_URL}/${id}`, {
      headers: this.authService.getAuthHeaders()
    });
  }

  createCrewMember(crewMember: CrewCreateRequest): Observable<ApiResponse<CrewMember>> {
    return this.http.post<ApiResponse<CrewMember>>(this.API_URL, crewMember, {
      headers: this.authService.getAuthHeaders()
    });
  }

  updateCrewMember(id: number, crewMember: CrewUpdateRequest): Observable<ApiResponse<CrewMember>> {
    return this.http.put<ApiResponse<CrewMember>>(`${this.API_URL}/${id}`, crewMember, {
      headers: this.authService.getAuthHeaders()
    });
  }

  deleteCrewMember(id: number): Observable<ApiResponse<void>> {
    return this.http.delete<ApiResponse<void>>(`${this.API_URL}/${id}`, {
      headers: this.authService.getAuthHeaders()
    });
  }

  getCrewByVessel(vesselId: number): Observable<ApiResponse<CrewMember[]>> {
    return this.http.get<ApiResponse<CrewMember[]>>(`${this.API_URL}/vessel/${vesselId}`, {
      headers: this.authService.getAuthHeaders()
    });
  }

  assignCrewToVessel(crewId: number, vesselId: number): Observable<ApiResponse<CrewMember>> {
    return this.http.patch<ApiResponse<CrewMember>>(`${this.API_URL}/${crewId}/assign-vessel`, 
      { vesselId }, {
      headers: this.authService.getAuthHeaders()
    });
  }

  unassignCrewFromVessel(crewId: number): Observable<ApiResponse<CrewMember>> {
    return this.http.patch<ApiResponse<CrewMember>>(`${this.API_URL}/${crewId}/unassign-vessel`, {}, {
      headers: this.authService.getAuthHeaders()
    });
  }

  getCrewByNationality(nationality: string): Observable<ApiResponse<CrewMember[]>> {
    return this.http.get<ApiResponse<CrewMember[]>>(`${this.API_URL}/nationality/${nationality}`, {
      headers: this.authService.getAuthHeaders()
    });
  }

  getCrewByRank(rank: string): Observable<ApiResponse<CrewMember[]>> {
    return this.http.get<ApiResponse<CrewMember[]>>(`${this.API_URL}/rank/${rank}`, {
      headers: this.authService.getAuthHeaders()
    });
  }

  searchCrewMembers(filters: CrewFilters): Observable<ApiResponse<CrewMember[]>> {
    let params = new URLSearchParams();
    
    if (filters.nationality) {
      params.append('nationality', filters.nationality);
    }
    if (filters.rank) {
      params.append('rank', filters.rank);
    }
    if (filters.vesselId !== undefined) {
      params.append('vesselId', filters.vesselId.toString());
    }
    if (filters.search) {
      params.append('search', filters.search);
    }

    const queryString = params.toString();
    const url = queryString ? `${this.API_URL}/search?${queryString}` : `${this.API_URL}/search`;

    return this.http.get<ApiResponse<CrewMember[]>>(url, {
      headers: this.authService.getAuthHeaders()
    });
  }
}
