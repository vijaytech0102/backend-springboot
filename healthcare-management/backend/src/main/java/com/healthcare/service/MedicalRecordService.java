package com.healthcare.service;

import com.healthcare.dto.request.MedicalRecordRequest;
import com.healthcare.dto.response.MedicalRecordResponse;
import com.healthcare.entity.Appointment;
import com.healthcare.entity.MedicalRecord;
import com.healthcare.exception.ResourceNotFoundException;
import com.healthcare.repository.AppointmentRepository;
import com.healthcare.repository.MedicalRecordRepository;
import com.healthcare.repository.UserRepository;
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
public class MedicalRecordService {

    private final MedicalRecordRepository medicalRecordRepository;
    private final AppointmentRepository appointmentRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Transactional
    public MedicalRecordResponse createMedicalRecord(MedicalRecordRequest request) {
        Appointment appointment = appointmentRepository.findById(request.getAppointmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found"));

        MedicalRecord medicalRecord = MedicalRecord.builder()
                .appointment(appointment)
                .diagnosis(request.getDiagnosis())
                .prescription(request.getPrescription())
                .labTests(request.getLabTests())
                .followUpDate(request.getFollowUpDate())
                .doctorNotes(request.getDoctorNotes())
                .build();

        MedicalRecord saved = medicalRecordRepository.save(medicalRecord);
        log.info("Medical record created for appointment: {}", request.getAppointmentId());

        return mapMedicalRecordToResponse(saved);
    }

    public MedicalRecordResponse getMedicalRecordByAppointmentId(Long appointmentId) {
        MedicalRecord record = medicalRecordRepository.findByAppointmentAppointmentId(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Medical record not found"));
        return mapMedicalRecordToResponse(record);
    }

    public List<MedicalRecordResponse> getPatientMedicalRecords() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Long userId = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found"))
                .getUserId();

        List<MedicalRecord> records = medicalRecordRepository.findByPatientUserId(userId);
        return records.stream()
                .map(this::mapMedicalRecordToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public MedicalRecordResponse updateMedicalRecord(Long recordId, MedicalRecordRequest request) {
        MedicalRecord record = medicalRecordRepository.findById(recordId)
                .orElseThrow(() -> new ResourceNotFoundException("Medical record not found"));

        if (request.getDiagnosis() != null) {
            record.setDiagnosis(request.getDiagnosis());
        }
        if (request.getPrescription() != null) {
            record.setPrescription(request.getPrescription());
        }
        if (request.getLabTests() != null) {
            record.setLabTests(request.getLabTests());
        }
        if (request.getFollowUpDate() != null) {
            record.setFollowUpDate(request.getFollowUpDate());
        }

        MedicalRecord updated = medicalRecordRepository.save(record);
        log.info("Medical record updated: {}", recordId);

        return mapMedicalRecordToResponse(updated);
    }

    private MedicalRecordResponse mapMedicalRecordToResponse(MedicalRecord record) {
        MedicalRecordResponse response = modelMapper.map(record, MedicalRecordResponse.class);
        if (record.getAppointment() != null) {
            response.setAppointment(modelMapper.map(record.getAppointment(), com.healthcare.dto.response.AppointmentResponse.class));
        }
        return response;
    }
}
