package com.healthcare.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BillResponse {

    private Long billId;
    private Long appointmentId;
    private AppointmentResponse appointment;
    private BigDecimal consultationFee;
    private BigDecimal additionalCharges;
    private BigDecimal discount;
    private BigDecimal totalAmount;
    private String paymentStatus;
    private LocalDateTime paymentDate;
    private LocalDateTime generatedAt;

    public BillResponse() {
    }

    public BillResponse(Long billId, Long appointmentId, AppointmentResponse appointment, BigDecimal consultationFee, BigDecimal additionalCharges, BigDecimal discount, BigDecimal totalAmount, String paymentStatus, LocalDateTime paymentDate, LocalDateTime generatedAt) {
        this.billId = billId;
        this.appointmentId = appointmentId;
        this.appointment = appointment;
        this.consultationFee = consultationFee;
        this.additionalCharges = additionalCharges;
        this.discount = discount;
        this.totalAmount = totalAmount;
        this.paymentStatus = paymentStatus;
        this.paymentDate = paymentDate;
        this.generatedAt = generatedAt;
    }

    public Long getBillId() {
        return billId;
    }

    public void setBillId(Long billId) {
        this.billId = billId;
    }

    public Long getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(Long appointmentId) {
        this.appointmentId = appointmentId;
    }

    public AppointmentResponse getAppointment() {
        return appointment;
    }

    public void setAppointment(AppointmentResponse appointment) {
        this.appointment = appointment;
    }

    public BigDecimal getConsultationFee() {
        return consultationFee;
    }

    public void setConsultationFee(BigDecimal consultationFee) {
        this.consultationFee = consultationFee;
    }

    public BigDecimal getAdditionalCharges() {
        return additionalCharges;
    }

    public void setAdditionalCharges(BigDecimal additionalCharges) {
        this.additionalCharges = additionalCharges;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public LocalDateTime getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDateTime paymentDate) {
        this.paymentDate = paymentDate;
    }

    public LocalDateTime getGeneratedAt() {
        return generatedAt;
    }

    public void setGeneratedAt(LocalDateTime generatedAt) {
        this.generatedAt = generatedAt;
    }

    @Override
    public String toString() {
        return "BillResponse{" +
                "billId=" + billId +
                ", appointmentId=" + appointmentId +
                ", appointment=" + appointment +
                ", consultationFee=" + consultationFee +
                ", additionalCharges=" + additionalCharges +
                ", discount=" + discount +
                ", totalAmount=" + totalAmount +
                ", paymentStatus='" + paymentStatus + '\'' +
                ", paymentDate=" + paymentDate +
                ", generatedAt=" + generatedAt +
                '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long billId;
        private Long appointmentId;
        private AppointmentResponse appointment;
        private BigDecimal consultationFee;
        private BigDecimal additionalCharges;
        private BigDecimal discount;
        private BigDecimal totalAmount;
        private String paymentStatus;
        private LocalDateTime paymentDate;
        private LocalDateTime generatedAt;

        public Builder billId(Long billId) {
            this.billId = billId;
            return this;
        }

        public Builder appointmentId(Long appointmentId) {
            this.appointmentId = appointmentId;
            return this;
        }

        public Builder appointment(AppointmentResponse appointment) {
            this.appointment = appointment;
            return this;
        }

        public Builder consultationFee(BigDecimal consultationFee) {
            this.consultationFee = consultationFee;
            return this;
        }

        public Builder additionalCharges(BigDecimal additionalCharges) {
            this.additionalCharges = additionalCharges;
            return this;
        }

        public Builder discount(BigDecimal discount) {
            this.discount = discount;
            return this;
        }

        public Builder totalAmount(BigDecimal totalAmount) {
            this.totalAmount = totalAmount;
            return this;
        }

        public Builder paymentStatus(String paymentStatus) {
            this.paymentStatus = paymentStatus;
            return this;
        }

        public Builder paymentDate(LocalDateTime paymentDate) {
            this.paymentDate = paymentDate;
            return this;
        }

        public Builder generatedAt(LocalDateTime generatedAt) {
            this.generatedAt = generatedAt;
            return this;
        }

        public BillResponse build() {
            return new BillResponse(billId, appointmentId, appointment, consultationFee, additionalCharges, discount, totalAmount, paymentStatus, paymentDate, generatedAt);
        }
    }
}
