package com.healthcare.service;

import com.healthcare.dto.response.BillResponse;
import com.healthcare.entity.Bill;
import com.healthcare.entity.Appointment;
import com.healthcare.enums.PaymentStatus;
import com.healthcare.exception.ResourceNotFoundException;
import com.healthcare.repository.BillRepository;
import com.healthcare.repository.AppointmentRepository;
import com.healthcare.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BillingService {

    private final BillRepository billRepository;
    private final AppointmentRepository appointmentRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public BillResponse getBillByAppointmentId(Long appointmentId) {
        Bill bill = billRepository.findByAppointmentAppointmentId(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Bill not found"));
        return mapBillToResponse(bill);
    }

    public List<BillResponse> getPatientBills() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Long userId = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found"))
                .getUserId();

        List<Bill> bills = billRepository.findByPatientUserId(userId);
        return bills.stream()
                .map(this::mapBillToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public BillResponse payBill(Long billId) {
        Bill bill = billRepository.findById(billId)
                .orElseThrow(() -> new ResourceNotFoundException("Bill not found"));

        bill.setPaymentStatus(PaymentStatus.PAID);
        bill.setPaymentDate(LocalDateTime.now());
        Bill updated = billRepository.save(bill);
        log.info("Bill marked as paid: {}", billId);

        return mapBillToResponse(updated);
    }

    public List<BillResponse> getAllBills() {
        List<Bill> bills = billRepository.findAll();
        return bills.stream()
                .map(this::mapBillToResponse)
                .collect(Collectors.toList());
    }

    private BillResponse mapBillToResponse(Bill bill) {
        BillResponse response = modelMapper.map(bill, BillResponse.class);
        response.setPaymentStatus(bill.getPaymentStatus().toString());
        if (bill.getAppointment() != null) {
            response.setAppointmentId(bill.getAppointment().getAppointmentId());
        }
        return response;
    }
}
