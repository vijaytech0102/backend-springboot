package com.healthcare.service;

import com.healthcare.dto.response.UserResponse;
import com.healthcare.entity.User;
import com.healthcare.exception.ResourceNotFoundException;
import com.healthcare.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PatientService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public UserResponse getCurrentPatient() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found"));
        UserResponse response = modelMapper.map(user, UserResponse.class);
        response.setRole(user.getRole().toString());
        return response;
    }

    @Transactional
    public UserResponse updatePatientProfile(UserResponse updateRequest) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found"));

        if (updateRequest.getFullName() != null) {
            user.setFullName(updateRequest.getFullName());
        }
        if (updateRequest.getPhone() != null) {
            user.setPhone(updateRequest.getPhone());
        }
        if (updateRequest.getAddress() != null) {
            user.setAddress(updateRequest.getAddress());
        }
        if (updateRequest.getDateOfBirth() != null) {
            user.setDateOfBirth(updateRequest.getDateOfBirth());
        }

        User updated = userRepository.save(user);
        log.info("Patient profile updated for user ID: {}", updated.getUserId());

        UserResponse response = modelMapper.map(updated, UserResponse.class);
        response.setRole(updated.getRole().toString());
        return response;
    }

    public void deletePatientAccount() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found"));
        userRepository.deleteById(user.getUserId());
        log.info("Patient account deleted for user ID: {}", user.getUserId());
    }
}
