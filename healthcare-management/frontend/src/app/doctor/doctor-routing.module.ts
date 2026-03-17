import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { DoctorDashboardComponent } from './doctor-dashboard/doctor-dashboard.component';
import { DoctorAppointmentsComponent } from './doctor-appointments/doctor-appointments.component';
import { AddMedicalRecordComponent } from './add-medical-record/add-medical-record.component';
import { DoctorScheduleComponent } from './doctor-schedule/doctor-schedule.component';
import { DoctorProfileComponent } from './doctor-profile/doctor-profile.component';

const routes: Routes = [
  { path: 'dashboard', component: DoctorDashboardComponent },
  { path: 'appointments', component: DoctorAppointmentsComponent },
  { path: 'add-record/:id', component: AddMedicalRecordComponent },
  { path: 'schedule', component: DoctorScheduleComponent },
  { path: 'profile', component: DoctorProfileComponent }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class DoctorRoutingModule { }
