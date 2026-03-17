package com.healthcare.service;

import com.healthcare.dto.response.UserResponse;
import com.healthcare.dto.response.AdminStatsResponse;
import com.healthcare.entity.User;
import com.healthcare.enums.Role;
import com.healthcare.exception.ResourceNotFoundException;
import com.healthcare.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminService {

    private final UserRepository userRepository;
    private final DoctorRepository doctorRepository;
    private final AppointmentRepository appointmentRepository;
    private final BillRepository billRepository;
    private final FeedbackRepository feedbackRepository;
    private final ModelMapper modelMapper;

    public List<UserResponse> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(user -> {
                    UserResponse response = modelMapper.map(user, UserResponse.class);
                    response.setRole(user.getRole().toString());
                    return response;
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public UserResponse toggleUserStatus(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        user.setIsActive(!user.getIsActive());
        User updated = userRepository.save(user);
        log.info("User status toggled for user ID: {}, active: {}", userId, updated.getIsActive());

        UserResponse response = modelMapper.map(updated, UserResponse.class);
        response.setRole(updated.getRole().toString());
        return response;
    }

    @Transactional
    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        userRepository.deleteById(userId);
        log.info("User deleted with ID: {}", userId);
    }

    public AdminStatsResponse getAdminStats() {
        long totalUsers = userRepository.count();
        long totalDoctors = doctorRepository.count();
        long totalPatients = userRepository.findByRole(Role.PATIENT).size();
        long totalAppointments = appointmentRepository.count();
        long completedAppointments = appointmentRepository
                .findByStatus(com.healthcare.enums.AppointmentStatus.COMPLETED).size();
        long pendingAppointments = appointmentRepository
                .findByStatus(com.healthcare.enums.AppointmentStatus.PENDING).size();

        BigDecimal totalRevenue = billRepository.getTotalRevenue();
        if (totalRevenue == null) {
            totalRevenue = BigDecimal.ZERO;
        }

        YearMonth currentMonth = YearMonth.now();
        BigDecimal monthlyRevenue = billRepository.getMonthlyRevenue(currentMonth.getMonthValue(), currentMonth.getYear());
        if (monthlyRevenue == null) {
            monthlyRevenue = BigDecimal.ZERO;
        }

        Double averageRating = feedbackRepository.getAverageRatingForDoctor(null);
        if (averageRating == null) {
            averageRating = 0.0;
        }

        return AdminStatsResponse.builder()
                .totalUsers(totalUsers)
                .totalDoctors(totalDoctors)
                .totalPatients(totalPatients)
                .totalAppointments(totalAppointments)
                .completedAppointments(completedAppointments)
                .pendingAppointments(pendingAppointments)
                .totalRevenue(totalRevenue)
                .monthlyRevenue(monthlyRevenue)
                .averageRating(averageRating)
                .build();
    }
}
