package com.healthcare.controller;

import com.healthcare.dto.request.AppointmentRequest;
import com.healthcare.dto.response.AppointmentResponse;
import com.healthcare.dto.response.ApiResponse;
import com.healthcare.service.AppointmentService;
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
@RequestMapping("/appointments")
@RequiredArgsConstructor
@Tag(name = "Appointment Management", description = "Appointment endpoints")
@SecurityRequirement(name = "bearerAuth")
@CrossOrigin(origins = {"http://localhost:4200", "http://localhost:3000"})
public class AppointmentController {

    private final AppointmentService appointmentService;

    @PostMapping
    @Operation(summary = "Book new appointment")
    public ResponseEntity<ApiResponse<AppointmentResponse>> bookAppointment(@Valid @RequestBody AppointmentRequest request) {
        AppointmentResponse response = appointmentService.bookAppointment(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Appointment booked successfully", response));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get appointment by ID")
    public ResponseEntity<ApiResponse<AppointmentResponse>> getAppointmentById(@PathVariable Long id) {
        AppointmentResponse response = appointmentService.getAppointmentById(id);
        return ResponseEntity.ok(ApiResponse.success("Appointment retrieved successfully", response));
    }

    @PutMapping("/{id}/confirm")
    @Operation(summary = "Confirm appointment (Doctor only)")
    public ResponseEntity<ApiResponse<AppointmentResponse>> confirmAppointment(@PathVariable Long id) {
        AppointmentResponse response = appointmentService.confirmAppointment(id);
        return ResponseEntity.ok(ApiResponse.success("Appointment confirmed successfully", response));
    }

    @PutMapping("/{id}/cancel")
    @Operation(summary = "Cancel appointment")
    public ResponseEntity<ApiResponse<AppointmentResponse>> cancelAppointment(@PathVariable Long id) {
        AppointmentResponse response = appointmentService.cancelAppointment(id);
        return ResponseEntity.ok(ApiResponse.success("Appointment cancelled successfully", response));
    }

    @PutMapping("/{id}/complete")
    @Operation(summary = "Mark appointment as complete (Doctor only)")
    public ResponseEntity<ApiResponse<AppointmentResponse>> completeAppointment(@PathVariable Long id) {
        AppointmentResponse response = appointmentService.completeAppointment(id);
        return ResponseEntity.ok(ApiResponse.success("Appointment completed successfully", response));
    }

    @GetMapping("/patient/my-appointments")
    @Operation(summary = "Get current patient's appointments")
    public ResponseEntity<ApiResponse<List<AppointmentResponse>>> getPatientAppointments() {
        List<AppointmentResponse> response = appointmentService.getPatientAppointments();
        return ResponseEntity.ok(ApiResponse.success("Patient appointments retrieved successfully", response));
    }

    @GetMapping("/doctor/my-appointments")
    @Operation(summary = "Get current doctor's appointments")
    public ResponseEntity<ApiResponse<List<AppointmentResponse>>> getDoctorAppointments() {
        List<AppointmentResponse> response = appointmentService.getDoctorAppointments();
        return ResponseEntity.ok(ApiResponse.success("Doctor appointments retrieved successfully", response));
    }
}
