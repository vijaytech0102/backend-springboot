import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '@environments/environment';
import { User, ApiResponse } from '../models';
import { map } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class PatientService {

  constructor(private http: HttpClient) { }

  getCurrentProfile(): Observable<User> {
    return this.http.get<ApiResponse<User>>(`${environment.apiUrl}/patients/profile`)
      .pipe(map(response => response.data));
  }

  updateProfile(user: Partial<User>): Observable<User> {
    return this.http.put<ApiResponse<User>>(`${environment.apiUrl}/patients/profile`, user)
      .pipe(map(response => response.data));
  }

  deleteAccount(): Observable<any> {
    return this.http.delete<ApiResponse<any>>(`${environment.apiUrl}/patients/profile`)
      .pipe(map(response => response.data));
  }
}
