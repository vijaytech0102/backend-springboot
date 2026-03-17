import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../core/services/auth.service';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent implements OnInit {
  registerForm!: FormGroup;
  isLoading = false;

  constructor(
    private formBuilder: FormBuilder,
    private router: Router,
    private authService: AuthService,
    private toastr: ToastrService
  ) {}

  ngOnInit(): void {
    this.registerForm = this.formBuilder.group({
      fullName: ['', [Validators.required, Validators.minLength(2)]],
      email: ['', [Validators.required, Validators.email]],
      phone: ['', Validators.required],
      password: ['', [Validators.required, Validators.minLength(6)]],
      role: ['', Validators.required],
      specialisation: [''],
      qualification: [''],
      experienceYears: [''],
      consultationFee: ['']
    });
  }

  isFieldInvalid(field: string): boolean {
    const f = this.registerForm.get(field);
    return !!(f && f.invalid && (f.dirty || f.touched));
  }

  onRoleChange(): void {
    const role = this.registerForm.get('role')?.value;
    const specialisation = this.registerForm.get('specialisation');
    const qualification = this.registerForm.get('qualification');
    const experienceYears = this.registerForm.get('experienceYears');
    const consultationFee = this.registerForm.get('consultationFee');

    if (role === 'DOCTOR') {
      specialisation?.setValidators([Validators.required]);
      qualification?.setValidators([Validators.required]);
      experienceYears?.setValidators([Validators.required]);
      consultationFee?.setValidators([Validators.required]);
    } else {
      specialisation?.clearValidators();
      qualification?.clearValidators();
      experienceYears?.clearValidators();
      consultationFee?.clearValidators();
    }

    specialisation?.updateValueAndValidity();
    qualification?.updateValueAndValidity();
    experienceYears?.updateValueAndValidity();
    consultationFee?.updateValueAndValidity();
  }

  onSubmit(): void {
    if (this.registerForm.invalid) {
      this.toastr.error('Please fill in all required fields correctly');
      return;
    }

    this.isLoading = true;
    const formValue = this.registerForm.value;
    const role = formValue.role;

    this.authService.register(
      formValue.fullName,
      formValue.email,
      formValue.password,
      role,
      formValue.phone,
      role === 'DOCTOR' ? formValue.specialisation : undefined,
      role === 'DOCTOR' ? formValue.qualification : undefined,
      role === 'DOCTOR' ? formValue.experienceYears : undefined,
      role === 'DOCTOR' ? formValue.consultationFee : undefined
    ).subscribe({
      next: (response) => {
        this.isLoading = false;
        this.toastr.success('Registration successful! Logging you in...');
        
        if (role === 'PATIENT') {
          this.router.navigate(['/patient/dashboard']);
        } else if (role === 'DOCTOR') {
          this.router.navigate(['/doctor/dashboard']);
        }
      },
      error: (error) => {
        this.isLoading = false;
        this.toastr.error(error?.error?.message || 'Registration failed');
      }
    });
  }
}
