package com.example.hospital.repository;

import com.example.hospital.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    
    List<Appointment> findByDoctorId(Long doctorId);
    
    List<Appointment> findByPatientId(Long patientId);
    
    List<Appointment> findByStatus(String status);
    
    List<Appointment> findByDoctorIdAndStatus(Long doctorId, String status);
    
    List<Appointment> findByPatientIdAndStatus(Long patientId, String status);
}
