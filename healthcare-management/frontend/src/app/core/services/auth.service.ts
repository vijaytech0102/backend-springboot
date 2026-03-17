import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { BehaviorSubject, Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { environment } from '@environments/environment';
import { AuthResponse, User, ApiResponse } from '../models';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private currentUserSubject: BehaviorSubject<User | null>;
  public currentUser$: Observable<User | null>;
  private tokenKey = 'healthcare_token';

  constructor(private http: HttpClient, private router: Router) {
    this.currentUserSubject = new BehaviorSubject<User | null>(this.getUserFromStorage());
    this.currentUser$ = this.currentUserSubject.asObservable();
  }

  public get currentUserValue(): User | null {
    return this.currentUserSubject.value;
  }

  register(fullName: string, email: string, password: string, role: string, phone?: string, 
    specialisation?: string, qualification?: string, experienceYears?: number, consultationFee?: number): Observable<AuthResponse> {
    const body: any = { fullName, email, password, role, phone };
    if (role === 'DOCTOR') {
      body.specialisation = specialisation;
      body.qualification = qualification;
      body.experienceYears = experienceYears;
      body.consultationFee = consultationFee;
    }
    return this.http.post<ApiResponse<AuthResponse>>(`${environment.apiUrl}/auth/register`, body)
      .pipe(map(response => {
        if (response.data && response.data.access_token) {
          this.setToken(response.data.access_token);
          this.currentUserSubject.next(response.data.user);
        }
        return response.data;
      }));
  }

  login(email: string, password: string): Observable<AuthResponse> {
    return this.http.post<ApiResponse<AuthResponse>>(`${environment.apiUrl}/auth/login`, { email, password })
      .pipe(map(response => {
        if (response.data && response.data.access_token) {
          this.setToken(response.data.access_token);
          this.currentUserSubject.next(response.data.user);
        }
        return response.data;
      }));
  }

  logout(): Observable<any> {
    this.clearToken();
    this.currentUserSubject.next(null);
    this.router.navigate(['/login']);
    return new Observable(observer => observer.complete());
  }

  isLoggedIn(): boolean {
    return !!this.getToken();
  }

  getToken(): string | null {
    return localStorage.getItem(this.tokenKey);
  }

  setToken(token: string): void {
    localStorage.setItem(this.tokenKey, token);
  }

  clearToken(): void {
    localStorage.removeItem(this.tokenKey);
  }

  private getUserFromStorage(): User | null {
    const token = this.getToken();
    if (token) {
      try {
        const payload = JSON.parse(atob(token.split('.')[1]));
        const user: User = {
          userId: parseInt(payload.sub),
          email: payload.email,
          role: payload.roles[0]?.authority.replace('ROLE_', '') || '',
          fullName: '',
          isActive: true
        };
        return user;
      } catch {
        return null;
      }
    }
    return null;
  }
}
