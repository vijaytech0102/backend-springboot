package com.healthcare.service;

import com.healthcare.dto.request.AppointmentRequest;
import com.healthcare.dto.response.AppointmentResponse;
import com.healthcare.entity.Appointment;
import com.healthcare.entity.Doctor;
import com.healthcare.entity.User;
import com.healthcare.entity.Bill;
import com.healthcare.enums.AppointmentStatus;
import com.healthcare.exception.BadRequestException;
import com.healthcare.exception.ResourceNotFoundException;
import com.healthcare.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final DoctorRepository doctorRepository;
    private final UserRepository userRepository;
    private final BillRepository billRepository;
    private final ModelMapper modelMapper;

    @Transactional
    public AppointmentResponse bookAppointment(AppointmentRequest request) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User patient = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found"));

        Doctor doctor = doctorRepository.findById(request.getDoctorId())
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found"));

        if (request.getAppointmentDate().isBefore(LocalDate.now())) {
            throw new BadRequestException("Appointment date cannot be in the past");
        }

        List<Appointment> conflictingAppointments = appointmentRepository.appointmentRepository
                .findAll().stream()
                .filter(a -> a.getDoctor().getDoctorId().equals(doctor.getDoctorId())
                        && a.getAppointmentDate().equals(request.getAppointmentDate())
                        && a.getAppointmentTime().equals(request.getAppointmentTime())
                        && (a.getStatus() == AppointmentStatus.CONFIRMED || a.getStatus() == AppointmentStatus.PENDING))
                .collect(Collectors.toList());

        if (!conflictingAppointments.isEmpty()) {
            throw new BadRequestException("Time slot not available for selected doctor");
        }

        Appointment appointment = Appointment.builder()
                .patient(patient)
                .doctor(doctor)
                .appointmentDate(request.getAppointmentDate())
                .appointmentTime(request.getAppointmentTime())
                .reason(request.getReason())
                .notes(request.getNotes())
                .status(AppointmentStatus.PENDING)
                .build();

        Appointment saved = appointmentRepository.save(appointment);
        log.info("Appointment booked with ID: {} for patient: {}", saved.getAppointmentId(), patient.getUserId());

        return mapAppointmentToResponse(saved);
    }

    @Transactional
    public AppointmentResponse confirmAppointment(Long appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found"));

        appointment.setStatus(AppointmentStatus.CONFIRMED);
        Appointment updated = appointmentRepository.save(appointment);
        log.info("Appointment confirmed: {}", appointmentId);

        return mapAppointmentToResponse(updated);
    }

    @Transactional
    public AppointmentResponse cancelAppointment(Long appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found"));

        if (appointment.getStatus() == AppointmentStatus.COMPLETED) {
            throw new BadRequestException("Cannot cancel a completed appointment");
        }

        appointment.setStatus(AppointmentStatus.CANCELLED);
        Appointment updated = appointmentRepository.save(appointment);
        log.info("Appointment cancelled: {}", appointmentId);

        return mapAppointmentToResponse(updated);
    }

    @Transactional
    public AppointmentResponse completeAppointment(Long appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found"));

        if (appointment.getStatus() != AppointmentStatus.CONFIRMED) {
            throw new BadRequestException("Only confirmed appointments can be marked as completed");
        }

        appointment.setStatus(AppointmentStatus.COMPLETED);
        Appointment updated = appointmentRepository.save(appointment);

        Bill bill = Bill.builder()
                .appointment(updated)
                .consultationFee(updated.getDoctor().getConsultationFee())
                .additionalCharges(BigDecimal.ZERO)
                .discount(BigDecimal.ZERO)
                .totalAmount(updated.getDoctor().getConsultationFee())
                .paymentStatus(com.healthcare.enums.PaymentStatus.UNPAID)
                .build();
        billRepository.save(bill);

        log.info("Appointment completed and bill generated: {}", appointmentId);
        return mapAppointmentToResponse(updated);
    }

    public List<AppointmentResponse> getPatientAppointments() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User patient = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found"));

        List<Appointment> appointments = appointmentRepository.findPatientAppointmentsSorted(patient.getUserId());
        return appointments.stream()
                .map(this::mapAppointmentToResponse)
                .collect(Collectors.toList());
    }

    public List<AppointmentResponse> getDoctorAppointments() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User doctor = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found"));

        Doctor doctorEntity = doctorRepository.findByUserId(doctor.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Doctor profile not found"));

        List<Appointment> appointments = appointmentRepository.findDoctorAppointmentsSorted(doctorEntity.getDoctorId());
        return appointments.stream()
                .map(this::mapAppointmentToResponse)
                .collect(Collectors.toList());
    }

    public AppointmentResponse getAppointmentById(Long appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found"));
        return mapAppointmentToResponse(appointment);
    }

    private AppointmentResponse mapAppointmentToResponse(Appointment appointment) {
        AppointmentResponse response = modelMapper.map(appointment, AppointmentResponse.class);
        response.setStatus(appointment.getStatus().toString());
        if (appointment.getPatient() != null) {
            response.setPatient(modelMapper.map(appointment.getPatient(), com.healthcare.dto.response.UserResponse.class));
        }
        if (appointment.getDoctor() != null && appointment.getDoctor().getUser() != null) {
            response.setDoctor(modelMapper.map(appointment.getDoctor(), com.healthcare.dto.response.DoctorResponse.class));
        }
        return response;
    }
}
