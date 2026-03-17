import { Injectable } from '@angular/core';
import { HttpRequest, HttpHandler, HttpEvent, HttpInterceptor, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { AuthService } from '../services/auth.service';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';

@Injectable()
export class ErrorInterceptor implements HttpInterceptor {

  constructor(private authService: AuthService, private router: Router, private toastr: ToastrService) { }

  intercept(request: HttpRequest<unknown>, next: HttpHandler): Observable<HttpEvent<unknown>> {
    return next.handle(request).pipe(catchError((error: HttpErrorResponse) => {
      let errorMessage = 'An error occurred';

      if (error.error instanceof ErrorEvent) {
        errorMessage = `Error: ${error.error.message}`;
      } else {
        errorMessage = error.error?.message || `Error: ${error.status} ${error.statusText}`;
      }

      if (error.status === 401) {
        this.authService.logout().subscribe();
        this.router.navigate(['/login']);
        this.toastr.error('Session expired. Please login again');
      } else if (error.status === 403) {
        this.toastr.error('You do not have permission to access this resource');
      } else if (error.status === 404) {
        this.toastr.error('Resource not found');
      } else if (error.status === 409) {
        this.toastr.error(errorMessage || 'Conflict: This resource already exists');
      } else if (error.status >= 500) {
        this.toastr.error('Server error. Please try again later');
      } else {
        this.toastr.error(errorMessage);
      }

      console.error('Error:', error);
      return throwError(() => error);
    }));
  }
}
