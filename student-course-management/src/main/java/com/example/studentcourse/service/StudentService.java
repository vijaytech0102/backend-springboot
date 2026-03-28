package com.example.studentcourse.service;

import com.example.studentcourse.entity.Student;
import com.example.studentcourse.exception.DuplicateResourceException;
import com.example.studentcourse.exception.ResourceNotFoundException;
import com.example.studentcourse.repository.StudentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * StudentService
 * 
 * This service class contains the business logic for Student-related operations
 * 
 * Key concepts:
 * - @Service: Marks this class as a service component in Spring
 * - @Transactional: Ensures database operations are atomic (all succeed or all fail)
 * - Repositories are injected via dependency injection
 * 
 * The service layer:
 * - Contains business logic and validation
 * - Throws custom exceptions for error handling
 * - Communicates with repositories for database operations
 */
@Service
public class StudentService {

    // Dependency injection: StudentRepository is automatically injected by Spring
    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    /**
     * Creates a new student
     * 
     * Exception Handling Demo:
     * - Throws DuplicateResourceException if email or rollNumber already exists
     * - This demonstrates validation and custom exception handling
     * 
     * @param student The student to create
     * @return The created student with generated ID
     * @throws DuplicateResourceException if student email or roll number already exists
     */
    @Transactional
    public Student createStudent(Student student) {
        // Check if student with this email already exists
        if (studentRepository.existsByEmail(student.getEmail())) {
            throw new DuplicateResourceException(
                    "Student with email '" + student.getEmail() + "' already exists!"
            );
        }

        // Check if student with this roll number already exists
        if (studentRepository.existsByRollNumber(student.getRollNumber())) {
            throw new DuplicateResourceException(
                    "Student with roll number '" + student.getRollNumber() + "' already exists!"
            );
        }

        // If validation passes, save and return the student
        return studentRepository.save(student);
    }

    /**
     * Retrieves a student by ID
     * 
     * Exception Handling Demo:
     * - Throws ResourceNotFoundException if student is not found
     * - This demonstrates using Optional with orElseThrow
     * 
     * @param id The student ID
     * @return The found student
     * @throws ResourceNotFoundException if student with given ID is not found
     */
    public Student getStudentById(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Student with ID " + id + " not found!"
                ));
    }

    /**
     * Retrieves a student by email
     * 
     * @param email The student's email
     * @return The found student
     * @throws ResourceNotFoundException if student with given email is not found
     */
    public Student getStudentByEmail(String email) {
        return studentRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Student with email '" + email + "' not found!"
                ));
    }

    /**
     * Retrieves a student by roll number
     * 
     * @param rollNumber The student's roll number
     * @return The found student
     * @throws ResourceNotFoundException if student with given roll number is not found
     */
    public Student getStudentByRollNumber(String rollNumber) {
        return studentRepository.findByRollNumber(rollNumber)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Student with roll number '" + rollNumber + "' not found!"
                ));
    }

    /**
     * Retrieves all students
     * 
     * @return List of all students
     */
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    /**
     * Updates an existing student
     * 
     * Exception Handling Demo:
     * - Throws ResourceNotFoundException if student is not found
     * - Throws DuplicateResourceException if email is being changed to an existing email
     * 
     * @param id The ID of the student to update
     * @param updated The updated student data
     * @return The updated student
     * @throws ResourceNotFoundException if student not found
     * @throws DuplicateResourceException if new email already exists
     */
    @Transactional
    public Student updateStudent(Long id, Student updated) {
        // First, find the existing student
        Student student = this.getStudentById(id);

        // Check if email is being changed to a duplicate email
        if (!student.getEmail().equals(updated.getEmail()) && 
            studentRepository.existsByEmail(updated.getEmail())) {
            throw new DuplicateResourceException(
                    "Student with email '" + updated.getEmail() + "' already exists!"
            );
        }

        // Update the student fields
        student.setName(updated.getName());
        student.setEmail(updated.getEmail());
        student.setPhoneNumber(updated.getPhoneNumber());
        // rollNumber is typically not changed, but you can add it if needed

        // Save and return the updated student
        return studentRepository.save(student);
    }

    /**
     * Deletes a student by ID
     * 
     * Exception Handling Demo:
     * - Throws ResourceNotFoundException if student is not found
     * 
     * @param id The ID of the student to delete
     * @throws ResourceNotFoundException if student not found
     */
    @Transactional
    public void deleteStudent(Long id) {
        // Verify student exists before attempting to delete
        Student student = this.getStudentById(id);
        studentRepository.delete(student);
    }
}
