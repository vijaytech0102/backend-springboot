import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '@environments/environment';
import { MedicalRecord, ApiResponse } from '../models';
import { map } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class MedicalRecordService {

  constructor(private http: HttpClient) { }

  createMedicalRecord(record: any): Observable<MedicalRecord> {
    return this.http.post<ApiResponse<MedicalRecord>>(`${environment.apiUrl}/medical-records`, record)
      .pipe(map(response => response.data));
  }

  getMedicalRecordByAppointmentId(appointmentId: number): Observable<MedicalRecord> {
    return this.http.get<ApiResponse<MedicalRecord>>(`${environment.apiUrl}/medical-records/appointment/${appointmentId}`)
      .pipe(map(response => response.data));
  }

  getPatientMedicalRecords(): Observable<MedicalRecord[]> {
    return this.http.get<ApiResponse<MedicalRecord[]>>(`${environment.apiUrl}/medical-records/patient`)
      .pipe(map(response => response.data));
  }

  updateMedicalRecord(id: number, record: any): Observable<MedicalRecord> {
    return this.http.put<ApiResponse<MedicalRecord>>(`${environment.apiUrl}/medical-records/${id}`, record)
      .pipe(map(response => response.data));
  }
}
