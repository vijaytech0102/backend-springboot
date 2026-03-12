package com.example.onetomany;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main Application class for One-to-Many Demo
 * This Spring Boot application demonstrates One-to-Many relationship
 * between Author and Book entities with complete CRUD operations
 */
@SpringBootApplication
public class OneToManyDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(OneToManyDemoApplication.class, args);
    }
}
