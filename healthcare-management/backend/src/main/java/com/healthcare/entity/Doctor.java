package com.healthcare.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalTime;

@Entity
@Table(name = "doctors", indexes = {
        @Index(name = "idx_specialisation", columnList = "specialisation"),
        @Index(name = "idx_is_available", columnList = "is_available")
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long doctorId;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(nullable = false, length = 100)
    private String specialisation;

    @Column(nullable = false, length = 200)
    private String qualification;

    @Column(columnDefinition = "INT DEFAULT 0")
    private Integer experienceYears = 0;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal consultationFee;

    @Column(columnDefinition = "TEXT")
    private String bio;

    @Column(length = 100)
    private String availabilityDays;

    @Column
    private LocalTime availableFrom;

    @Column
    private LocalTime availableTo;

    @Column(columnDefinition = "BOOLEAN DEFAULT TRUE")
    private Boolean isAvailable = true;
}
