package com.example.aadhaar.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.aadhaar.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    
    @Query("SELECT u FROM User u WHERE u.aadhaar.aadhaarNumber = :aadhaarNumber")
    User findByAadhaarNumber(@Param("aadhaarNumber") String aadhaarNumber);
    
    @Query("SELECT u FROM User u WHERE u.aadhaar.address = :address")
    java.util.List<User> findByAadhaarAddress(@Param("address") String address);
    
    @Query("SELECT u FROM User u WHERE u.aadhaar.dob = :dob")
    java.util.List<User> findByAadhaarDob(@Param("dob") String dob);
    
    @Query("SELECT u FROM User u WHERE u.aadhaar.id = :aadhaarId")
    User findByAadhaarId(@Param("aadhaarId") Long aadhaarId);
}