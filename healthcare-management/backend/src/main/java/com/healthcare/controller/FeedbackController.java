package com.healthcare.controller;

import com.healthcare.dto.request.FeedbackRequest;
import com.healthcare.dto.response.FeedbackResponse;
import com.healthcare.dto.response.ApiResponse;
import com.healthcare.service.FeedbackService;
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
@RequestMapping("/feedback")
@RequiredArgsConstructor
@Tag(name = "Feedback", description = "Feedback and rating endpoints")
@SecurityRequirement(name = "bearerAuth")
@CrossOrigin(origins = {"http://localhost:4200", "http://localhost:3000"})
public class FeedbackController {

    private final FeedbackService feedbackService;

    @PostMapping
    @Operation(summary = "Submit feedback for doctor")
    public ResponseEntity<ApiResponse<FeedbackResponse>> submitFeedback(@Valid @RequestBody FeedbackRequest request) {
        FeedbackResponse response = feedbackService.submitFeedback(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Feedback submitted successfully", response));
    }

    @GetMapping("/doctor/{doctorId}")
    @Operation(summary = "Get all feedback for a doctor")
    public ResponseEntity<ApiResponse<List<FeedbackResponse>>> getDoctorFeedbacks(@PathVariable Long doctorId) {
        List<FeedbackResponse> response = feedbackService.getDoctorFeedbacks(doctorId);
        return ResponseEntity.ok(ApiResponse.success("Doctor feedbacks retrieved successfully", response));
    }

    @GetMapping("/doctor/{doctorId}/rating")
    @Operation(summary = "Get average rating for a doctor")
    public ResponseEntity<ApiResponse<Double>> getDoctorAverageRating(@PathVariable Long doctorId) {
        Double rating = feedbackService.getDoctorAverageRating(doctorId);
        return ResponseEntity.ok(ApiResponse.success("Average rating retrieved successfully", rating));
    }
}
