import { Component, OnInit } from '@angular/core';
import { BillingService } from '../../core/services/billing.service';
import { Bill } from '../../core/models';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-patient-billing',
  templateUrl: './patient-billing.component.html',
  styleUrls: ['./patient-billing.component.scss']
})
export class PatientBillingComponent implements OnInit {
  bills: Bill[] = [];
  totalDue = 0;
  totalPaid = 0;

  constructor(
    private billingService: BillingService,
    private toastr: ToastrService
  ) {}

  ngOnInit(): void {
    this.loadBills();
  }

  loadBills(): void {
    this.billingService.getPatientBills().subscribe({
      next: (bills) => {
        this.bills = bills;
        this.calculateTotals();
      },
      error: () => {
        this.toastr.error('Failed to load bills');
      }
    });
  }

  calculateTotals(): void {
    this.totalDue = this.bills
      .filter(b => b.paymentStatus === 'UNPAID')
      .reduce((sum, b) => sum + (b.totalAmount || 0), 0);
    
    this.totalPaid = this.bills
      .filter(b => b.paymentStatus === 'PAID')
      .reduce((sum, b) => sum + (b.totalAmount || 0), 0);
  }

  payBill(billId: number | undefined): void {
    if (!billId) return;
    
    this.billingService.payBill(billId).subscribe({
      next: () => {
        this.toastr.success('Payment successful');
        this.loadBills();
      },
      error: () => {
        this.toastr.error('Payment failed');
      }
    });
  }
}
