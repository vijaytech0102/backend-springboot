package com.healthcare.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MedicalRecordRequest {

    @NotNull(message = "Appointment ID is required")
    private Long appointmentId;

    @NotBlank(message = "Diagnosis is required")
    private String diagnosis;

    private String prescription;

    private String labTests;

    private LocalDate followUpDate;

    private String doctorNotes;

    public MedicalRecordRequest() {
    }

    public MedicalRecordRequest(Long appointmentId, String diagnosis) {
        this.appointmentId = appointmentId;
        this.diagnosis = diagnosis;
    }

    public Long getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(Long appointmentId) {
        this.appointmentId = appointmentId;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public String getPrescription() {
        return prescription;
    }

    public void setPrescription(String prescription) {
        this.prescription = prescription;
    }

    public String getLabTests() {
        return labTests;
    }

    public void setLabTests(String labTests) {
        this.labTests = labTests;
    }

    public LocalDate getFollowUpDate() {
        return followUpDate;
    }

    public void setFollowUpDate(LocalDate followUpDate) {
        this.followUpDate = followUpDate;
    }

    public String getDoctorNotes() {
        return doctorNotes;
    }

    public void setDoctorNotes(String doctorNotes) {
        this.doctorNotes = doctorNotes;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long appointmentId;
        private String diagnosis;
        private String prescription;
        private String labTests;
        private LocalDate followUpDate;
        private String doctorNotes;

        public Builder appointmentId(Long appointmentId) {
            this.appointmentId = appointmentId;
            return this;
        }

        public Builder diagnosis(String diagnosis) {
            this.diagnosis = diagnosis;
            return this;
        }

        public Builder prescription(String prescription) {
            this.prescription = prescription;
            return this;
        }

        public Builder labTests(String labTests) {
            this.labTests = labTests;
            return this;
        }

        public Builder followUpDate(LocalDate followUpDate) {
            this.followUpDate = followUpDate;
            return this;
        }

        public Builder doctorNotes(String doctorNotes) {
            this.doctorNotes = doctorNotes;
            return this;
        }

        public MedicalRecordRequest build() {
            MedicalRecordRequest request = new MedicalRecordRequest(appointmentId, diagnosis);
            request.prescription = this.prescription;
            request.labTests = this.labTests;
            request.followUpDate = this.followUpDate;
            request.doctorNotes = this.doctorNotes;
            return request;
        }
    }

    @Override
    public String toString() {
        return "MedicalRecordRequest{" +
                "appointmentId=" + appointmentId +
                ", diagnosis='" + diagnosis + '\'' +
                '}';
    }
}
