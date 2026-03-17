import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AuthGuard } from './core/guards/auth.guard';
import { RoleGuard } from './core/guards/role.guard';
import { LoginComponent } from './auth/login/login.component';
import { RegisterComponent } from './auth/register/register.component';

const routes: Routes = [
  { path: '', redirectTo: '/login', pathMatch: 'full' },
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  {
    path: 'patient',
    canActivate: [AuthGuard, RoleGuard],
    data: { role: 'PATIENT' },
    children: [
      { path: 'dashboard', loadChildren: () => import('./patient/patient.module').then(m => m.PatientModule) },
      { path: 'find-doctor', loadChildren: () => import('./patient/patient.module').then(m => m.PatientModule) },
      { path: 'appointments', loadChildren: () => import('./patient/patient.module').then(m => m.PatientModule) },
      { path: 'records', loadChildren: () => import('./patient/patient.module').then(m => m.PatientModule) },
      { path: 'billing', loadChildren: () => import('./patient/patient.module').then(m => m.PatientModule) }
    ]
  },
  {
    path: 'doctor',
    canActivate: [AuthGuard, RoleGuard],
    data: { role: 'DOCTOR' },
    children: [
      { path: 'dashboard', loadChildren: () => import('./doctor/doctor.module').then(m => m.DoctorModule) },
      { path: 'appointments', loadChildren: () => import('./doctor/doctor.module').then(m => m.DoctorModule) },
      { path: 'add-record/:id', loadChildren: () => import('./doctor/doctor.module').then(m => m.DoctorModule) },
      { path: 'schedule', loadChildren: () => import('./doctor/doctor.module').then(m => m.DoctorModule) }
    ]
  },
  {
    path: 'admin',
    canActivate: [AuthGuard, RoleGuard],
    data: { role: 'ADMIN' },
    children: [
      { path: 'dashboard', loadChildren: () => import('./admin/admin.module').then(m => m.AdminModule) },
      { path: 'users', loadChildren: () => import('./admin/admin.module').then(m => m.AdminModule) },
      { path: 'doctors', loadChildren: () => import('./admin/admin.module').then(m => m.AdminModule) },
      { path: 'appointments', loadChildren: () => import('./admin/admin.module').then(m => m.AdminModule) },
      { path: 'reports', loadChildren: () => import('./admin/admin.module').then(m => m.AdminModule) }
    ]
  },
  { path: '**', redirectTo: '/login' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
