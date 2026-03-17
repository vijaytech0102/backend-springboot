package com.example.hospital.service;

import com.example.hospital.dto.DoctorDTO;
import com.example.hospital.entity.Doctor;
import com.example.hospital.repository.DoctorRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DoctorService {
    
    private final DoctorRepository doctorRepository;
    private final ModelMapper modelMapper;
    
    public DoctorService(DoctorRepository doctorRepository, ModelMapper modelMapper) {
        this.doctorRepository = doctorRepository;
        this.modelMapper = modelMapper;
    }
    
    // CREATE - Add a new doctor
    public DoctorDTO createDoctor(DoctorDTO doctorDTO) {
        Doctor doctor = modelMapper.map(doctorDTO, Doctor.class);
        Doctor savedDoctor = doctorRepository.save(doctor);
        return modelMapper.map(savedDoctor, DoctorDTO.class);
    }
    
    // READ - Get all doctors
    public List<DoctorDTO> getAllDoctors() {
        return doctorRepository.findAll()
            .stream()
            .map(doctor -> modelMapper.map(doctor, DoctorDTO.class))
            .collect(Collectors.toList());
    }
    
    // READ - Get doctor by ID
    public DoctorDTO getDoctorById(Long id) {
        Doctor doctor = doctorRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Doctor not found with id: " + id));
        return modelMapper.map(doctor, DoctorDTO.class);
    }
    
    // READ - Get doctor by email
    public DoctorDTO getDoctorByEmail(String email) {
        Doctor doctor = doctorRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("Doctor not found with email: " + email));
        return modelMapper.map(doctor, DoctorDTO.class);
    }
    
    // READ - Get doctor by license number
    public DoctorDTO getDoctorByLicenseNumber(String licenseNumber) {
        Doctor doctor = doctorRepository.findByLicenseNumber(licenseNumber)
            .orElseThrow(() -> new RuntimeException("Doctor not found with license number: " + licenseNumber));
        return modelMapper.map(doctor, DoctorDTO.class);
    }
    
    // READ - Get doctors by specialization
    public List<DoctorDTO> getDoctorsBySpecialization(String specialization) {
        return doctorRepository.findBySpecialization(specialization)
            .stream()
            .map(doctor -> modelMapper.map(doctor, DoctorDTO.class))
            .collect(Collectors.toList());
    }
    
    // READ - Get available doctors
    public List<DoctorDTO> getAvailableDoctors() {
        return doctorRepository.findByIsAvailable(true)
            .stream()
            .map(doctor -> modelMapper.map(doctor, DoctorDTO.class))
            .collect(Collectors.toList());
    }
    
    // UPDATE - Update doctor information
    public DoctorDTO updateDoctor(Long id, DoctorDTO doctorDTO) {
        Doctor doctor = doctorRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Doctor not found with id: " + id));
        
        modelMapper.map(doctorDTO, doctor);
        Doctor updatedDoctor = doctorRepository.save(doctor);
        return modelMapper.map(updatedDoctor, DoctorDTO.class);
    }
    
    // UPDATE - Update doctor availability
    public DoctorDTO updateDoctorAvailability(Long id, Boolean isAvailable) {
        Doctor doctor = doctorRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Doctor not found with id: " + id));
        
        doctor.setIsAvailable(isAvailable);
        Doctor updatedDoctor = doctorRepository.save(doctor);
        return modelMapper.map(updatedDoctor, DoctorDTO.class);
    }
    
    // DELETE - Delete doctor by ID
    public void deleteDoctor(Long id) {
        if (!doctorRepository.existsById(id)) {
            throw new RuntimeException("Doctor not found with id: " + id);
        }
        doctorRepository.deleteById(id);
    }
    
    // DELETE - Delete all doctors
    public void deleteAllDoctors() {
        doctorRepository.deleteAll();
    }
    
    // Check if doctor exists
    public boolean doctorExists(Long id) {
        return doctorRepository.existsById(id);
    }
    
    // Get total count of doctors
    public long getTotalDoctorsCount() {
        return doctorRepository.count();
    }
}
