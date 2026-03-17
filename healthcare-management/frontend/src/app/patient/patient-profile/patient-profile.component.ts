import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { PatientService } from '../../core/services/patient.service';
import { AuthService } from '../../core/services/auth.service';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-patient-profile',
  templateUrl: './patient-profile.component.html',
  styleUrls: ['./patient-profile.component.scss']
})
export class PatientProfileComponent implements OnInit {
  profileForm!: FormGroup;
  isLoading = false;

  constructor(
    private formBuilder: FormBuilder,
    private patientService: PatientService,
    private authService: AuthService,
    private router: Router,
    private toastr: ToastrService
  ) {}

  ngOnInit(): void {
    this.initForm();
    this.loadProfile();
  }

  initForm(): void {
    this.profileForm = this.formBuilder.group({
      fullName: ['', Validators.required],
      email: [''],
      phone: [''],
      dateOfBirth: [''],
      gender: [''],
      address: ['']
    });
  }

  loadProfile(): void {
    this.patientService.getCurrentProfile().subscribe({
      next: (user) => {
        this.profileForm.patchValue(user);
      },
      error: () => {
        this.toastr.error('Failed to load profile');
      }
    });
  }

  updateProfile(): void {
    if (this.profileForm.invalid) {
      return;
    }

    this.isLoading = true;
    this.patientService.updateProfile(this.profileForm.value).subscribe({
      next: () => {
        this.isLoading = false;
        this.toastr.success('Profile updated successfully');
      },
      error: () => {
        this.isLoading = false;
        this.toastr.error('Failed to update profile');
      }
    });
  }

  deleteAccount(): void {
    if (confirm('Are you sure you want to delete your account? This action cannot be undone.')) {
      this.patientService.deleteAccount().subscribe({
        next: () => {
          this.toastr.success('Account deleted successfully');
          this.authService.logout();
        },
        error: () => {
          this.toastr.error('Failed to delete account');
        }
      });
    }
  }
}
