import { Component, OnInit } from '@angular/core';
import { AuthService } from '../../../core/services/auth.service';

interface MenuItem {
  label: string;
  icon: string;
  path: string;
}

@Component({
  selector: 'app-sidebar',
  templateUrl: './sidebar.component.html',
  styleUrls: ['./sidebar.component.scss']
})
export class SidebarComponent implements OnInit {
  isCollapsed = false;
  menuLinks: MenuItem[] = [];

  constructor(private authService: AuthService) {}

  ngOnInit(): void {
    this.authService.currentUser$.subscribe(user => {
      if (user) {
        this.buildMenuLinks(user.role);
      }
    });
  }

  buildMenuLinks(role: string): void {
    switch (role) {
      case 'PATIENT':
        this.menuLinks = [
          { label: 'Dashboard', icon: 'bi-house', path: '/patient/dashboard' },
          { label: 'Find Doctor', icon: 'bi-search', path: '/patient/find-doctor' },
          { label: 'My Appointments', icon: 'bi-calendar', path: '/patient/appointments' },
          { label: 'Medical Records', icon: 'bi-file-text', path: '/patient/records' },
          { label: 'Billing', icon: 'bi-wallet2', path: '/patient/billing' }
        ];
        break;
      case 'DOCTOR':
        this.menuLinks = [
          { label: 'Dashboard', icon: 'bi-house', path: '/doctor/dashboard' },
          { label: 'Appointments', icon: 'bi-calendar', path: '/doctor/appointments' },
          { label: 'Schedule', icon: 'bi-clock', path: '/doctor/schedule' }
        ];
        break;
      case 'ADMIN':
        this.menuLinks = [
          { label: 'Dashboard', icon: 'bi-house', path: '/admin/dashboard' },
          { label: 'Users', icon: 'bi-people', path: '/admin/users' },
          { label: 'Doctors', icon: 'bi-person-badge', path: '/admin/doctors' },
          { label: 'Appointments', icon: 'bi-calendar', path: '/admin/appointments' },
          { label: 'Reports', icon: 'bi-bar-chart', path: '/admin/reports' }
        ];
        break;
    }
  }

  toggleCollapse(): void {
    this.isCollapsed = !this.isCollapsed;
  }
}
