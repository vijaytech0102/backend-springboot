package com.example.studentcourse.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.studentcourse.entity.Course;
import com.example.studentcourse.entity.Student;
import com.example.studentcourse.exception.CourseCapacityException;
import com.example.studentcourse.exception.DuplicateResourceException;
import com.example.studentcourse.exception.ResourceNotFoundException;
import com.example.studentcourse.repository.CourseRepository;
import com.example.studentcourse.repository.StudentRepository;

/**
 * CourseService
 * 
 * This service class contains the business logic for Course-related operations
 * It also handles the many-to-many relationship between Student and Course
 * 
 * Key concepts:
 * - @Service: Marks this class as a service component
 * - @Transactional: Ensures database consistency for complex operations
 * 
 * This service demonstrates:
 * - Many-to-Many relationship management (student enrollment)
 * - Exception handling for various business scenarios
 * - Business logic validation before operations
 */
@Service
public class CourseService {

    // ========================================================================================
    // DEPENDENCY INJECTION (DI) EXPLANATION
    // ========================================================================================
    // 
    // This class demonstrates CONSTRUCTOR INJECTION - the BEST PRACTICE for Spring DI
    // 
    // WHAT IS DEPENDENCY INJECTION?
    // =============================
    // DI is a design pattern where a class receives its dependencies from external sources
    // rather than creating them itself. This promotes loose coupling and testability.
    // 
    // Instead of: private CourseRepository repo = new CourseRepository();  ❌ WRONG
    // We use:    CourseService(CourseRepository repo) { this.repo = repo; }  ✅ RIGHT
    // 
    // HOW SPRING INJECTS DEPENDENCIES:
    // =================================
    // 1. Spring sees @Service annotation → Marks this as a bean to manage
    // 2. Spring scans constructor parameters → Sees CourseRepository and StudentRepository
    // 3. Spring looks for matching beans → Finds @Repository classes
    // 4. Spring creates instances → Instantiates the repositories (since they're beans)
    // 5. Spring injects them → Passes to this constructor automatically
    // 6. Result → This service has access to both repositories without creating them!
    // 
    // WHY CONSTRUCTOR INJECTION (BEST PRACTICE)?
    // ==========================================
    // • Dependencies are IMMUTABLE (marked as 'final') - cannot be changed after creation
    // • Dependencies are VISIBLE - clearly shown in constructor signature
    // • Easy to TEST - just pass mock objects to constructor in unit tests
    // • All dependencies available immediately after instantiation
    // • No coupling to Spring Framework (could work with any DI framework)
    // 
    // ALTERNATIVE APPROACHES (NOT USED HERE):
    // =========================================
    // 
    // 1. FIELD INJECTION (Commented out below - not recommended):
    //    @Autowired
    //    private CourseRepository courseRepository;
    //    Problems:
    //    - Not immutable (can be changed anytime)
    //    - Hard to test (need Spring to inject)
    //    - Dependencies hidden (not visible at construction)
    //    - Tight coupling to Spring (@Autowired is Spring-specific)
    // 
    // 2. SETTER INJECTION:
    //    public void setCourseRepository(CourseRepository repo) { this.courseRepository = repo; }
    //    Problems:
    //    - Dependencies optional (might not be set)
    //    - Not immutable
    //    - Order of setters matters
    //    - Harder to ensure object is in valid state
    // 

    // Repository dependencies injected via constructor
    // These are IMMUTABLE (final) - set once during construction, never changes
    private final CourseRepository courseRepository;
    
    // Another repository dependency
    // Spring will automatically find and inject the StudentRepository bean
    private final StudentRepository studentRepository;
    
    /**
     * CONSTRUCTOR INJECTION - Spring automatically calls this
     * 
     * How Spring uses this constructor:
     * 1. Spring sees this constructor needs CourseRepository and StudentRepository
     * 2. Spring looks for beans that implement these interfaces
     * 3. Spring finds @Repository classes (in repository package) that match
     * 4. Spring creates instances of those repositories
     * 5. Spring calls this constructor and passes the repository instances
     * 6. The repositories are now available throughout this service
     * 
     * The constructor parameters act as DEPENDENCY DECLARATIONS:
     * "This service REQUIRES CourseRepository and StudentRepository to function"
     * 
     * Benefits of declaring dependencies in constructor:
     * - Makes it clear what this class depends on
     * - Spring validates all dependencies exist (fails fast if missing)
     * - Easy to test: new CourseService(mockCourseRepo, mockStudentRepo)
     * - Immutable: repositories can't be changed after object creation
     * 
     * @param courseRepository The CourseRepository bean (injected by Spring)
     * @param studentRepository The StudentRepository bean (injected by Spring)
     */
    public CourseService(CourseRepository courseRepository, StudentRepository studentRepository) {
        // Spring sets these values before the object is fully constructed
        // These are now available for all methods in this service
        this.courseRepository = courseRepository;
        this.studentRepository = studentRepository;
    }

