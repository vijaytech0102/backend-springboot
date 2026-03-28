package com.example.studentcourse;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * StudentCourseApplication
 * 
 * Main entry point for the Spring Boot application
 * 
 * @SpringBootApplication: This annotation does three things:
 * 1. @Configuration: Marks this as a configuration class
 * 2. @ComponentScan: Scans for Spring components in the package and its subpackages
 * 3. @EnableAutoConfiguration: Enables Spring Boot auto-configuration
 * 
 * When you run this class, Spring Boot:
 * 1. Creates an application context
 * 2. Auto-configures Spring and third-party libraries
 * 3. Starts the embedded Tomcat server
 * 4. Sets up data source, JPA, etc.
 * 
 * Project Structure:
 * - entity: JPA entity classes (Student, Course) with annotations for database mapping
 * - repository: Spring Data JPA repositories for database operations
 * - service: Business logic layer with transaction management and exception handling
 * - controller: REST API endpoints with request/response handling
 * - exception: Custom exception classes and global exception handler
 * 
 * Key Learning Points:
 * 
 * 1. MANY-TO-MANY RELATIONSHIP:
 *    - Student and Course entities have a many-to-many relationship
 *    - One student can enroll in multiple courses
 *    - One course can have multiple students
 *    - The relationship is stored in a join table 'student_course'
 *    - See: Student.java and Course.java for @ManyToMany annotations
 * 
 * 2. EXCEPTION HANDLING:
 *    - Custom exceptions: ResourceNotFoundException, DuplicateResourceException, CourseCapacityException
 *    - GlobalExceptionHandler: Centralized exception handling for all controllers
 *    - Service layer throws exceptions, controllers let them propagate
 *    - GlobalExceptionHandler catches and converts to appropriate HTTP responses
 *    - See: exception package for exception classes and handler
 * 
 * 3. TRANSACTION MANAGEMENT:
 *    - @Transactional annotation ensures database operations are atomic
 *    - If any operation fails, all changes are rolled back
 *    - See: service methods with @Transactional
 * 
 * Database Schema:
 * - students table: Stores student information
 * - courses table: Stores course information
 * - student_course table: Join table for many-to-many relationship
 * 
 * How to use:
 * 1. Ensure MySQL is running
 * 2. Create a database: CREATE DATABASE student_course_db;
 * 3. Update application.properties with your database credentials
 * 4. Run this application
 * 5. Access API at http://localhost:8080/api/...
 * 
 * Test the API endpoints:
 * - Create Student: POST /api/students
 * - Create Course: POST /api/courses
 * - Enroll Student: POST /api/courses/{courseId}/students/{studentId}
 * - Get Students in Course: GET /api/courses/{courseId}/students
 * - And more...
 */
@SpringBootApplication
public class StudentCourseApplication {

    /**
     * Main method: Entry point for the Spring Boot application
     * 
     * @param args Command-line arguments (optional)
     */
    public static void main(String[] args) {
        // SpringApplication.run() starts the Spring Boot application
        SpringApplication.run(StudentCourseApplication.class, args);
    }
}
