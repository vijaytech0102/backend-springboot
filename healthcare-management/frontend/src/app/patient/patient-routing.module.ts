import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { PatientDashboardComponent } from './patient-dashboard/patient-dashboard.component';
import { FindDoctorComponent } from './find-doctor/find-doctor.component';
import { MyAppointmentsComponent } from './my-appointments/my-appointments.component';
import { PatientProfileComponent } from './patient-profile/patient-profile.component';
import { MedicalRecordsComponent } from './medical-records/medical-records.component';
import { PatientBillingComponent } from './patient-billing/patient-billing.component';

const routes: Routes = [
  { path: 'dashboard', component: PatientDashboardComponent },
  { path: 'find-doctor', component: FindDoctorComponent },
  { path: 'appointments', component: MyAppointmentsComponent },
  { path: 'profile', component: PatientProfileComponent },
  { path: 'records', component: MedicalRecordsComponent },
  { path: 'billing', component: PatientBillingComponent }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class PatientRoutingModule { }
