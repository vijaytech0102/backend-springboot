package com.example.studentcourse.entity;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

/**
 * Course Entity Class
 * 
 * This entity represents a course in the Student Course Management System.
 * It demonstrates the INVERSE side of a Many-to-Many relationship with the Student entity.
 * 
 * Key Concepts:
 * 1. @Entity: Marks this class as a JPA entity (persistent object)
 * 2. @Table(name="courses"): Maps this class to the 'courses' table in the database
 * 3. Many-to-Many Relationship (Inverse Side): A course has multiple students, and these are
 *    accessed here. However, the relationship is OWNED by the Student entity.
 * 4. Set<Student>: Using Set ensures no duplicate student enrollments in a course
 * 
 * IMPORTANT RELATIONSHIP CONCEPT:
 * ====================================
 * In a Many-to-Many relationship, there are TWO sides:
 * 
 * OWNING SIDE (Student.java):
 * - This side owns the relationship
 * - Uses @JoinTable to define the join table structure
 * - When you update this side, the join table is updated
 * 
 * INVERSE SIDE (Course.java - THIS CLASS):
 * - This side references back to the owning side
 * - Uses mappedBy parameter to indicate it's not responsible for the relationship
 * - Read-only on the JPA side (updates here don't affect the database)
 * - You should always modify the relationship through the owning side (Student)
 * 
 * Database Mapping:
 * - Main table: courses
 * - Join table: student_course (defined in Student entity, auto-created by Hibernate)
 */
@Entity
@Table(name = "courses")
public class Course {
    // ========================================================================================
    // PRIMARY KEY
    // ========================================================================================
    
    /**
     * Unique identifier for the course.
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
     * Unique course code (e.g., "CS101", "MATH201").
     * Marked as unique to prevent duplicate course codes in the database.
     */
    @Column(name = "course_code", unique = true, nullable = false)
    private String courseCode;

    /**
     * Course name or title (e.g., "Introduction to Java Programming").
     * Mandatory field required for all course records.
     */
    @Column(name = "course_name", nullable = false)
    private String courseName;

    /**
     * Detailed description of the course content and learning objectives.
     * Optional field providing additional information about the course.
     */
    @Column(name = "description")
    private String description;

    /**
     * Number of academic credits for this course.
     * Typically ranges from 1-4 credits per course.
     */
    @Column(name = "credits")
    private Integer credits;

    /**
     * Name of the instructor teaching this course.
     * Optional field to identify who teaches the course.
     */
    @Column(name = "instructor_name")
    private String instructorName;

    /**
     * Maximum number of students allowed in this course.
     * Used to enforce course capacity constraints.
     * If null, the course has unlimited enrollment capacity.
     */
    @Column(name = "max_students")
    private Integer maxStudents;

    // ========================================================================================
    // MANY-TO-MANY RELATIONSHIP (INVERSE SIDE)
    // ========================================================================================
    
