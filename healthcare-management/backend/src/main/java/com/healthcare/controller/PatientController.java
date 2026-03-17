package com.healthcare.controller;

import com.healthcare.dto.response.UserResponse;
import com.healthcare.dto.response.ApiResponse;
import com.healthcare.service.PatientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/patients")
@RequiredArgsConstructor
@Tag(name = "Patient Management", description = "Patient endpoints")
@SecurityRequirement(name = "bearerAuth")
@CrossOrigin(origins = {"http://localhost:4200", "http://localhost:3000"})
public class PatientController {

    private final PatientService patientService;

    @GetMapping("/profile")
    @Operation(summary = "Get current patient profile")
    public ResponseEntity<ApiResponse<UserResponse>> getCurrentProfile() {
        UserResponse response = patientService.getCurrentPatient();
        return ResponseEntity.ok(ApiResponse.success("Profile retrieved successfully", response));
    }

    @PutMapping("/profile")
    @Operation(summary = "Update patient profile")
    public ResponseEntity<ApiResponse<UserResponse>> updateProfile(@Valid @RequestBody UserResponse updateRequest) {
        UserResponse response = patientService.updatePatientProfile(updateRequest);
        return ResponseEntity.ok(ApiResponse.success("Profile updated successfully", response));
    }

    @DeleteMapping("/profile")
    @Operation(summary = "Delete patient account")
    public ResponseEntity<ApiResponse<String>> deleteAccount() {
        patientService.deletePatientAccount();
        return ResponseEntity.ok(ApiResponse.success("Account deleted successfully", null));
    }
}
