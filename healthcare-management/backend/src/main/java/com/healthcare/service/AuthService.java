package com.healthcare.service;

import com.healthcare.dto.request.LoginRequest;
import com.healthcare.dto.request.RegisterRequest;
import com.healthcare.dto.response.AuthResponse;
import com.healthcare.entity.User;
import com.healthcare.entity.Doctor;
import com.healthcare.enums.Role;
import com.healthcare.exception.BadRequestException;
import com.healthcare.repository.UserRepository;
import com.healthcare.repository.DoctorRepository;
import com.healthcare.security.JwtTokenProvider;
import com.healthcare.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.healthcare.dto.response.UserResponse;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final DoctorRepository doctorRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final ModelMapper modelMapper;

    @Transactional
    public AuthResponse register(RegisterRequest registerRequest) {
        log.info("Registering new user with email: {}", registerRequest.getEmail());

        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            log.error("Email already exists: {}", registerRequest.getEmail());
            throw new BadRequestException("Email already exists");
        }

        User user = new User();
        user.setFullName(registerRequest.getFullName());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setPhone(registerRequest.getPhone());
        user.setIsActive(true);

        try {
            Role role = Role.valueOf(registerRequest.getRole().toUpperCase());
            user.setRole(role);
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Invalid role: " + registerRequest.getRole());
        }

        User savedUser = userRepository.save(user);
        log.info("User registered successfully with ID: {}", savedUser.getUserId());

        if (savedUser.getRole() == Role.DOCTOR) {
            if (registerRequest.getSpecialisation() == null || registerRequest.getConsultationFee() == null) {
                throw new BadRequestException("Specialisation and consultation fee are required for doctor registration");
            }
            Doctor doctor = Doctor.builder()
                    .user(savedUser)
                    .specialisation(registerRequest.getSpecialisation())
                    .qualification(registerRequest.getQualification() != null ? registerRequest.getQualification() : "")
                    .experienceYears(registerRequest.getExperienceYears() != null ? registerRequest.getExperienceYears() : 0)
                    .consultationFee(BigDecimal.valueOf(registerRequest.getConsultationFee()))
                    .isAvailable(true)
                    .build();
            doctorRepository.save(doctor);
            log.info("Doctor profile created for user ID: {}", savedUser.getUserId());
        }

        return login(new LoginRequest(registerRequest.getEmail(), registerRequest.getPassword()));
    }

    public AuthResponse login(LoginRequest loginRequest) {
        log.info("User login attempt with email: {}", loginRequest.getEmail());

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(),
                            loginRequest.getPassword()
                    )
            );

            String token = tokenProvider.generateToken(authentication);
            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
            User user = userRepository.findById(userPrincipal.getUserId()).orElseThrow();

            UserResponse userResponse = modelMapper.map(user, UserResponse.class);
            userResponse.setRole(user.getRole().toString());

            log.info("User logged in successfully: {}", loginRequest.getEmail());

            return AuthResponse.builder()
                    .accessToken(token)
                    .tokenType("Bearer")
                    .user(userResponse)
                    .message("Login successful")
                    .build();
        } catch (Exception e) {
            log.error("Login failed for email: {}", loginRequest.getEmail());
            throw new BadRequestException("Invalid email or password");
        }
    }
}
