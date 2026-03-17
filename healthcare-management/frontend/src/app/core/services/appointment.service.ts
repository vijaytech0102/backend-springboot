import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '@environments/environment';
import { Appointment, ApiResponse } from '../models';
import { map } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class AppointmentService {

  constructor(private http: HttpClient) { }

  bookAppointment(appointment: any): Observable<Appointment> {
    return this.http.post<ApiResponse<Appointment>>(`${environment.apiUrl}/appointments`, appointment)
      .pipe(map(response => response.data));
  }

  getAppointmentById(id: number): Observable<Appointment> {
    return this.http.get<ApiResponse<Appointment>>(`${environment.apiUrl}/appointments/${id}`)
      .pipe(map(response => response.data));
  }

  confirmAppointment(id: number): Observable<Appointment> {
    return this.http.put<ApiResponse<Appointment>>(`${environment.apiUrl}/appointments/${id}/confirm`, {})
      .pipe(map(response => response.data));
  }

  cancelAppointment(id: number): Observable<Appointment> {
    return this.http.put<ApiResponse<Appointment>>(`${environment.apiUrl}/appointments/${id}/cancel`, {})
      .pipe(map(response => response.data));
  }

  completeAppointment(id: number): Observable<Appointment> {
    return this.http.put<ApiResponse<Appointment>>(`${environment.apiUrl}/appointments/${id}/complete`, {})
      .pipe(map(response => response.data));
  }

  getPatientAppointments(): Observable<Appointment[]> {
    return this.http.get<ApiResponse<Appointment[]>>(`${environment.apiUrl}/appointments/patient/my-appointments`)
      .pipe(map(response => response.data));
  }

  getDoctorAppointments(): Observable<Appointment[]> {
    return this.http.get<ApiResponse<Appointment[]>>(`${environment.apiUrl}/appointments/doctor/my-appointments`)
      .pipe(map(response => response.data));
  }
}
