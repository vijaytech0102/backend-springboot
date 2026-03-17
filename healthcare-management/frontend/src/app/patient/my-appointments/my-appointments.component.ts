import { Component, OnInit } from '@angular/core';
import { AppointmentService } from '../../core/services/appointment.service';
import { Appointment } from '../../core/models';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-my-appointments',
  templateUrl: './my-appointments.component.html',
  styleUrls: ['./my-appointments.component.scss']
})
export class MyAppointmentsComponent implements OnInit {
  appointments: Appointment[] = [];
  filteredAppointments: Appointment[] = [];
  selectedTab = 'all';

  constructor(
    private appointmentService: AppointmentService,
    private toastr: ToastrService
  ) {}

  ngOnInit(): void {
    this.loadAppointments();
  }

  loadAppointments(): void {
    this.appointmentService.getPatientAppointments().subscribe({
      next: (apts) => {
        this.appointments = apts;
        this.filterAppointments();
      },
      error: () => {
        this.toastr.error('Failed to load appointments');
      }
    });
  }

  filterAppointments(): void {
    const now = new Date();
    this.filteredAppointments = this.appointments.filter(apt => {
      const aptDate = new Date(apt.appointmentDate);
      
      switch (this.selectedTab) {
        case 'upcoming':
          return aptDate > now && (apt.status === 'PENDING' || apt.status === 'CONFIRMED');
        case 'completed':
          return apt.status === 'COMPLETED';
        case 'cancelled':
          return apt.status === 'CANCELLED';
        default:
          return true;
      }
    });
  }

  cancelAppointment(appointmentId: number | undefined): void {
    if (!appointmentId) return;
    
    this.appointmentService.cancelAppointment(appointmentId).subscribe({
      next: () => {
        this.toastr.success('Appointment cancelled successfully');
        this.loadAppointments();
      },
      error: () => {
        this.toastr.error('Failed to cancel appointment');
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
