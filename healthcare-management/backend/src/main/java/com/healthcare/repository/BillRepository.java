package com.healthcare.repository;

import com.healthcare.entity.Bill;
import com.healthcare.enums.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;
import java.math.BigDecimal;

@Repository
public interface BillRepository extends JpaRepository<Bill, Long> {
    Optional<Bill> findByAppointmentAppointmentId(Long appointmentId);
    
    @Query("SELECT b FROM Bill b WHERE b.appointment.patient.userId = :patientId ORDER BY b.generatedAt DESC")
    List<Bill> findByPatientUserId(Long patientId);
    
    List<Bill> findByPaymentStatus(PaymentStatus paymentStatus);
    
    @Query("SELECT SUM(b.totalAmount) FROM Bill b WHERE b.paymentStatus = 'PAID'")
    BigDecimal getTotalRevenue();
    
    @Query("SELECT SUM(b.totalAmount) FROM Bill b WHERE b.paymentStatus = 'PAID' AND MONTH(b.paymentDate) = :month AND YEAR(b.paymentDate) = :year")
    BigDecimal getMonthlyRevenue(int month, int year);
}
