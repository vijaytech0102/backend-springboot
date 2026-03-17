import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '@environments/environment';
import { Doctor, ApiResponse } from '../models';
import { map } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class DoctorService {

  constructor(private http: HttpClient) { }

  getAllDoctors(specialisation?: string): Observable<Doctor[]> {
    let url = `${environment.apiUrl}/doctors`;
    if (specialisation) {
      url += `?specialisation=${specialisation}`;
    }
    return this.http.get<ApiResponse<Doctor[]>>(url)
      .pipe(map(response => response.data));
  }

  getDoctorById(id: number): Observable<Doctor> {
    return this.http.get<ApiResponse<Doctor>>(`${environment.apiUrl}/doctors/${id}`)
      .pipe(map(response => response.data));
  }

  getCurrentProfile(): Observable<Doctor> {
    return this.http.get<ApiResponse<Doctor>>(`${environment.apiUrl}/doctors/profile`)
      .pipe(map(response => response.data));
  }

  updateProfile(doctor: Partial<Doctor>): Observable<Doctor> {
    return this.http.put<ApiResponse<Doctor>>(`${environment.apiUrl}/doctors/profile`, doctor)
      .pipe(map(response => response.data));
  }

  updateAvailability(doctorId: number, available: boolean): Observable<any> {
    return this.http.put<ApiResponse<any>>(`${environment.apiUrl}/doctors/${doctorId}/availability`, null, {
      params: { available: available.toString() }
    }).pipe(map(response => response.data));
  }
}
