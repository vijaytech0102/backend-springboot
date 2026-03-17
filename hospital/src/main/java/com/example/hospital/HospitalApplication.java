package com.example.hospital;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Hospital Management System Application
 * 
 * This is a Spring Boot application for managing hospital operations including:
 * - Doctor management (CRUD operations)
 * - Patient management (CRUD operations)
 * 
 * The application uses:
 * - Spring Data JPA for database operations
 * - ModelMapper for DTO mapping
 * - H2 in-memory database for development and testing
 */
@SpringBootApplication
public class HospitalApplication {

    public static void main(String[] args) {
        SpringApplication.run(HospitalApplication.class, args);
    }
}
