import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';

import { PatientRoutingModule } from './patient-routing.module';
import { SharedModule } from '../shared/shared.module';

import { PatientDashboardComponent } from './patient-dashboard/patient-dashboard.component';
import { FindDoctorComponent } from './find-doctor/find-doctor.component';
import { MyAppointmentsComponent } from './my-appointments/my-appointments.component';
import { PatientProfileComponent } from './patient-profile/patient-profile.component';
import { MedicalRecordsComponent } from './medical-records/medical-records.component';
import { PatientBillingComponent } from './patient-billing/patient-billing.component';

@NgModule({
  declarations: [
    PatientDashboardComponent,
    FindDoctorComponent,
    MyAppointmentsComponent,
    PatientProfileComponent,
    MedicalRecordsComponent,
    PatientBillingComponent
  ],
  imports: [
    CommonModule,
    ReactiveFormsModule,
    FormsModule,
    NgbModule,
    PatientRoutingModule,
    SharedModule
  ]
})
export class PatientModule { }
