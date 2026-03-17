import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '@environments/environment';
import { Bill, ApiResponse } from '../models';
import { map } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class BillingService {

  constructor(private http: HttpClient) { }

  getBillByAppointmentId(appointmentId: number): Observable<Bill> {
    return this.http.get<ApiResponse<Bill>>(`${environment.apiUrl}/billing/appointment/${appointmentId}`)
      .pipe(map(response => response.data));
  }

  getPatientBills(): Observable<Bill[]> {
    return this.http.get<ApiResponse<Bill[]>>(`${environment.apiUrl}/billing/patient`)
      .pipe(map(response => response.data));
  }

  payBill(billId: number): Observable<Bill> {
    return this.http.put<ApiResponse<Bill>>(`${environment.apiUrl}/billing/${billId}/pay`, {})
      .pipe(map(response => response.data));
  }

  getAllBills(): Observable<Bill[]> {
    return this.http.get<ApiResponse<Bill[]>>(`${environment.apiUrl}/billing/admin/all`)
      .pipe(map(response => response.data));
  }
}
