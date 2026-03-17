package com.healthcare.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MedicalRecordResponse {

    private Long recordId;
    private AppointmentResponse appointment;
    private String diagnosis;
    private String prescription;
    private String labTests;
    private LocalDate followUpDate;
    private String doctorNotes;
    private LocalDateTime createdAt;
}
