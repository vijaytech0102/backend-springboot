package com.example.studentcourse.controller;

import com.example.studentcourse.entity.Student;
import com.example.studentcourse.service.StudentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * StudentController
 * 
 * REST API controller for Student operations
 * 
 * Key concepts:
 * - @RestController: Combines @Controller + @ResponseBody
 * - @RequestMapping: Base path for all endpoints in this controller
 * - @PostMapping, @GetMapping, @PutMapping, @DeleteMapping: HTTP method mappings
 * - @PathVariable: Extracts variables from URL path
 * - @RequestBody: Converts JSON request body to Java object
 * 
 * Exception Handling:
 * - Exceptions thrown in service are caught by GlobalExceptionHandler
 * - No try-catch needed here; let exceptions propagate to the handler
 * 
 * Endpoints provided:
 * - POST /api/students - Create a new student
 * - GET /api/students - Get all students
 * - GET /api/students/{id} - Get student by ID
 * - GET /api/students/email/{email} - Get student by email
 * - PUT /api/students/{id} - Update a student
 * - DELETE /api/students/{id} - Delete a student
 */
@RestController
@RequestMapping("/api/students")
public class StudentController {

    // Dependency injection: StudentService is automatically injected
    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    /**
     * Create a new student
     * HTTP Method: POST
     * URL: /api/students
     * 
     * Example request body:
     * {
     *     "name": "John Doe",
     *     "email": "john@example.com",
     *     "rollNumber": "STU001",
     *     "phoneNumber": "1234567890"
     * }
     * 
     * @param student The student data from request body
     * @return ResponseEntity with created student and HTTP 201 (Created) status
     * @throws DuplicateResourceException if email or roll number already exists
     */
    @PostMapping
    public ResponseEntity<Student> createStudent(@RequestBody Student student) {
        // Call service to create student
        // If duplicate email/rollNumber, service throws DuplicateResourceException
        // GlobalExceptionHandler catches it and returns 409 response
        Student createdStudent = studentService.createStudent(student);

        // Return 201 Created with the new student
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdStudent);
    }

    /**
     * Get a student by ID
     * HTTP Method: GET
     * URL: /api/students/{id}
     * 
     * Example: GET /api/students/1
     * 
     * @param id The student ID from URL path
     * @return ResponseEntity with the student data
     * @throws ResourceNotFoundException if student with ID not found
     */
    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudentById(@PathVariable Long id) {
        // Call service to retrieve student by ID
        // If not found, service throws ResourceNotFoundException
        // GlobalExceptionHandler catches it and returns 404 response
        Student student = studentService.getStudentById(id);

        return ResponseEntity.ok(student);
    }

    /**
     * Get a student by email
     * HTTP Method: GET
     * URL: /api/students/email/{email}
     * 
     * Example: GET /api/students/email/john@example.com
     * 
     * @param email The student's email from URL path
     * @return ResponseEntity with the student data
     * @throws ResourceNotFoundException if student with email not found
     */
    @GetMapping("/email/{email}")
    public ResponseEntity<Student> getStudentByEmail(@PathVariable String email) {
        Student student = studentService.getStudentByEmail(email);
        return ResponseEntity.ok(student);
    }

    /**
     * Get all students
     * HTTP Method: GET
     * URL: /api/students
     * 
     * @return List of all students
     */
    @GetMapping
    public ResponseEntity<List<Student>> getAllStudents() {
        List<Student> students = studentService.getAllStudents();
        return ResponseEntity.ok(students);
    }

    /**
     * Update an existing student
     * HTTP Method: PUT
     * URL: /api/students/{id}
     * 
     * Example request body:
     * {
     *     "name": "John Doe Updated",
     *     "email": "newemail@example.com",
     *     "rollNumber": "STU001",
     *     "phoneNumber": "9876543210"
     * }
     * 
     * @param id The student ID to update
     * @param student The updated student data
     * @return ResponseEntity with updated student
     * @throws ResourceNotFoundException if student not found
     * @throws DuplicateResourceException if new email already exists
     */
    @PutMapping("/{id}")
    public ResponseEntity<Student> updateStudent(
            @PathVariable Long id,
            @RequestBody Student student) {
        // Call service to update student
        Student updatedStudent = studentService.updateStudent(id, student);

        return ResponseEntity.ok(updatedStudent);
    }

    /**
     * Delete a student
     * HTTP Method: DELETE
     * URL: /api/students/{id}
     * 
     * Example: DELETE /api/students/1
     * 
     * @param id The student ID to delete
     * @return ResponseEntity with HTTP 204 (No Content) status
     * @throws ResourceNotFoundException if student not found
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        // Call service to delete student
        studentService.deleteStudent(id);

        // Return 204 No Content (standard response for successful DELETE)
        return ResponseEntity.noContent().build();
    }
}
