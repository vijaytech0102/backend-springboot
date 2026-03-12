package com.example.aadhaar.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.aadhaar.model.Aadhaar;

public interface AadhaarRepository extends JpaRepository<Aadhaar, Long> {
}