import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { DoctorService } from '../../core/services/doctor.service';
import { AppointmentService } from '../../core/services/appointment.service';
import { Doctor, Appointment } from '../../core/models';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-find-doctor',
  templateUrl: './find-doctor.component.html',
  styleUrls: ['./find-doctor.component.scss']
})
export class FindDoctorComponent implements OnInit {
  doctors: Doctor[] = [];
  filteredDoctors: Doctor[] = [];
  searchQuery = '';
  selectedSpecialisation = '';
  selectedDoctor: Doctor | null = null;
  showBookingModal = false;
  isBooking = false;
  bookingForm!: FormGroup;

  constructor(
    private doctorService: DoctorService,
    private appointmentService: AppointmentService,
    private formBuilder: FormBuilder,
    private toastr: ToastrService
  ) {}

  ngOnInit(): void {
    this.bookingForm = this.formBuilder.group({
      appointmentDate: ['', Validators.required],
      appointmentTime: ['', Validators.required],
      reason: ['', Validators.required],
      notes: ['']
    });

    this.loadDoctors();
  }

  loadDoctors(): void {
    this.doctorService.getAllDoctors().subscribe({
      next: (doctors) => {
        this.doctors = doctors;
        this.filteredDoctors = doctors;
      },
      error: (error) => {
        this.toastr.error('Failed to load doctors');
      }
    });
  }

  filterDoctors(): void {
    this.filteredDoctors = this.doctors.filter(doctor => {
      const matchName = doctor.user.fullName.toLowerCase().includes(this.searchQuery.toLowerCase());
      const matchSpec = !this.selectedSpecialisation || doctor.specialisation === this.selectedSpecialisation;
      return matchName && matchSpec;
    });
  }

  bookAppointment(doctor: Doctor): void {
    this.selectedDoctor = doctor;
    this.showBookingModal = true;
  }

  closeModal(): void {
    this.showBookingModal = false;
    this.selectedDoctor = null;
    this.bookingForm.reset();
  }

  submitBooking(): void {
    if (this.bookingForm.invalid || !this.selectedDoctor) {
      return;
    }

    this.isBooking = true;
    const appointment: Appointment = {
      doctorId: this.selectedDoctor.doctorId,
      ...this.bookingForm.value,
      appointmentDate: new Date(this.bookingForm.value.appointmentDate).toISOString().split('T')[0]
    };

    this.appointmentService.bookAppointment(appointment).subscribe({
      next: () => {
        this.isBooking = false;
        this.toastr.success('Appointment booked successfully!');
        this.closeModal();
      },
      error: (error) => {
        this.isBooking = false;
        this.toastr.error(error?.error?.message || 'Failed to book appointment');
      }
    });
  }
}
