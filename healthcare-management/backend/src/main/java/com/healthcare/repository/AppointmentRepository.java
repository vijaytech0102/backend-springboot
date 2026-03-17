package com.healthcare.repository;

import com.healthcare.entity.Appointment;
import com.healthcare.enums.AppointmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findByPatientUserId(Long patientId);
    List<Appointment> findByDoctorDoctorId(Long doctorId);
    List<Appointment> findByStatus(AppointmentStatus status);
    List<Appointment> findByAppointmentDate(LocalDate date);
    List<Appointment> findByPatientUserIdAndStatus(Long patientId, AppointmentStatus status);
    List<Appointment> findByDoctorDoctorIdAndStatus(Long doctorId, AppointmentStatus status);
    
    @Query("SELECT a FROM Appointment a WHERE a.patient.userId = :patientId ORDER BY a.appointmentDate DESC")
    List<Appointment> findPatientAppointmentsSorted(Long patientId);
    
    @Query("SELECT a FROM Appointment a WHERE a.doctor.doctorId = :doctorId ORDER BY a.appointmentDate DESC")
    List<Appointment> findDoctorAppointmentsSorted(Long doctorId);
}
