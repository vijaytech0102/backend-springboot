package com.healthcare.repository;

import com.healthcare.entity.MedicalRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface MedicalRecordRepository extends JpaRepository<MedicalRecord, Long> {
    Optional<MedicalRecord> findByAppointmentAppointmentId(Long appointmentId);
    
    @Query("SELECT m FROM MedicalRecord m WHERE m.appointment.patient.userId = :patientId ORDER BY m.createdAt DESC")
    List<MedicalRecord> findByPatientUserId(Long patientId);
}
