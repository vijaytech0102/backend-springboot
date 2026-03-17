package com.healthcare.service;

import com.healthcare.dto.response.DoctorResponse;
import com.healthcare.entity.Doctor;
import com.healthcare.exception.ResourceNotFoundException;
import com.healthcare.repository.DoctorRepository;
import com.healthcare.repository.FeedbackRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class DoctorService {

    private final DoctorRepository doctorRepository;
    private final FeedbackRepository feedbackRepository;
    private final ModelMapper modelMapper;
    private final PatientService patientService;

    public List<DoctorResponse> getAllDoctors() {
        List<Doctor> doctors = doctorRepository.findAll();
        return doctors.stream()
                .map(this::mapDoctorToResponse)
                .collect(Collectors.toList());
    }

    public List<DoctorResponse> getDoctorsBySpecialisation(String specialisation) {
        List<Doctor> doctors = doctorRepository.findAvailableDoctorsBySpecialisation(specialisation);
        return doctors.stream()
                .map(this::mapDoctorToResponse)
                .collect(Collectors.toList());
    }

    public DoctorResponse getDoctorById(Long doctorId) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with ID: " + doctorId));
        return mapDoctorToResponse(doctor);
    }

    public DoctorResponse getCurrentDoctor() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Long userId = extractUserIdFromEmail(email);
        
        Doctor doctor = doctorRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor profile not found"));
        
        return mapDoctorToResponse(doctor);
    }

    @Transactional
    public DoctorResponse updateDoctorProfile(DoctorResponse updateRequest) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Long userId = extractUserIdFromEmail(email);
        
        Doctor doctor = doctorRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor profile not found"));

        if (updateRequest.getBio() != null) {
            doctor.setBio(updateRequest.getBio());
        }
        if (updateRequest.getAvailabilityDays() != null) {
            doctor.setAvailabilityDays(updateRequest.getAvailabilityDays());
        }
        if (updateRequest.getAvailableFrom() != null) {
            doctor.setAvailableFrom(updateRequest.getAvailableFrom());
        }
        if (updateRequest.getAvailableTo() != null) {
            doctor.setAvailableTo(updateRequest.getAvailableTo());
        }

        Doctor updated = doctorRepository.save(doctor);
        log.info("Doctor profile updated for doctor ID: {}", updated.getDoctorId());

        return mapDoctorToResponse(updated);
    }

    @Transactional
    public void updateDoctorAvailability(Long doctorId, Boolean isAvailable) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found"));
        doctor.setIsAvailable(isAvailable);
        doctorRepository.save(doctor);
        log.info("Doctor {} availability set to: {}", doctorId, isAvailable);
    }

    private DoctorResponse mapDoctorToResponse(Doctor doctor) {
        DoctorResponse response = modelMapper.map(doctor, DoctorResponse.class);
        if (doctor.getUser() != null) {
            response.setUser(modelMapper.map(doctor.getUser(), com.healthcare.dto.response.UserResponse.class));
            response.getUser().setRole(doctor.getUser().getRole().toString());
        }
        Double avgRating = feedbackRepository.getAverageRatingForDoctor(doctor.getDoctorId());
        response.setAverageRating(avgRating != null ? avgRating : 0.0);
        return response;
    }

    private Long extractUserIdFromEmail(String email) {
        // This would need to fetch from database - simplified for now
        return 1L;
    }
}
