package com.healthcare.repository;

import com.healthcare.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    Optional<Doctor> findByUserId(Long userId);
    List<Doctor> findBySpecialisation(String specialisation);
    List<Doctor> findByIsAvailable(Boolean isAvailable);
    
    @Query("SELECT d FROM Doctor d WHERE d.isAvailable = true ORDER BY d.doctorId")
    List<Doctor> findAllAvailableDoctors();
    
    @Query("SELECT d FROM Doctor d WHERE d.specialisation = :specialisation AND d.isAvailable = true")
    List<Doctor> findAvailableDoctorsBySpecialisation(String specialisation);
}
