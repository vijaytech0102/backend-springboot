package com.healthcare.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DoctorResponse {

    private Long doctorId;
    private UserResponse user;
    private String specialisation;
    private String qualification;
    private Integer experienceYears;
    private BigDecimal consultationFee;
    private String bio;
    private String availabilityDays;
    private LocalTime availableFrom;
    private LocalTime availableTo;
    private Boolean isAvailable;
    private Double averageRating;

    public DoctorResponse() {
    }

    public DoctorResponse(Long doctorId, UserResponse user, String specialisation, String qualification, Integer experienceYears, BigDecimal consultationFee, String bio, String availabilityDays, LocalTime availableFrom, LocalTime availableTo, Boolean isAvailable, Double averageRating) {
        this.doctorId = doctorId;
        this.user = user;
        this.specialisation = specialisation;
        this.qualification = qualification;
        this.experienceYears = experienceYears;
        this.consultationFee = consultationFee;
        this.bio = bio;
        this.availabilityDays = availabilityDays;
        this.availableFrom = availableFrom;
        this.availableTo = availableTo;
        this.isAvailable = isAvailable;
        this.averageRating = averageRating;
    }

    public Long getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Long doctorId) {
        this.doctorId = doctorId;
    }

    public UserResponse getUser() {
        return user;
    }

    public void setUser(UserResponse user) {
        this.user = user;
    }

    public String getSpecialisation() {
        return specialisation;
    }

    public void setSpecialisation(String specialisation) {
        this.specialisation = specialisation;
    }

    public String getQualification() {
        return qualification;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    public Integer getExperienceYears() {
        return experienceYears;
    }

    public void setExperienceYears(Integer experienceYears) {
        this.experienceYears = experienceYears;
    }

    public BigDecimal getConsultationFee() {
        return consultationFee;
    }

    public void setConsultationFee(BigDecimal consultationFee) {
        this.consultationFee = consultationFee;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getAvailabilityDays() {
        return availabilityDays;
    }

    public void setAvailabilityDays(String availabilityDays) {
        this.availabilityDays = availabilityDays;
    }

    public LocalTime getAvailableFrom() {
        return availableFrom;
    }

    public void setAvailableFrom(LocalTime availableFrom) {
        this.availableFrom = availableFrom;
    }

    public LocalTime getAvailableTo() {
        return availableTo;
    }

    public void setAvailableTo(LocalTime availableTo) {
        this.availableTo = availableTo;
    }

    public Boolean getIsAvailable() {
        return isAvailable;
    }

    public void setIsAvailable(Boolean isAvailable) {
        this.isAvailable = isAvailable;
    }

    public Double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(Double averageRating) {
        this.averageRating = averageRating;
    }

    @Override
    public String toString() {
        return "DoctorResponse{" +
                "doctorId=" + doctorId +
                ", user=" + user +
                ", specialisation='" + specialisation + '\'' +
                ", qualification='" + qualification + '\'' +
                ", experienceYears=" + experienceYears +
                ", consultationFee=" + consultationFee +
                ", bio='" + bio + '\'' +
                ", availabilityDays='" + availabilityDays + '\'' +
                ", availableFrom=" + availableFrom +
                ", availableTo=" + availableTo +
                ", isAvailable=" + isAvailable +
                ", averageRating=" + averageRating +
                '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long doctorId;
        private UserResponse user;
        private String specialisation;
        private String qualification;
        private Integer experienceYears;
        private BigDecimal consultationFee;
        private String bio;
        private String availabilityDays;
        private LocalTime availableFrom;
        private LocalTime availableTo;
        private Boolean isAvailable;
        private Double averageRating;

        public Builder doctorId(Long doctorId) {
            this.doctorId = doctorId;
            return this;
        }

        public Builder user(UserResponse user) {
            this.user = user;
            return this;
        }

        public Builder specialisation(String specialisation) {
            this.specialisation = specialisation;
            return this;
        }

        public Builder qualification(String qualification) {
            this.qualification = qualification;
            return this;
        }

        public Builder experienceYears(Integer experienceYears) {
            this.experienceYears = experienceYears;
            return this;
        }

        public Builder consultationFee(BigDecimal consultationFee) {
            this.consultationFee = consultationFee;
            return this;
        }

        public Builder bio(String bio) {
            this.bio = bio;
            return this;
        }

        public Builder availabilityDays(String availabilityDays) {
            this.availabilityDays = availabilityDays;
            return this;
        }

        public Builder availableFrom(LocalTime availableFrom) {
            this.availableFrom = availableFrom;
            return this;
        }

        public Builder availableTo(LocalTime availableTo) {
            this.availableTo = availableTo;
            return this;
        }

        public Builder isAvailable(Boolean isAvailable) {
            this.isAvailable = isAvailable;
            return this;
        }

        public Builder averageRating(Double averageRating) {
            this.averageRating = averageRating;
            return this;
        }

        public DoctorResponse build() {
            return new DoctorResponse(doctorId, user, specialisation, qualification, experienceYears, consultationFee, bio, availabilityDays, availableFrom, availableTo, isAvailable, averageRating);
        }
    }
}
