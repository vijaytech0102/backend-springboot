package com.healthcare.repository;

import com.healthcare.entity.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
    List<Feedback> findByDoctorDoctorId(Long doctorId);
    List<Feedback> findByPatientUserId(Long patientId);
    
    @Query("SELECT AVG(f.rating) FROM Feedback f WHERE f.doctor.doctorId = :doctorId")
    Double getAverageRatingForDoctor(Long doctorId);
    
    @Query("SELECT f FROM Feedback f WHERE f.doctor.doctorId = :doctorId ORDER BY f.createdAt DESC")
    List<Feedback> findDoctorFeedbacksSorted(Long doctorId);
}
