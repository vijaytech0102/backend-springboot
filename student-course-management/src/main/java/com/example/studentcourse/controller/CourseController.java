package com.example.studentcourse.controller;

import com.example.studentcourse.entity.Course;
import com.example.studentcourse.entity.Student;
import com.example.studentcourse.service.CourseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * CourseController
 * 
 * REST API controller for Course operations and Student-Course enrollment (Many-to-Many)
 * 
 * Key concepts:
 * - @RestController: Marks this as a REST controller
 * - @RequestMapping: Base path for all endpoints (/api/courses)
 * - Handles both course CRUD operations and enrollment operations
 * 
 * Many-to-Many Operations:
 * - POST /api/courses/{courseId}/students/{studentId} - Enroll student in course
 * - DELETE /api/courses/{courseId}/students/{studentId} - Unenroll student from course
 * - GET /api/courses/{courseId}/students - Get all students in a course
 * 
 * Exception Handling:
 * - All exceptions from service layer are caught by GlobalExceptionHandler
 * - Returns appropriate HTTP status codes and error messages
 * 
 * Endpoints provided:
 * - POST /api/courses - Create a new course
 * - GET /api/courses - Get all courses
 * - GET /api/courses/{id} - Get course by ID
 * - PUT /api/courses/{id} - Update a course
 * - DELETE /api/courses/{id} - Delete a course
 * - POST /api/courses/{courseId}/students/{studentId} - Enroll student
 * - DELETE /api/courses/{courseId}/students/{studentId} - Unenroll student
 * - GET /api/courses/{courseId}/students - Get enrolled students
 * - GET /api/courses/{courseId}/students/count - Get enrollment count
 */
@RestController
@RequestMapping("/api/courses")
public class CourseController {

    // Dependency injection: CourseService is automatically injected
    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    /**
     * Create a new course
     * HTTP Method: POST
     * URL: /api/courses
     * 
     * Example request body:
     * {
     *     "courseCode": "CS101",
     *     "courseName": "Introduction to Computer Science",
     *     "description": "Basic CS concepts",
     *     "credits": 3,
     *     "instructorName": "Dr. Smith",
     *     "maxStudents": 50
     * }
     * 
     * @param course The course data from request body
     * @return ResponseEntity with created course and HTTP 201 (Created) status
     * @throws DuplicateResourceException if course code already exists
     */
    @PostMapping
    public ResponseEntity<Course> createCourse(@RequestBody Course course) {
        Course createdCourse = courseService.createCourse(course);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdCourse);
    }

    /**
     * Get a course by ID
     * HTTP Method: GET
     * URL: /api/courses/{id}
     * 
     * Example: GET /api/courses/1
     * 
     * @param id The course ID
     * @return ResponseEntity with the course data
     * @throws ResourceNotFoundException if course not found
     */
    @GetMapping("/{id}")
    public ResponseEntity<Course> getCourseById(@PathVariable Long id) {
        Course course = courseService.getCourseById(id);
        return ResponseEntity.ok(course);
    }

    /**
     * Get all courses
     * HTTP Method: GET
     * URL: /api/courses
     * 
     * @return List of all courses
     */
    @GetMapping
    public ResponseEntity<List<Course>> getAllCourses() {
        List<Course> courses = courseService.getAllCourses();
        return ResponseEntity.ok(courses);
    }

    /**
     * Update an existing course
     * HTTP Method: PUT
     * URL: /api/courses/{id}
     * 
     * @param id The course ID to update
     * @param course The updated course data
     * @return ResponseEntity with updated course
     * @throws ResourceNotFoundException if course not found
     * @throws DuplicateResourceException if new course code already exists
     */
    @PutMapping("/{id}")
    public ResponseEntity<Course> updateCourse(
            @PathVariable Long id,
            @RequestBody Course course) {
        Course updatedCourse = courseService.updateCourse(id, course);
        return ResponseEntity.ok(updatedCourse);
    }

    /**
     * Delete a course
     * HTTP Method: DELETE
     * URL: /api/courses/{id}
     * 
     * @param id The course ID to delete
     * @return ResponseEntity with HTTP 204 (No Content) status
     * @throws ResourceNotFoundException if course not found
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable Long id) {
        courseService.deleteCourse(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * ========== MANY-TO-MANY OPERATIONS ==========
     * 
     * The following endpoints demonstrate the many-to-many relationship
     * between Student and Course entities
     * A student can be in multiple courses, and a course can have multiple students
     */

    /**
     * MANY-TO-MANY: Enroll a student in a course
     * HTTP Method: POST
     * URL: /api/courses/{courseId}/students/{studentId}
     * 
     * This adds the student to the course and the course to the student
     * The relationship is stored in the 'student_course' join table
     * 
     * Example: POST /api/courses/1/students/5
     * 
     * @param courseId The ID of the course
     * @param studentId The ID of the student
     * @return ResponseEntity with the updated course including the new student
     * @throws ResourceNotFoundException if student or course not found
     * @throws DuplicateResourceException if student already enrolled in course
     * @throws CourseCapacityException if course is at full capacity
     */
    @PostMapping("/{courseId}/students/{studentId}")
    public ResponseEntity<Course> enrollStudentInCourse(
            @PathVariable Long courseId,
            @PathVariable Long studentId) {
        // Call service to enroll student
        // Service handles validation and throws appropriate exceptions
        Course course = courseService.enrollStudentInCourse(studentId, courseId);
        return ResponseEntity.ok(course);
    }

    /**
     * MANY-TO-MANY: Unenroll a student from a course
     * HTTP Method: DELETE
     * URL: /api/courses/{courseId}/students/{studentId}
     * 
     * This removes the student from the course and the course from the student
     * 
     * Example: DELETE /api/courses/1/students/5
     * 
     * @param courseId The ID of the course
     * @param studentId The ID of the student
     * @return ResponseEntity with the updated course
     * @throws ResourceNotFoundException if student, course not found or student not enrolled
     */
    @DeleteMapping("/{courseId}/students/{studentId}")
    public ResponseEntity<Course> unrollStudentFromCourse(
            @PathVariable Long courseId,
            @PathVariable Long studentId) {
        // Call service to unenroll student
        Course course = courseService.unrollStudentFromCourse(studentId, courseId);
        return ResponseEntity.ok(course);
    }

    /**
     * MANY-TO-MANY: Get all students enrolled in a course
     * HTTP Method: GET
     * URL: /api/courses/{courseId}/students
     * 
     * Returns a list of all students in the course
     * 
     * Example: GET /api/courses/1/students
     * 
     * @param courseId The ID of the course
     * @return List of students enrolled in the course
     * @throws ResourceNotFoundException if course not found
     */
    @GetMapping("/{courseId}/students")
    public ResponseEntity<List<Student>> getStudentsInCourse(@PathVariable Long courseId) {
        List<Student> students = courseService.getStudentsEnrolledInCourse(courseId);
        return ResponseEntity.ok(students);
    }

    /**
     * Get the number of students enrolled in a course
     * HTTP Method: GET
     * URL: /api/courses/{courseId}/students/count
     * 
     * Example: GET /api/courses/1/students/count
     * 
     * @param courseId The ID of the course
     * @return ResponseEntity with the count of enrolled students
     * @throws ResourceNotFoundException if course not found
     */
    @GetMapping("/{courseId}/students/count")
    public ResponseEntity<Integer> getStudentCountInCourse(@PathVariable Long courseId) {
        Course course = courseService.getCourseById(courseId);
        return ResponseEntity.ok(course.getEnrolledStudentCount());
    }
}
