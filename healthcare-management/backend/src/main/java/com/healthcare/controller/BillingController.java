package com.healthcare.controller;

import com.healthcare.dto.response.BillResponse;
import com.healthcare.dto.response.ApiResponse;
import com.healthcare.service.BillingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/billing")
@RequiredArgsConstructor
@Tag(name = "Billing", description = "Billing and payment endpoints")
@SecurityRequirement(name = "bearerAuth")
@CrossOrigin(origins = {"http://localhost:4200", "http://localhost:3000"})
public class BillingController {

    private final BillingService billingService;

    @GetMapping("/appointment/{appointmentId}")
    @Operation(summary = "Get bill by appointment ID")
    public ResponseEntity<ApiResponse<BillResponse>> getBillByAppointmentId(@PathVariable Long appointmentId) {
        BillResponse response = billingService.getBillByAppointmentId(appointmentId);
        return ResponseEntity.ok(ApiResponse.success("Bill retrieved successfully", response));
    }

    @GetMapping("/patient")
    @Operation(summary = "Get current patient's bills")
    public ResponseEntity<ApiResponse<List<BillResponse>>> getPatientBills() {
        List<BillResponse> response = billingService.getPatientBills();
        return ResponseEntity.ok(ApiResponse.success("Patient bills retrieved successfully", response));
    }

    @PutMapping("/{billId}/pay")
    @Operation(summary = "Mark bill as paid")
    public ResponseEntity<ApiResponse<BillResponse>> payBill(@PathVariable Long billId) {
        BillResponse response = billingService.payBill(billId);
        return ResponseEntity.ok(ApiResponse.success("Bill marked as paid successfully", response));
    }

    @GetMapping("/admin/all")
    @Operation(summary = "Get all bills (Admin only)")
    public ResponseEntity<ApiResponse<List<BillResponse>>> getAllBills() {
        List<BillResponse> response = billingService.getAllBills();
        return ResponseEntity.ok(ApiResponse.success("All bills retrieved successfully", response));
    }
}
