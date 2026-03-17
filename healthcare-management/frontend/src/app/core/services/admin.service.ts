import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '@environments/environment';
import { User, ApiResponse } from '../models';
import { map } from 'rxjs/operators';

export interface AdminStats {
  totalUsers: number;
  totalDoctors: number;
  totalPatients: number;
  totalAppointments: number;
  completedAppointments: number;
  pendingAppointments: number;
  totalRevenue: number;
  monthlyRevenue: number;
  averageRating: number;
}

@Injectable({
  providedIn: 'root'
})
export class AdminService {

  constructor(private http: HttpClient) { }

  getAllUsers(): Observable<User[]> {
    return this.http.get<ApiResponse<User[]>>(`${environment.apiUrl}/admin/users`)
      .pipe(map(response => response.data));
  }

  toggleUserStatus(userId: number): Observable<User> {
    return this.http.put<ApiResponse<User>>(`${environment.apiUrl}/admin/users/${userId}/toggle`, {})
      .pipe(map(response => response.data));
  }

  deleteUser(userId: number): Observable<any> {
    return this.http.delete<ApiResponse<any>>(`${environment.apiUrl}/admin/users/${userId}`)
      .pipe(map(response => response.data));
  }

  getStats(): Observable<AdminStats> {
    return this.http.get<ApiResponse<AdminStats>>(`${environment.apiUrl}/admin/stats`)
      .pipe(map(response => response.data));
  }
}
