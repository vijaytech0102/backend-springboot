package com.example.studentcourse.repository;

import com.example.studentcourse.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * CourseRepository Interface
 * Extends JpaRepository for database operations on Course entity
 * 
 * JpaRepository provides:
 * - save(), saveAll()
 * - delete(), deleteAll()
 * - findById(), findAll()
 * - And many more CRUD operations
 * 
 * Custom query methods can be added here for specialized queries
 */
@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    /**
     * Custom query method: Find a course by course code
     * Spring Data JPA automatically generates the SQL query based on the method name
     * 
     * Equivalent SQL: SELECT * FROM courses WHERE course_code = ?
     * 
     * @param courseCode The course code (e.g., CS101)
     * @return Optional containing the course if found, empty otherwise
     */
    Optional<Course> findByCourseCode(String courseCode);

    /**
     * Custom query method: Check if a course exists by course code
     * 
     * @param courseCode The course code to check
     * @return true if course exists, false otherwise
     */
    boolean existsByCourseCode(String courseCode);
}
