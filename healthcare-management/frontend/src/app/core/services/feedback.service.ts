import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '@environments/environment';
import { Feedback, ApiResponse } from '../models';
import { map } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class FeedbackService {

  constructor(private http: HttpClient) { }

  submitFeedback(feedback: any): Observable<Feedback> {
    return this.http.post<ApiResponse<Feedback>>(`${environment.apiUrl}/feedback`, feedback)
      .pipe(map(response => response.data));
  }

  getDoctorFeedbacks(doctorId: number): Observable<Feedback[]> {
    return this.http.get<ApiResponse<Feedback[]>>(`${environment.apiUrl}/feedback/doctor/${doctorId}`)
      .pipe(map(response => response.data));
  }

  getDoctorAverageRating(doctorId: number): Observable<number> {
    return this.http.get<ApiResponse<number>>(`${environment.apiUrl}/feedback/doctor/${doctorId}/rating`)
      .pipe(map(response => response.data));
  }
}