    /**
     * Creates a new course
     * 
     * Exception Handling Demo:
     * - Throws DuplicateResourceException if course code already exists
     * 
     * DEPENDENCY INJECTION IN ACTION:
     * ===============================
     * This method uses courseRepository (injected in constructor).
     * The repository was automatically provided by Spring's DI container,
     * so we can just call its methods without creating it ourselves.
     * 
     * Benefits here:
     * - If we need to change repository implementation, we only change @Repository class
     * - Easy to test by passing a mock repository
     * - Service doesn't know HOW the repository works, just THAT it works
     * 
     * @param course The course to create
     * @return The created course with generated ID
     * @throws DuplicateResourceException if course code already exists
     */
    @Transactional
    public Course createCourse(Course course) {
        // courseRepository is available here because it was injected in constructor
        if (courseRepository.existsByCourseCode(course.getCourseCode())) {
            throw new DuplicateResourceException(
                    "Course with code '" + course.getCourseCode() + "' already exists!"
            );
        }

        // If validation passes, save and return the course
        // Again using the injected repository
        return courseRepository.save(course);
    }

    /**
     * Retrieves a course by ID
     * 
     * Exception Handling Demo:
     * - Throws ResourceNotFoundException if course is not found
     * 
     * @param id The course ID
     * @return The found course
     * @throws ResourceNotFoundException if course with given ID is not found
     */
    public Course getCourseById(Long id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Course with ID " + id + " not found!"
                ));
    }

    /**
     * Retrieves a course by course code
     * 
     * @param courseCode The course code (e.g., CS101)
     * @return The found course
     * @throws ResourceNotFoundException if course with given code is not found
     */
    public Course getCourseByCourseCode(String courseCode) {
        return courseRepository.findByCourseCode(courseCode)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Course with code '" + courseCode + "' not found!"
                ));
    }

    /**
     * Retrieves all courses
     * 
     * @return List of all courses
     */
    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    /**
     * Updates an existing course
     * 
     * Exception Handling Demo:
     * - Throws ResourceNotFoundException if course is not found
     * - Throws DuplicateResourceException if course code is being changed to an existing code
     * 
     * @param id The ID of the course to update
     * @param updated The updated course data
     * @return The updated course
     * @throws ResourceNotFoundException if course not found
     * @throws DuplicateResourceException if new course code already exists
     */
    @Transactional
    public Course updateCourse(Long id, Course updated) {
        // First, find the existing course
        Course course = this.getCourseById(id);

        // Check if course code is being changed to a duplicate code
        if (!course.getCourseCode().equals(updated.getCourseCode()) && 
            courseRepository.existsByCourseCode(updated.getCourseCode())) {
            throw new DuplicateResourceException(
                    "Course with code '" + updated.getCourseCode() + "' already exists!"
            );
        }

        // Update the course fields
        course.setCourseCode(updated.getCourseCode());
        course.setCourseName(updated.getCourseName());
        course.setDescription(updated.getDescription());
        course.setCredits(updated.getCredits());
        course.setInstructorName(updated.getInstructorName());
        course.setMaxStudents(updated.getMaxStudents());

        // Save and return the updated course
        return courseRepository.save(course);
    }

    /**
     * Deletes a course by ID
     * 
     * Exception Handling Demo:
     * - Throws ResourceNotFoundException if course is not found
     * 
     * @param id The ID of the course to delete
     * @throws ResourceNotFoundException if course not found
     */
    @Transactional
    public void deleteCourse(Long id) {
        // Verify course exists before attempting to delete
        Course course = this.getCourseById(id);
        courseRepository.delete(course);
    }

    /**
     * MANY-TO-MANY OPERATION: Enrolls a student in a course
     * 
     * This method demonstrates the many-to-many relationship in action
     * A student can be enrolled in multiple courses
     * A course can have multiple students
     * 
     * DEPENDENCY INJECTION IN ACTION (Intermediate):
     * ===============================================
     * This method uses BOTH injected repositories:
     * - studentRepository.findById() → finds the student (injected via constructor)
     * - courseRepository operations → finds the course (injected via constructor)
     * 
     * Without DI, we would need to create both repositories manually:
     *   StudentRepository studentRepo = new StudentRepository();      // ❌ WRONG
     *   CourseRepository courseRepo = new CourseRepository();        // ❌ WRONG
     * 
     * With DI, they're already available:
     *   studentRepository.findById(studentId)                         // ✅ RIGHT
     *   this.getCourseById(courseId)                                 // ✅ RIGHT
     * 
     * Testing this method is easy with DI:
     *   @Test
     *   public void testEnrollment() {
     *       StudentRepository mockStudentRepo = mock(StudentRepository.class);
     *       CourseRepository mockCourseRepo = mock(CourseRepository.class);
     *       CourseService service = new CourseService(mockCourseRepo, mockStudentRepo);
     *       // Now test with mock objects that don't touch real database
     *   }
     * 
     * Exception Handling Demo:
     * - Throws ResourceNotFoundException if student or course not found
     * - Throws DuplicateResourceException if student is already enrolled
     * - Throws CourseCapacityException if course is at full capacity
     * 
     * @param studentId The ID of the student to enroll
     * @param courseId The ID of the course to enroll in
     * @return The updated course with the newly enrolled student
     * @throws ResourceNotFoundException if student or course not found
     * @throws DuplicateResourceException if student already enrolled in course
     * @throws CourseCapacityException if course is full
     */
    @Transactional
    public Course enrollStudentInCourse(Long studentId, Long courseId) {
        // Using injected studentRepository to find student
        // If not found, throws ResourceNotFoundException (handled by repository)
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Student with ID " + studentId + " not found!"
                ));

        // Using injected courseRepository (indirectly via getCourseById method)
        // This method also uses courseRepository that was injected
        Course course = this.getCourseById(courseId);

        // Check if student is already enrolled in this course
        if (course.isStudentEnrolled(student)) {
            throw new DuplicateResourceException(
                    "Student with ID " + studentId + " is already enrolled in course " + courseId
            );
        }

        // Check if course has available slots
        if (!course.hasAvailableSlots()) {
            throw new CourseCapacityException(
                    "Course '" + course.getCourseCode() + "' is at full capacity!"
            );
        }

        // All validations passed, proceed with enrollment
        // Using the helper method in Student to manage the relationship properly
        student.enrollCourse(course);

        // Save the changes to the database using injected studentRepository
        studentRepository.save(student);

        return course;
    }

    /**
     * MANY-TO-MANY OPERATION: Unenrolls a student from a course
     * 
     * This method demonstrates removing from a many-to-many relationship
     * 
     * Exception Handling Demo:
     * - Throws ResourceNotFoundException if student, course not found, or not enrolled
     * 
     * @param studentId The ID of the student to unenroll
     * @param courseId The ID of the course to unenroll from
     * @return The updated course
     * @throws ResourceNotFoundException if student, course not found or student not enrolled
     */
    @Transactional
    public Course unrollStudentFromCourse(Long studentId, Long courseId) {
        // Get the student
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Student with ID " + studentId + " not found!"
                ));

        // Get the course
        Course course = this.getCourseById(courseId);

        // Check if student is enrolled in this course
        if (!course.isStudentEnrolled(student)) {
            throw new ResourceNotFoundException(
                    "Student with ID " + studentId + " is not enrolled in course " + courseId
            );
        }

        // Remove the enrollment using the helper method
        student.unrollCourse(course);

        // Save the changes to the database
        studentRepository.save(student);

        return course;
    }

    /**
     * Gets a list of all students enrolled in a course
     * 
     * Exception Handling Demo:
     * - Throws ResourceNotFoundException if course not found
     * 
     * @param courseId The ID of the course
     * @return List of students enrolled in the course
     * @throws ResourceNotFoundException if course not found
     */
    public List<Student> getStudentsEnrolledInCourse(Long courseId) {
        Course course = this.getCourseById(courseId);
        return course.getStudents().stream().toList();
    }

    /**
     * Gets a list of all courses a student is enrolled in
     * 
     * Exception Handling Demo:
     * - Throws ResourceNotFoundException if student not found
     * 
     * @param studentId The ID of the student
     * @return List of courses the student is enrolled in
     * @throws ResourceNotFoundException if student not found
     */
    public List<Course> getCoursesForStudent(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Student with ID " + studentId + " not found!"
                ));
        return student.getCourses().stream().toList();
    }
}
