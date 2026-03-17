package com.healthcare.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FeedbackResponse {

    private Long feedbackId;
    private UserResponse patient;
    private DoctorResponse doctor;
    private AppointmentResponse appointment;
    private Integer rating;
    private String comment;
    private LocalDateTime createdAt;

    public FeedbackResponse() {
    }

    public FeedbackResponse(Long feedbackId, UserResponse patient, DoctorResponse doctor, AppointmentResponse appointment, Integer rating, String comment, LocalDateTime createdAt) {
        this.feedbackId = feedbackId;
        this.patient = patient;
        this.doctor = doctor;
        this.appointment = appointment;
        this.rating = rating;
        this.comment = comment;
        this.createdAt = createdAt;
    }

    public Long getFeedbackId() {
        return feedbackId;
    }

    public void setFeedbackId(Long feedbackId) {
        this.feedbackId = feedbackId;
    }

    public UserResponse getPatient() {
        return patient;
    }

    public void setPatient(UserResponse patient) {
        this.patient = patient;
    }

    public DoctorResponse getDoctor() {
        return doctor;
    }

    public void setDoctor(DoctorResponse doctor) {
        this.doctor = doctor;
    }

    public AppointmentResponse getAppointment() {
        return appointment;
    }

    public void setAppointment(AppointmentResponse appointment) {
        this.appointment = appointment;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "FeedbackResponse{" +
                "feedbackId=" + feedbackId +
                ", patient=" + patient +
                ", doctor=" + doctor +
                ", appointment=" + appointment +
                ", rating=" + rating +
                ", comment='" + comment + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long feedbackId;
        private UserResponse patient;
        private DoctorResponse doctor;
        private AppointmentResponse appointment;
        private Integer rating;
        private String comment;
        private LocalDateTime createdAt;

        public Builder feedbackId(Long feedbackId) {
            this.feedbackId = feedbackId;
            return this;
        }

        public Builder patient(UserResponse patient) {
            this.patient = patient;
            return this;
        }

        public Builder doctor(DoctorResponse doctor) {
            this.doctor = doctor;
            return this;
        }

        public Builder appointment(AppointmentResponse appointment) {
            this.appointment = appointment;
            return this;
        }

        public Builder rating(Integer rating) {
            this.rating = rating;
            return this;
        }

        public Builder comment(String comment) {
            this.comment = comment;
            return this;
        }

        public Builder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public FeedbackResponse build() {
            return new FeedbackResponse(feedbackId, patient, doctor, appointment, rating, comment, createdAt);
        }
    }
}
