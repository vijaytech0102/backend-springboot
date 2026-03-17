package com.example.hospital.controller;

import com.example.hospital.dto.DoctorDTO;
import com.example.hospital.service.DoctorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/doctors")
@CrossOrigin(origins = "*", maxAge = 3600)
public class DoctorController {
    
    private final DoctorService doctorService;
    
    public DoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }
    
    // CREATE - Add a new doctor
    @PostMapping
    public ResponseEntity<DoctorDTO> createDoctor(@RequestBody DoctorDTO doctorDTO) {
        DoctorDTO createdDoctor = doctorService.createDoctor(doctorDTO);
        return new ResponseEntity<>(createdDoctor, HttpStatus.CREATED);
    }
    
    // READ - Get all doctors
    @GetMapping
    public ResponseEntity<List<DoctorDTO>> getAllDoctors() {
        List<DoctorDTO> doctors = doctorService.getAllDoctors();
        return new ResponseEntity<>(doctors, HttpStatus.OK);
    }
    
    // READ - Get doctor by ID
    @GetMapping("/{id}")
    public ResponseEntity<DoctorDTO> getDoctorById(@PathVariable Long id) {
        DoctorDTO doctor = doctorService.getDoctorById(id);
        return new ResponseEntity<>(doctor, HttpStatus.OK);
    }
    
    // READ - Get doctor by email
    @GetMapping("/email/{email}")
    public ResponseEntity<DoctorDTO> getDoctorByEmail(@PathVariable String email) {
        DoctorDTO doctor = doctorService.getDoctorByEmail(email);
        return new ResponseEntity<>(doctor, HttpStatus.OK);
    }
    
    // READ - Get doctor by license number
    @GetMapping("/license/{licenseNumber}")
    public ResponseEntity<DoctorDTO> getDoctorByLicenseNumber(@PathVariable String licenseNumber) {
        DoctorDTO doctor = doctorService.getDoctorByLicenseNumber(licenseNumber);
        return new ResponseEntity<>(doctor, HttpStatus.OK);
    }
    
    // READ - Get doctors by specialization
    @GetMapping("/specialization/{specialization}")
    public ResponseEntity<List<DoctorDTO>> getDoctorsBySpecialization(
            @PathVariable String specialization) {
        List<DoctorDTO> doctors = doctorService.getDoctorsBySpecialization(specialization);
        return new ResponseEntity<>(doctors, HttpStatus.OK);
    }
    
    // READ - Get available doctors
    @GetMapping("/available")
    public ResponseEntity<List<DoctorDTO>> getAvailableDoctors() {
        List<DoctorDTO> doctors = doctorService.getAvailableDoctors();
        return new ResponseEntity<>(doctors, HttpStatus.OK);
    }
    
    // UPDATE - Update doctor information
    @PutMapping("/{id}")
    public ResponseEntity<DoctorDTO> updateDoctor(
            @PathVariable Long id,
            @RequestBody DoctorDTO doctorDTO) {
        DoctorDTO updatedDoctor = doctorService.updateDoctor(id, doctorDTO);
        return new ResponseEntity<>(updatedDoctor, HttpStatus.OK);
    }
    
    // UPDATE - Update doctor availability
    @PatchMapping("/{id}/availability")
    public ResponseEntity<DoctorDTO> updateDoctorAvailability(
            @PathVariable Long id,
            @RequestParam Boolean isAvailable) {
        DoctorDTO updatedDoctor = doctorService.updateDoctorAvailability(id, isAvailable);
        return new ResponseEntity<>(updatedDoctor, HttpStatus.OK);
    }
    
    // DELETE - Delete doctor by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDoctor(@PathVariable Long id) {
        doctorService.deleteDoctor(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    
    // DELETE - Delete all doctors
    @DeleteMapping
    public ResponseEntity<Void> deleteAllDoctors() {
        doctorService.deleteAllDoctors();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    
    // Check if doctor exists
    @GetMapping("/exists/{id}")
    public ResponseEntity<Boolean> doctorExists(@PathVariable Long id) {
        boolean exists = doctorService.doctorExists(id);
        return new ResponseEntity<>(exists, HttpStatus.OK);
    }
    
    // Get total count of doctors
    @GetMapping("/count")
    public ResponseEntity<Long> getTotalDoctorsCount() {
        long count = doctorService.getTotalDoctorsCount();
        return new ResponseEntity<>(count, HttpStatus.OK);
    }
}
