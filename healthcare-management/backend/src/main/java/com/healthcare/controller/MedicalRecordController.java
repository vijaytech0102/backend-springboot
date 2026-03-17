package com.healthcare.controller;

import com.healthcare.dto.request.MedicalRecordRequest;
import com.healthcare.dto.response.MedicalRecordResponse;
import com.healthcare.dto.response.ApiResponse;
import com.healthcare.service.MedicalRecordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/medical-records")
@RequiredArgsConstructor
@Tag(name = "Medical Records", description = "Medical record endpoints")
@SecurityRequirement(name = "bearerAuth")
@CrossOrigin(origins = {"http://localhost:4200", "http://localhost:3000"})
public class MedicalRecordController {

    private final MedicalRecordService medicalRecordService;

    @PostMapping
    @Operation(summary = "Create medical record (Doctor only)")
    public ResponseEntity<ApiResponse<MedicalRecordResponse>> createMedicalRecord(
            @Valid @RequestBody MedicalRecordRequest request) {
        MedicalRecordResponse response = medicalRecordService.createMedicalRecord(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Medical record created successfully", response));
    }

    @GetMapping("/appointment/{appointmentId}")
    @Operation(summary = "Get medical record by appointment ID")
    public ResponseEntity<ApiResponse<MedicalRecordResponse>> getMedicalRecordByAppointmentId(
            @PathVariable Long appointmentId) {
        MedicalRecordResponse response = medicalRecordService.getMedicalRecordByAppointmentId(appointmentId);
        return ResponseEntity.ok(ApiResponse.success("Medical record retrieved successfully", response));
    }

    @GetMapping("/patient")
    @Operation(summary = "Get current patient's medical records")
    public ResponseEntity<ApiResponse<List<MedicalRecordResponse>>> getPatientMedicalRecords() {
        List<MedicalRecordResponse> response = medicalRecordService.getPatientMedicalRecords();
        return ResponseEntity.ok(ApiResponse.success("Patient medical records retrieved successfully", response));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update medical record (Doctor only)")
    public ResponseEntity<ApiResponse<MedicalRecordResponse>> updateMedicalRecord(
            @PathVariable Long id,
            @Valid @RequestBody MedicalRecordRequest request) {
        MedicalRecordResponse response = medicalRecordService.updateMedicalRecord(id, request);
        return ResponseEntity.ok(ApiResponse.success("Medical record updated successfully", response));
    }
}
