package com.example.aadhaar.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.aadhaar.dto.AadhaarDTO;
import com.example.aadhaar.dto.UserDTO;
import com.example.aadhaar.model.Aadhaar;
import com.example.aadhaar.model.User;
import com.example.aadhaar.repo.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // Create - Save User and return DTO
    public UserDTO saveUser(User user) {
        User savedUser = userRepository.save(user);
        return convertToDTO(savedUser);
    }

    // Read - Get all Users and return DTOs
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    // Read - Get User by ID and return DTO
    public UserDTO getUserById(Long id) {
        Optional<User> user = userRepository.findById(id);
        return user.map(this::convertToDTO).orElse(null);
    }

    // Read - Get User by ID as entity (for backward compatibility)
    public User getUser(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    // Delete - Delete User
    public boolean deleteUser(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // Update - Update User and return DTO
    public UserDTO updateUser(Long id, User userDetails) {
        Optional<User> existingUserOpt = userRepository.findById(id);
        
        if (existingUserOpt.isPresent()) {
            User existingUser = existingUserOpt.get();
            
            if (userDetails.getName() != null) {
                existingUser.setName(userDetails.getName());
            }
            if (userDetails.getEmail() != null) {
                existingUser.setEmail(userDetails.getEmail());
            }
            if (userDetails.getPhone() != null) {
                existingUser.setPhone(userDetails.getPhone());
            }
            if (userDetails.getAadhaar() != null) {
                existingUser.setAadhaar(userDetails.getAadhaar());
            }
            
            User updatedUser = userRepository.save(existingUser);
            return convertToDTO(updatedUser);
        }
        
        return null;
    }

    // Custom query methods
    public User getUserByAadhaarNumber(String aadhaarNumber) {
        return userRepository.findByAadhaarNumber(aadhaarNumber);
    }

    public List<User> getUsersByAadhaarAddress(String address) {
        return userRepository.findByAadhaarAddress(address);
    }

    public List<User> getUsersByAadhaarDob(String dob) {
        return userRepository.findByAadhaarDob(dob);
    }

    public User getUserByAadhaarId(Long aadhaarId) {
        return userRepository.findByAadhaarId(aadhaarId);
    }

    // Helper method to convert User entity to DTO
    private UserDTO convertToDTO(User user) {
        AadhaarDTO aadhaarDTO = null;
        if (user.getAadhaar() != null) {
            Aadhaar aadhaar = user.getAadhaar();
            aadhaarDTO = new AadhaarDTO(
                aadhaar.getId(),
                aadhaar.getAadhaarNumber(),
                aadhaar.getAddress(),
                aadhaar.getDob()
            );
        }
        
        return new UserDTO(
            user.getId(),
            user.getName(),
            user.getEmail(),
            user.getPhone(),
            aadhaarDTO
        );
    }
}