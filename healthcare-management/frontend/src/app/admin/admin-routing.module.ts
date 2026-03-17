import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AdminDashboardComponent } from './admin-dashboard/admin-dashboard.component';
import { ManageUsersComponent } from './manage-users/manage-users.component';
import { ManageDoctorsComponent } from './manage-doctors/manage-doctors.component';
import { ManageAppointmentsComponent } from './manage-appointments/manage-appointments.component';
import { AdminReportsComponent } from './admin-reports/admin-reports.component';

const routes: Routes = [
  { path: 'dashboard', component: AdminDashboardComponent },
  { path: 'users', component: ManageUsersComponent },
  { path: 'doctors', component: ManageDoctorsComponent },
  { path: 'appointments', component: ManageAppointmentsComponent },
  { path: 'reports', component: AdminReportsComponent }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class AdminRoutingModule { }
