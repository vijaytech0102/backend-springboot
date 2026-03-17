package com.healthcare.controller;

import com.healthcare.dto.response.DoctorResponse;
import com.healthcare.dto.response.ApiResponse;
import com.healthcare.service.DoctorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/doctors")
@RequiredArgsConstructor
@Tag(name = "Doctor Management", description = "Doctor endpoints")
@CrossOrigin(origins = {"http://localhost:4200", "http://localhost:3000"})
public class DoctorController {

    private final DoctorService doctorService;

    @GetMapping
    @Operation(summary = "Get all doctors", description = "Get all doctors with optional specialisation filter")
    public ResponseEntity<ApiResponse<List<DoctorResponse>>> getAllDoctors(
            @RequestParam(required = false) String specialisation) {
        List<DoctorResponse> doctors = specialisation != null 
                ? doctorService.getDoctorsBySpecialisation(specialisation)
                : doctorService.getAllDoctors();
        return ResponseEntity.ok(ApiResponse.success("Doctors retrieved successfully", doctors));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get doctor by ID")
    public ResponseEntity<ApiResponse<DoctorResponse>> getDoctorById(@PathVariable Long id) {
        DoctorResponse doctor = doctorService.getDoctorById(id);
        return ResponseEntity.ok(ApiResponse.success("Doctor retrieved successfully", doctor));
    }

    @GetMapping("/profile")
    @Operation(summary = "Get current doctor profile")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<ApiResponse<DoctorResponse>> getCurrentProfile() {
        DoctorResponse doctor = doctorService.getCurrentDoctor();
        return ResponseEntity.ok(ApiResponse.success("Doctor profile retrieved successfully", doctor));
    }

    @PutMapping("/profile")
    @Operation(summary = "Update doctor profile")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<ApiResponse<DoctorResponse>> updateProfile(@Valid @RequestBody DoctorResponse updateRequest) {
        DoctorResponse doctor = doctorService.updateDoctorProfile(updateRequest);
        return ResponseEntity.ok(ApiResponse.success("Doctor profile updated successfully", doctor));
    }

    @PutMapping("/{id}/availability")
    @Operation(summary = "Update doctor availability")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<ApiResponse<String>> updateAvailability(@PathVariable Long id, @RequestParam Boolean available) {
        doctorService.updateDoctorAvailability(id, available);
        return ResponseEntity.ok(ApiResponse.success("Availability updated successfully", null));
    }
}
