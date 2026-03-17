import { Component, OnInit } from '@angular/core';
import { AppointmentService } from '../../core/services/appointment.service';
import { BillingService } from '../../core/services/billing.service';
import { MedicalRecordService } from '../../core/services/medical-record.service';
import { DoctorService } from '../../core/services/doctor.service';
import { Appointment } from '../../core/models';

@Component({
  selector: 'app-patient-dashboard',
  templateUrl: './patient-dashboard.component.html',
  styleUrls: ['./patient-dashboard.component.scss']
})
export class PatientDashboardComponent implements OnInit {
  appointments: Appointment[] = [];
  upcomingAppointments = 0;
  totalRecords = 0;
  pendingBills = 0;
  totalDoctors = 0;

  constructor(
    private appointmentService: AppointmentService,
    private billingService: BillingService,
    private medicalRecordService: MedicalRecordService,
    private doctorService: DoctorService
  ) {}

  ngOnInit(): void {
    this.loadDashboardData();
  }

  loadDashboardData(): void {
    this.appointmentService.getPatientAppointments().subscribe({
      next: (apts) => {
        this.appointments = apts;
        this.upcomingAppointments = apts.filter(a => a.status === 'PENDING' || a.status === 'CONFIRMED').length;
      }
    });

    this.medicalRecordService.getPatientMedicalRecords().subscribe({
      next: (records) => {
        this.totalRecords = records.length;
      }
    });

    this.billingService.getPatientBills().subscribe({
      next: (bills) => {
        this.pendingBills = bills
          .filter(b => b.paymentStatus === 'UNPAID')
          .reduce((sum, b) => sum + (b.totalAmount || 0), 0);
      }
    });

    this.doctorService.getAllDoctors().subscribe({
      next: (doctors) => {
        this.totalDoctors = doctors.length;
      }
    });
  }

  getStatusColor(status: string): string {
    const colors: { [key: string]: string } = {
      'PENDING': 'warning',
      'CONFIRMED': 'info',
      'COMPLETED': 'success',
      'CANCELLED': 'danger'
    };
    return colors[status] || 'secondary';
  }
}