    /**
     * Collection of students enrolled in this course.
     * 
     * This represents the INVERSE SIDE of the Many-to-Many relationship.
     * The owning side is defined in Student.java
     * 
     * @ManyToMany Configuration:
     * - cascade = CascadeType.PERSIST: When a Course is saved and has existing Student objects,
     *   those Student objects are also persisted (saved to database)
     * - fetch = FetchType.LAZY: Students are loaded from the database only when explicitly accessed
     * - mappedBy = "courses": Indicates that this side does NOT own the relationship.
     *   The relationship is managed by the 'courses' field in the Student entity.
     *   This tells Hibernate to look at Student.courses to find the @JoinTable configuration.
     * 
     * IMPORTANT: Because this is the inverse side (mappedBy is used):
     * - If you add/remove Students from this Set in memory, IT WILL NOT be persisted to the database
     * - You MUST use Student.enrollCourse() or Student.unrollCourse() to modify the relationship
     * - This Set is READ-ONLY from a persistence perspective
     * - But it IS updated automatically in memory when you use the Student methods
     * 
     * Example of CORRECT usage:
     *   Student student = new Student(...);
     *   Course course = new Course(...);
     *   student.enrollCourse(course);  // ✓ CORRECT - modifies Student side (owning side)
     *   studentRepository.save(student); // Relationship is now persisted
     * 
     * Example of INCORRECT usage (won't work):
     *   course.getStudents().add(student);  // ✗ WRONG - modifies Course side (inverse side)
     *   courseRepository.save(course);      // Relationship is NOT persisted to database
     */
    /**
     * JSON SERIALIZATION ANNOTATIONS - @JsonBackReference:
     * =====================================================
     * 
     * @JsonBackReference(value = "student_courses"):
     * 
     * Purpose:
     * - Prevents this field from being serialized to JSON
     * - Part of a bidirectional relationship pair with @JsonManagedReference
     * - Avoids INFINITE CIRCULAR REFERENCE in JSON output
     * 
     * Why It's Needed:
     * Without @JsonBackReference, serializing a Course would cause:
     *   Course → includes students → each Student → includes courses → includes Course AGAIN
     *   This creates infinite recursion → Stack overflow!
     * 
     * How It Works:
     * The value "student_courses" MUST MATCH the @JsonManagedReference value in Student.java
     * This pairing tells Jackson:
     *   - MANAGED SIDE (Student.courses): Serialize to JSON ✓
     *   - BACK REFERENCE SIDE (Course.students): Do NOT serialize ✗
     * 
     * Data Flow in JSON:
     *   When serializing Student.getCourses() → includes Course objects
     *   When serializing those Course objects → students field is SKIPPED (circular reference prevented!)
     * 
     * Example JSON Output:
     *   When Course is serialized directly:
     *   {
     *     "id": 101,
     *     "courseName": "Java Programming",
     *     "students": null  // ← NOT included due to @JsonBackReference
     *   }
     * 
     *   When Course is serialized as part of Student:
     *   {
     *     "id": 1,
     *     "name": "John",
     *     "courses": [
     *       {
     *         "id": 101,
     *         "courseName": "Java Programming"
     *         // students field is NOT here (prevents circular reference)
     *       }
     *     ]
     *   }
     * 
     * IMPORTANT RULES:
     * - MUST have matching value with @JsonManagedReference in Student.courses
     * - Always placed on the INVERSE side of @ManyToMany (where mappedBy is used)
     * - Do NOT remove this annotation (will cause infinite JSON loops)
     * - If value doesn't match Student's annotation, serialization won't work correctly
     */
    /**
     * WHAT IS value = "student_courses"?
     * 
     * It's a LABEL/IDENTIFIER that pairs this @JsonBackReference with the matching
     * @JsonManagedReference in Student.java. Think of it as a "connection tag" or
     * "reference name" that tells Jackson: "This back reference is paired with the
     * managed reference that has the same value."
     * 
     * HOW IT WORKS:
     * ===============
     * value = "student_courses" (in Course.java @JsonBackReference)
     *         ↓ MUST MATCH ↓
     * value = "student_courses" (in Student.java @JsonManagedReference)
     * 
     * When Jackson serializes, it:
     *   1. Looks at Student with @JsonManagedReference("student_courses")
     *   2. Serializes the courses field (includes it in JSON)
     *   3. For each Course, looks for @JsonBackReference("student_courses")
     *   4. Finds the match! → Jackson SKIPS serializing the students field
     *   5. Result: No infinite loop ✓
     * 
     * NAMING CONVENTION:
     * ==================
     * Use descriptive names that show the relationship:
     *   - "student_courses" ← Shows relationship between students and courses
     *   - "author_books" ← Shows relationship between authors and books
     *   - "teacher_students" ← Shows relationship between teachers and students
     *   - "employer_employees" ← Shows relationship between employers and employees
     * 
     * WHAT IF VALUES DON'T MATCH?
     * =============================
     * If you write:
     *   Student: @JsonManagedReference("student_courses")
     *   Course: @JsonBackReference("course_students")  ← Different value!
     * 
     * Result: Jackson doesn't recognize them as a pair
     *         → Circular reference NOT prevented
     *         → Infinite JSON loop
     *         → Stack overflow error
     * 
     * EXAMPLE:
     * ========
     * Correct Pairing:
     *   Student.java:  @JsonManagedReference(value = "student_courses")
     *   Course.java:   @JsonBackReference(value = "student_courses")
     *   Status: ✓ WORKS - Jackson recognizes the pair
     * 
     * Wrong Pairing:
     *   Student.java:  @JsonManagedReference(value = "student_courses")
     *   Course.java:   @JsonBackReference(value = "different_value")
     *   Status: ✗ BROKEN - Jackson doesn't match them → infinite loop
     * 
     * KEY RULES TO REMEMBER:
     * ======================
     * 1. The value MUST be identical in both @JsonManagedReference and @JsonBackReference
     * 2. If you change it in one place, ALWAYS change it in the other
     * 3. Use meaningful names that describe the relationship
     * 4. Two annotations with same value = ONE complete pair
     */
    @JsonIgnore
    @JsonBackReference(value = "student_courses")  // ← Paired with Student.courses
    @ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY, mappedBy = "courses")
    private Set<Student> students = new HashSet<>();

    // ========================================================================================
    // AUDIT FIELDS (timestamps)
    // ========================================================================================
    
    /**
     * Timestamp when the course record was first created.
     * updatable = false ensures this value never changes after initial creation.
     */
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * Timestamp when the course record was last updated.
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
    public Course() {
    }

    /**
     * Constructor with all course information fields.
     * Useful for creating Course objects with initial values.
     * Note: ID is automatically generated and should NOT be set in the constructor
     * 
     * @param courseCode Unique code for the course (e.g., "CS101")
     * @param courseName Name/title of the course
     * @param description Description of the course content
     * @param credits Number of academic credits
     * @param instructorName Name of the instructor
     * @param maxStudents Maximum enrollment capacity (null for unlimited)
     */
    public Course(String courseCode, String courseName, String description, Integer credits, 
                  String instructorName, Integer maxStudents) {
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.description = description;
        this.credits = credits;
        this.instructorName = instructorName;
        this.maxStudents = maxStudents;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getCredits() {
        return credits;
    }

    public void setCredits(Integer credits) {
        this.credits = credits;
    }

    public String getInstructorName() {
        return instructorName;
    }

    public void setInstructorName(String instructorName) {
        this.instructorName = instructorName;
    }

    public Integer getMaxStudents() {
        return maxStudents;
    }

    public void setMaxStudents(Integer maxStudents) {
        this.maxStudents = maxStudents;
    }

    public Set<Student> getStudents() {
        return students;
    }

    public void setStudents(Set<Student> students) {
        this.students = students;
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
    // BUSINESS LOGIC METHODS - Helper methods for course management
    // ========================================================================================
    
    /**
     * Gets the current number of students enrolled in this course.
     * 
     * This method provides a convenient way to check enrollment numbers
     * and is typically used to enforce course capacity constraints.
     * 
     * @return Number of currently enrolled students (0 if students set is null)
     */
    public int getEnrolledStudentCount() {
        return students != null ? students.size() : 0;
    }

    /**
     * Checks if the course has available slots for new student enrollment.
     * 
     * This method is used to validate enrollment requests against course capacity.
     * A course has available slots if:
     * - maxStudents is null (unlimited capacity), OR
     * - Current enrollment count is less than maxStudents limit
     * 
     * Usage Example:
     *   if (course.hasAvailableSlots()) {
     *       student.enrollCourse(course);  // Enroll in course
     *   } else {
     *       throw new CourseCapacityException("Course is full");
     *   }
     * 
     * @return true if the course has available slots, false if at or over capacity
     */
    public boolean hasAvailableSlots() {
        if (maxStudents == null) {
            return true;  // Unlimited capacity
        }
        return getEnrolledStudentCount() < maxStudents;
    }

    /**
     * Checks if a specific student is currently enrolled in this course.
     * 
     * This method provides a quick lookup to determine student enrollment status
     * without iterating through the students collection manually.
     * 
     * Usage Example:
     *   if (course.isStudentEnrolled(student)) {
     *       System.out.println("Student is enrolled");
     *   } else {
     *       System.out.println("Student is not enrolled");
     *   }
     * 
     * @param student The student to check
     * @return true if the student is enrolled in this course, false otherwise
     */
    public boolean isStudentEnrolled(Student student) {
        return students != null && students.contains(student);
    }
}
