package com.healthcare.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FeedbackRequest {

    @NotNull(message = "Doctor ID is required")
    private Long doctorId;

    @NotNull(message = "Appointment ID is required")
    private Long appointmentId;

    @NotNull(message = "Rating is required")
    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating cannot be more than 5")
    private Integer rating;

    private String comment;

    public FeedbackRequest() {
    }

    public FeedbackRequest(Long doctorId, Long appointmentId, Integer rating) {
        this.doctorId = doctorId;
        this.appointmentId = appointmentId;
        this.rating = rating;
    }

    public Long getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Long doctorId) {
        this.doctorId = doctorId;
    }

    public Long getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(Long appointmentId) {
        this.appointmentId = appointmentId;
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

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long doctorId;
        private Long appointmentId;
        private Integer rating;
        private String comment;

        public Builder doctorId(Long doctorId) {
            this.doctorId = doctorId;
            return this;
        }

        public Builder appointmentId(Long appointmentId) {
            this.appointmentId = appointmentId;
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

        public FeedbackRequest build() {
            FeedbackRequest request = new FeedbackRequest(doctorId, appointmentId, rating);
            request.comment = this.comment;
            return request;
        }
    }

    @Override
    public String toString() {
        return "FeedbackRequest{" +
                "doctorId=" + doctorId +
                ", appointmentId=" + appointmentId +
                ", rating=" + rating +
                '}';
    }
}
