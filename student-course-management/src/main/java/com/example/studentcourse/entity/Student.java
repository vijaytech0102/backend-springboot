package com.example.studentcourse.entity;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

/**
 * Student Entity Class
 * 
 * This entity represents a student in the Student Course Management System.
 * It demonstrates a Many-to-Many relationship with the Course entity.
 * 
 * Key Concepts:
 * 1. @Entity: Marks this class as a JPA entity (persistent object)
 * 2. @Table(name="students"): Maps this class to the 'students' table in the database
 * 3. Many-to-Many Relationship: A student can enroll in multiple courses, and a course can have multiple students
 * 4. Set<Course>: Using Set ensures no duplicate course enrollments for a student
 * 
 * Database Mapping:
 * - Main table: students
 * - Join table: student_course (auto-created by @JoinTable)
 */
@Entity
@Table(name = "students")
public class Student {
    // ========================================================================================
    // PRIMARY KEY
    // ========================================================================================
    
    /**
     * Unique identifier for the student.
     * 
     * @Id: Marks this field as the primary key
     * @GeneratedValue(strategy = GenerationType.IDENTITY): Auto-incremented by the database
     * Using IDENTITY strategy means the database will auto-generate values (MySQL AUTO_INCREMENT)
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ========================================================================================
    // BASIC ATTRIBUTES
    // ========================================================================================
    
    /**
     * Student's email address.
     * Marked as unique to prevent duplicate emails in the database.
     * nullable=false means this field is mandatory.
     */
    @Column(name = "email", unique = true, nullable = false)
    private String email;

    /**
     * Student's full name.
     * Mandatory field required for all student records.
     */
    @Column(name = "name", nullable = false)
    private String name;

    /**
     * Student's unique roll number (e.g., "CSE001", "MEC045").
     * Marked as unique to enforce uniqueness in the database.
     */
    @Column(name = "roll_number", unique = true, nullable = false)
    private String rollNumber;

    /**
     * Student's phone number.
     * Optional field (nullable=true is the default).
     */
    @Column(name = "phone_number")
    private String phoneNumber;

    // ========================================================================================
    // MANY-TO-MANY RELATIONSHIP
    // ========================================================================================
    
    /**
     * Collection of courses the student is enrolled in.
     * 
     * MANY-TO-MANY Relationship Explanation:
     * - One Student can be enrolled in MANY Courses
     * - One Course can have MANY Students
     * - This is the OWNING side of the relationship (defined here in Student entity)
     * 
     * @ManyToMany Configuration:
     * - cascade = CascadeType.PERSIST: When a Student is saved and has existing Course objects,
     *   those Course objects are also persisted (saved to database)
     * - fetch = FetchType.LAZY: Courses are loaded from the database only when explicitly accessed
     *   (improves performance by not loading all courses immediately)
     * 
     * @JoinTable Configuration (creates a separate join table):
     * - name = "student_course": The name of the join table in the database
     * - joinColumns: References to Student (via student_id column)
     * - inverseJoinColumns: References to Course (via course_id column)
     * 
     * Join Table Structure:
     *   CREATE TABLE student_course (
     *       student_id BIGINT,
     *       course_id BIGINT,
     *       PRIMARY KEY (student_id, course_id),
     *       FOREIGN KEY (student_id) REFERENCES students(id),
     *       FOREIGN KEY (course_id) REFERENCES courses(id)
     *   );
     * 
     * Why Set instead of List?
     * - Set prevents duplicate course enrollments (a Set cannot contain duplicates)
     * - List would allow the same course to be enrolled multiple times
     * - HashSet provides O(1) lookup performance
     */
    @JsonManagedReference
    @ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinTable(
            name = "student_course",
            joinColumns = @JoinColumn(name = "student_id"),
            inverseJoinColumns = @JoinColumn(name = "course_id")
    )
    private Set<Course> courses = new HashSet<>();

    // ========================================================================================
    // AUDIT FIELDS (timestamps)
    // ========================================================================================
    
    /**
     * Timestamp when the student record was first created.
     * updatable = false ensures this value never changes after initial creation.
     */
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * Timestamp when the student record was last updated.
     * Automatically updated whenever the record is modified.
     */
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // ========================================================================================
    // JPA LIFECYCLE CALLBACKS
    // ========================================================================================
    
    /**
     * JPA Lifecycle Callback - Called BEFORE the entity is persisted (inserted) into the database.
     * Automatically sets creation and update timestamps.
     */
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * JPA Lifecycle Callback - Called BEFORE the entity is updated in the database.
     * Automatically updates the modification timestamp.
     */
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // ========================================================================================
    // CONSTRUCTORS
    // ========================================================================================
    
    /**
     * Default no-argument constructor.
     * Required by JPA/Hibernate for entity instantiation.
     */
    public Student() {
    }

    /**
     * Constructor with all fields.
     * Useful for creating Student objects with initial values.
     * Note: ID is automatically generated and should NOT be set in the constructor
     * 
     * @param email Student's email (must be unique)
     * @param name Student's full name
     * @param rollNumber Student's roll number (must be unique)
     * @param phoneNumber Student's phone number
     */
    public Student(String email, String name, String rollNumber, String phoneNumber) {
        this.email = email;
        this.name = name;
        this.rollNumber = rollNumber;
        this.phoneNumber = phoneNumber;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRollNumber() {
        return rollNumber;
    }

    public void setRollNumber(String rollNumber) {
        this.rollNumber = rollNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Set<Course> getCourses() {
        return courses;
    }

    public void setCourses(Set<Course> courses) {
        this.courses = courses;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    // ========================================================================================
    // BUSINESS LOGIC METHODS
    // ========================================================================================
    
    /**
     * Enrolls a student in a course (establishes the Many-to-Many relationship).
     * 
     * This method maintains BIDIRECTIONAL consistency:
     * - Adds the course to the student's course set
     * - Adds the student to the course's student set
     * - Prevents duplicate enrollments by checking if course already exists
     * 
     * Important: This method must maintain consistency on BOTH sides of the relationship.
     * If you only add to this.courses but not to course.students, the relationship becomes
     * inconsistent in memory (though the database will reflect it correctly).
     * 
     * @param course The course to enroll the student in
     */
    public void enrollCourse(Course course) {
        if (course != null && !this.courses.contains(course)) {
            this.courses.add(course);
            course.getStudents().add(this);
        }
    }

    /**
     * Removes a student from a course (removes the Many-to-Many relationship).
     * 
     * This method maintains BIDIRECTIONAL consistency:
     * - Removes the course from the student's course set
     * - Removes the student from the course's student set
     * - Only attempts removal if the student is actually enrolled
     * 
     * @param course The course to remove the student from
     */
    public void unrollCourse(Course course) {
        if (course != null && this.courses.contains(course)) {
            this.courses.remove(course);
            course.getStudents().remove(this);
        }
    }
}
