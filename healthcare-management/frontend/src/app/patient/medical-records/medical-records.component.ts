import { Component, OnInit } from '@angular/core';
import { MedicalRecordService } from '../../core/services/medical-record.service';
import { MedicalRecord } from '../../core/models';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-medical-records',
  templateUrl: './medical-records.component.html',
  styleUrls: ['./medical-records.component.scss']
})
export class MedicalRecordsComponent implements OnInit {
  medicalRecords: MedicalRecord[] = [];

  constructor(
    private medicalRecordService: MedicalRecordService,
    private toastr: ToastrService
  ) {}

  ngOnInit(): void {
    this.loadRecords();
  }

  loadRecords(): void {
    this.medicalRecordService.getPatientMedicalRecords().subscribe({
      next: (records) => {
        this.medicalRecords = records;
      },
      error: () => {
        this.toastr.error('Failed to load medical records');
      }
    });
  }
}
