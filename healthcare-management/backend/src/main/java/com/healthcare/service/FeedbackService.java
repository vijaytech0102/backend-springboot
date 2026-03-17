package com.healthcare.service;

import com.healthcare.dto.request.FeedbackRequest;
import com.healthcare.dto.response.FeedbackResponse;
import com.healthcare.entity.Feedback;
import com.healthcare.entity.Appointment;
import com.healthcare.entity.Doctor;
import com.healthcare.entity.User;
import com.healthcare.exception.BadRequestException;
import com.healthcare.exception.ResourceNotFoundException;
import com.healthcare.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final AppointmentRepository appointmentRepository;
    private final DoctorRepository doctorRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Transactional
    public FeedbackResponse submitFeedback(FeedbackRequest request) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User patient = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found"));

        Appointment appointment = appointmentRepository.findById(request.getAppointmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found"));

        if (!appointment.getPatient().getUserId().equals(patient.getUserId())) {
            throw new BadRequestException("Can only submit feedback for your own appointments");
        }

        Doctor doctor = doctorRepository.findById(request.getDoctorId())
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found"));

        if (request.getRating() < 1 || request.getRating() > 5) {
            throw new BadRequestException("Rating must be between 1 and 5");
        }

        Feedback feedback = Feedback.builder()
                .patient(patient)
                .doctor(doctor)
                .appointment(appointment)
                .rating(request.getRating())
                .comment(request.getComment())
                .build();

        Feedback saved = feedbackRepository.save(feedback);
        log.info("Feedback submitted for doctor: {} by patient: {}", request.getDoctorId(), patient.getUserId());

        return mapFeedbackToResponse(saved);
    }

    public List<FeedbackResponse> getDoctorFeedbacks(Long doctorId) {
        doctorRepository.findById(doctorId)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found"));

        List<Feedback> feedbacks = feedbackRepository.findDoctorFeedbacksSorted(doctorId);
        return feedbacks.stream()
                .map(this::mapFeedbackToResponse)
                .collect(Collectors.toList());
    }

    public Double getDoctorAverageRating(Long doctorId) {
        doctorRepository.findById(doctorId)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found"));

        Double rating = feedbackRepository.getAverageRatingForDoctor(doctorId);
        return rating != null ? rating : 0.0;
    }

    private FeedbackResponse mapFeedbackToResponse(Feedback feedback) {
        FeedbackResponse response = modelMapper.map(feedback, FeedbackResponse.class);
        if (feedback.getPatient() != null) {
            response.setPatient(modelMapper.map(feedback.getPatient(), com.healthcare.dto.response.UserResponse.class));
        }
        if (feedback.getDoctor() != null) {
            response.setDoctor(modelMapper.map(feedback.getDoctor(), com.healthcare.dto.response.DoctorResponse.class));
        }
        return response;
    }
}
