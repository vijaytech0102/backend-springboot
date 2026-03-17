package com.example.hospital.controller;

import com.example.hospital.dto.PatientDTO;
import com.example.hospital.service.PatientService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/patients")
@CrossOrigin(origins = "*", maxAge = 3600)
public class PatientController {
    
    private final PatientService patientService;
    
    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }
    
    // CREATE - Add a new patient
    @PostMapping
    public ResponseEntity<PatientDTO> createPatient(@RequestBody PatientDTO patientDTO) {
        PatientDTO createdPatient = patientService.createPatient(patientDTO);
        return new ResponseEntity<>(createdPatient, HttpStatus.CREATED);
    }
    
    // READ - Get all patients
    @GetMapping
    public ResponseEntity<List<PatientDTO>> getAllPatients() {
        List<PatientDTO> patients = patientService.getAllPatients();
        return new ResponseEntity<>(patients, HttpStatus.OK);
    }
    
    // READ - Get patient by ID
    @GetMapping("/{id}")
    public ResponseEntity<PatientDTO> getPatientById(@PathVariable Long id) {
        PatientDTO patient = patientService.getPatientById(id);
        return new ResponseEntity<>(patient, HttpStatus.OK);
    }
    
    // READ - Get patient by email
    @GetMapping("/email/{email}")
    public ResponseEntity<PatientDTO> getPatientByEmail(@PathVariable String email) {
        PatientDTO patient = patientService.getPatientByEmail(email);
        return new ResponseEntity<>(patient, HttpStatus.OK);
    }
    
    // READ - Get patient by insurance number
    @GetMapping("/insurance/{insuranceNumber}")
    public ResponseEntity<PatientDTO> getPatientByInsuranceNumber(@PathVariable String insuranceNumber) {
        PatientDTO patient = patientService.getPatientByInsuranceNumber(insuranceNumber);
        return new ResponseEntity<>(patient, HttpStatus.OK);
    }
    
    // READ - Get active patients
    @GetMapping("/active")
    public ResponseEntity<List<PatientDTO>> getActivePatients() {
        List<PatientDTO> patients = patientService.getActivePatients();
        return new ResponseEntity<>(patients, HttpStatus.OK);
    }
    
    // READ - Get patients by gender
    @GetMapping("/gender/{gender}")
    public ResponseEntity<List<PatientDTO>> getPatientsByGender(@PathVariable String gender) {
        List<PatientDTO> patients = patientService.getPatientsByGender(gender);
        return new ResponseEntity<>(patients, HttpStatus.OK);
    }
    
    // UPDATE - Update patient information
    @PutMapping("/{id}")
    public ResponseEntity<PatientDTO> updatePatient(
            @PathVariable Long id,
            @RequestBody PatientDTO patientDTO) {
        PatientDTO updatedPatient = patientService.updatePatient(id, patientDTO);
        return new ResponseEntity<>(updatedPatient, HttpStatus.OK);
    }
    
    // UPDATE - Update patient active status
    @PatchMapping("/{id}/status")
    public ResponseEntity<PatientDTO> updatePatientStatus(
            @PathVariable Long id,
            @RequestParam Boolean isActive) {
        PatientDTO updatedPatient = patientService.updatePatientStatus(id, isActive);
        return new ResponseEntity<>(updatedPatient, HttpStatus.OK);
    }
    
    // UPDATE - Update patient medical history
    @PatchMapping("/{id}/medical-history")
    public ResponseEntity<PatientDTO> updatePatientMedicalHistory(
            @PathVariable Long id,
            @RequestBody String medicalHistory) {
        PatientDTO updatedPatient = patientService.updatePatientMedicalHistory(id, medicalHistory);
        return new ResponseEntity<>(updatedPatient, HttpStatus.OK);
    }
    
    // DELETE - Delete patient by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePatient(@PathVariable Long id) {
        patientService.deletePatient(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    
    // DELETE - Delete all patients
    @DeleteMapping
    public ResponseEntity<Void> deleteAllPatients() {
        patientService.deleteAllPatients();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    
    // Check if patient exists
    @GetMapping("/exists/{id}")
    public ResponseEntity<Boolean> patientExists(@PathVariable Long id) {
        boolean exists = patientService.patientExists(id);
        return new ResponseEntity<>(exists, HttpStatus.OK);
    }
    
    // Get total count of patients
    @GetMapping("/count")
    public ResponseEntity<Long> getTotalPatientsCount() {
        long count = patientService.getTotalPatientsCount();
        return new ResponseEntity<>(count, HttpStatus.OK);
    }
}
