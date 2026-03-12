package com.example.aadhaar.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.aadhaar.dto.UserDTO;
import com.example.aadhaar.model.User;
import com.example.aadhaar.service.UserService;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    // Create - Post User
    @PostMapping
    public UserDTO createUser(@RequestBody User user) {
        return userService.saveUser(user);
    }

    // Read - Get all Users
    @GetMapping()
    public List<UserDTO> getAllUsers() {
        return userService.getAllUsers();
    }

    // Read - Get User by ID
    @GetMapping("/{id}")
    public UserDTO getUser(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    // Update - Update User
    @PutMapping("/{id}")
    public UserDTO updateUser(@PathVariable Long id, @RequestBody User userDetails) {
        return userService.updateUser(id, userDetails);
    }

    // Delete - Delete User
    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable Long id) {
        boolean isDeleted = userService.deleteUser(id);
        if (isDeleted) {
            return "User Deleted Successfully";
        }
        return "User not found";
    }

    // Custom search endpoints
    @GetMapping("/aadhaar/number/{aadhaarNumber}")
    public User getUserByAadhaarNumber(@PathVariable String aadhaarNumber) {
        return userService.getUserByAadhaarNumber(aadhaarNumber);
    }

    @GetMapping("/aadhaar/address/{address}")
    public List<User> getUsersByAddress(@PathVariable String address) {
        return userService.getUsersByAadhaarAddress(address);
    }

    @GetMapping("/aadhaar/dob/{dob}")
    public List<User> getUsersByDob(@PathVariable String dob) {
        return userService.getUsersByAadhaarDob(dob);
    }

    @GetMapping("/by-aadhaar-id/{aadhaarId}")
    public User getUserByAadhaarId(@PathVariable Long aadhaarId) {
        return userService.getUserByAadhaarId(aadhaarId);
    }
}