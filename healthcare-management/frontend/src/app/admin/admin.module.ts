import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';

import { AdminRoutingModule } from './admin-routing.module';
import { SharedModule } from '../shared/shared.module';

import { AdminDashboardComponent } from './admin-dashboard/admin-dashboard.component';
import { ManageUsersComponent } from './manage-users/manage-users.component';
import { ManageDoctorsComponent } from './manage-doctors/manage-doctors.component';
import { ManageAppointmentsComponent } from './manage-appointments/manage-appointments.component';
import { AdminReportsComponent } from './admin-reports/admin-reports.component';

@NgModule({
  declarations: [
    AdminDashboardComponent,
    ManageUsersComponent,
    ManageDoctorsComponent,
    ManageAppointmentsComponent,
    AdminReportsComponent
  ],
  imports: [
    CommonModule,
    ReactiveFormsModule,
    FormsModule,
    NgbModule,
    AdminRoutingModule,
    SharedModule
  ]
})
export class AdminModule { }
