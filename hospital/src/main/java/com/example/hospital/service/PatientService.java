package com.example.hospital.service;

import com.example.hospital.dto.PatientDTO;
import com.example.hospital.entity.Patient;
import com.example.hospital.repository.PatientRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PatientService {
    
    private final PatientRepository patientRepository;
    private final ModelMapper modelMapper;
    
    public PatientService(PatientRepository patientRepository, ModelMapper modelMapper) {
        this.patientRepository = patientRepository;
        this.modelMapper = modelMapper;
    }
    
    // CREATE - Add a new patient
    public PatientDTO createPatient(PatientDTO patientDTO) {
        Patient patient = modelMapper.map(patientDTO, Patient.class);
        Patient savedPatient = patientRepository.save(patient);
        return modelMapper.map(savedPatient, PatientDTO.class);
    }
    
    // READ - Get all patients
    public List<PatientDTO> getAllPatients() {
        return patientRepository.findAll()
            .stream()
            .map(patient -> modelMapper.map(patient, PatientDTO.class))
            .collect(Collectors.toList());
    }
    
    // READ - Get patient by ID
    public PatientDTO getPatientById(Long id) {
        Patient patient = patientRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Patient not found with id: " + id));
        return modelMapper.map(patient, PatientDTO.class);
    }
    
    // READ - Get patient by email
    public PatientDTO getPatientByEmail(String email) {
        Patient patient = patientRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("Patient not found with email: " + email));
        return modelMapper.map(patient, PatientDTO.class);
    }
    
    // READ - Get patient by insurance number
    public PatientDTO getPatientByInsuranceNumber(String insuranceNumber) {
        Patient patient = patientRepository.findByInsuranceNumber(insuranceNumber)
            .orElseThrow(() -> new RuntimeException("Patient not found with insurance number: " + insuranceNumber));
        return modelMapper.map(patient, PatientDTO.class);
    }
    
    // READ - Get active patients
    public List<PatientDTO> getActivePatients() {
        return patientRepository.findByIsActive(true)
            .stream()
            .map(patient -> modelMapper.map(patient, PatientDTO.class))
            .collect(Collectors.toList());
    }
    
    // READ - Get patients by gender
    public List<PatientDTO> getPatientsByGender(String gender) {
        return patientRepository.findByGender(gender)
            .stream()
            .map(patient -> modelMapper.map(patient, PatientDTO.class))
            .collect(Collectors.toList());
    }
    
    // UPDATE - Update patient information
    public PatientDTO updatePatient(Long id, PatientDTO patientDTO) {
        Patient patient = patientRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Patient not found with id: " + id));
        
        modelMapper.map(patientDTO, patient);
        Patient updatedPatient = patientRepository.save(patient);
        return modelMapper.map(updatedPatient, PatientDTO.class);
    }
    
    // UPDATE - Update patient active status
    public PatientDTO updatePatientStatus(Long id, Boolean isActive) {
        Patient patient = patientRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Patient not found with id: " + id));
        
        patient.setIsActive(isActive);
        Patient updatedPatient = patientRepository.save(patient);
        return modelMapper.map(updatedPatient, PatientDTO.class);
    }
    
    // UPDATE - Update patient medical history
    public PatientDTO updatePatientMedicalHistory(Long id, String medicalHistory) {
        Patient patient = patientRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Patient not found with id: " + id));
        
        patient.setMedicalHistory(medicalHistory);
        Patient updatedPatient = patientRepository.save(patient);
        return modelMapper.map(updatedPatient, PatientDTO.class);
    }
    
    // DELETE - Delete patient by ID
    public void deletePatient(Long id) {
        if (!patientRepository.existsById(id)) {
            throw new RuntimeException("Patient not found with id: " + id);
        }
        patientRepository.deleteById(id);
    }
    
    // DELETE - Delete all patients
    public void deleteAllPatients() {
        patientRepository.deleteAll();
    }
    
    // Check if patient exists
    public boolean patientExists(Long id) {
        return patientRepository.existsById(id);
    }
    
    // Get total count of patients
    public long getTotalPatientsCount() {
        return patientRepository.count();
    }
}
