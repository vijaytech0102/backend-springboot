package com.example.studentcourse.repository;

import com.example.studentcourse.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * StudentRepository Interface
 * Extends JpaRepository for database operations on Student entity
 * 
 * JpaRepository provides:
 * - save(), saveAll()
 * - delete(), deleteAll()
 * - findById(), findAll()
 * - And many more CRUD operations
 * 
 * You can add custom query methods here as needed
 */
@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    /**
     * Custom query method: Find a student by email
     * Spring Data JPA automatically generates the SQL query based on the method name
     * 
     * Equivalent SQL: SELECT * FROM students WHERE email = ?
     * 
     * @param email The student's email
     * @return Optional containing the student if found, empty otherwise
     */
    Optional<Student> findByEmail(String email);

    /**
     * Custom query method: Find a student by roll number
     * 
     * @param rollNumber The student's roll number
     * @return Optional containing the student if found, empty otherwise
     */
    Optional<Student> findByRollNumber(String rollNumber);

    /**
     * Custom query method: Check if a student exists by email
     * Returns true if a student with the given email exists
     * 
     * @param email The email to check
     * @return true if student exists, false otherwise
     */
    boolean existsByEmail(String email);

    /**
     * Custom query method: Check if a student exists by roll number
     * 
     * @param rollNumber The roll number to check
     * @return true if student exists, false otherwise
     */
    boolean existsByRollNumber(String rollNumber);
}
