# Help & FAQ - Student Course Management System

## ❓ Frequently Asked Questions

---

## Database Questions

### Q1: Where do I find the MySQL credentials to change?
**A:** Edit `src/main/resources/application.properties`

Look for:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/student_course_db?createDatabaseIfNotExist=true
spring.datasource.username=root
spring.datasource.password=
```

Change these values to match your MySQL setup.

---

### Q2: The application says "Can't connect to MySQL server"
**A:** Make sure:
1. MySQL server is running
   ```bash
   # On Windows
   net start MySQL80
   
   # On Mac
   brew services start mysql
   
   # On Linux
   sudo service mysql start
   ```

2. Check your credentials in `application.properties`
3. Verify MySQL is on default port 3306

---

### Q3: The database tables are not being created
**A:** Hibernate should create them automatically. If not:

Check `application.properties` has:
```properties
spring.jpa.hibernate.ddl-auto=update
```

If you want to force recreation, change to:
```properties
spring.jpa.hibernate.ddl-auto=create
```

⚠️ **Warning**: `create` will delete existing data!

---

### Q4: How do I connect to the database directly?
**A:** Open MySQL command line:
```bash
mysql -u root -p
```

Then:
```sql
USE student_course_db;
SHOW TABLES;
SELECT * FROM students;
SELECT * FROM courses;
SELECT * FROM student_course;
```

---

## Many-to-Many Questions

### Q5: What is Many-to-Many relationship?
**A:** It means:
- One Student can enroll in MANY Courses
- One Course can have MANY Students

Example:
```
Student "Ali" is in: CS101, CS102, CS103
Student "Sara" is in: CS101, MATH201

Course "CS101" has: Ali, Sara
Course "MATH201" has: Sara
```

The relationship is stored in `student_course` join table:
```
| student_id | course_id |
|---|---|
| 1 | 1 |
| 1 | 2 |
| 1 | 3 |
| 2 | 1 |
| 2 | 4 |
```

---

### Q6: Why is there a join table?
**A:** Because:
- A single database table can't store multiple values in one cell
- The join table `student_course` stores the relationships
- Each row represents one student-course pair

Without it, you'd have data duplication and inconsistency.

---

### Q7: How do I enroll a student in a course?
**A:** Use the API endpoint:
```
POST /api/courses/{courseId}/students/{studentId}
```

Example:
```bash
curl -X POST http://localhost:8080/api/courses/1/students/1 \
  -H "Content-Type: application/json"
```

Or see `CourseService.enrollStudentInCourse()` method.

---

### Q8: How do I see which courses a student is in?
**A:** Check the `courses` field when you GET a student:
```
GET /api/students/1
```

The response includes:
```json
{
    "id": 1,
    "name": "Ali",
    "courses": [
        { "id": 1, "courseCode": "CS101", ... },
        { "id": 2, "courseCode": "CS102", ... }
    ]
}
```

---

## Exception Handling Questions

### Q9: What happens if I try to create a student with duplicate email?
**A:** You get an error:

**HTTP Status**: 409 (Conflict)

**Response**:
```json
{
    "timestamp": "2024-03-23T10:30:00",
    "status": 409,
    "error": "Duplicate Resource",
    "message": "Student with email 'ali@example.com' already exists!"
}
```

This is handled by:
1. `StudentService.createStudent()` throws `DuplicateResourceException`
2. `GlobalExceptionHandler.handleDuplicateResourceException()` catches it
3. Returns formatted 409 response

---

### Q10: What if I request a student ID that doesn't exist?
**A:** You get:

**HTTP Status**: 404 (Not Found)

**Response**:
```json
{
    "timestamp": "2024-03-23T10:31:00",
    "status": 404,
    "error": "Resource Not Found",
    "message": "Student with ID 999 not found!"
}
```

This is handled by:
1. Repository returns empty Optional
2. Service throws `ResourceNotFoundException`
3. `GlobalExceptionHandler` catches and returns 404

---

### Q11: What if I enroll in a full course?
**A:** You get:

**HTTP Status**: 400 (Bad Request)

**Response**:
```json
{
    "timestamp": "2024-03-23T10:32:00",
    "status": 400,
    "error": "Course Capacity Full",
    "message": "Course 'CS101' is at full capacity!"
}
```

This is handled by:
1. `CourseService` checks `course.hasAvailableSlots()`
2. If full, throws `CourseCapacityException`
3. `GlobalExceptionHandler` catches and returns 400

---

### Q12: How does exception handling work in this project?
**A:** There are 4 levels:

**Level 1: Service Layer**
```java
public Course enrollStudentInCourse(Long studentId, Long courseId) {
    // ... validation ...
    if (!course.hasAvailableSlots()) {
        throw new CourseCapacityException("Course is full!");  // <-- Throw here
    }
}
```

**Level 2: Controller** (Just calls service)
```java
@PostMapping("/{courseId}/students/{studentId}")
public ResponseEntity<Course> enrollStudentInCourse(...) {
    Course course = courseService.enrollStudentInCourse(studentId, courseId);
    // Exception propagates up
    return ResponseEntity.ok(course);
}
```

**Level 3: Global Exception Handler** (Catches exception)
```java
@ExceptionHandler(CourseCapacityException.class)
public ResponseEntity<Object> handleCourseCapacityException(...) {
    // Create formatted error response
    Map<String, Object> body = new LinkedHashMap<>();
    body.put("status", HttpStatus.BAD_REQUEST.value());
    body.put("message", ex.getMessage());
    return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);  // <-- 400 response
}
```

**Level 4: Client** (Receives formatted error)
```json
{
    "status": 400,
    "message": "Course is full!"
}
```

**Benefit**: No try-catch need in controllers - clean code with unified error handling.

---

## API Questions

### Q13: How do I test the API?
**A:** Three ways:

**Option 1: Using cURL**
```bash
curl -X POST http://localhost:8080/api/students \
  -H "Content-Type: application/json" \
  -d '{"name":"Ali","email":"ali@example.com","rollNumber":"STU001","phoneNumber":"9876543210"}'
```

**Option 2: Using Postman**
- Download: https://www.postman.com/
- Create requests for each endpoint

**Option 3: Using Java/Spring Test**
- Write `@SpringBootTest` test methods
- See `StudentCourseApplicationTests.java`

---

### Q14: What does HTTP status code mean?
**A:**
- **200 OK**: Request succeeded
- **201 Created**: Resource was successfully created
- **204 No Content**: Successful DELETE (no body to return)
- **400 Bad Request**: Invalid request or business rule violated
- **404 Not Found**: Resource doesn't exist
- **409 Conflict**: Duplicate resource or constraint violation

---

### Q15: How do I know which endpoint to use?
**A:** Check `API_REFERENCE.md` for:
- All available endpoints
- Request/response examples
- Error scenarios
- cURL commands

---

## Code Questions

### Q16: What does @ManyToMany annotation do?
**A:** It tells Spring/Hibernate:
- This field represents a many-to-many relationship
- Multiple instances can be related to multiple instances
- Use a join table to store relationships

```java
@ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
@JoinTable(
    name = "student_course",
    joinColumns = @JoinColumn(name = "student_id"),
    inverseJoinColumns = @JoinColumn(name = "course_id")
)
private Set<Course> courses = new HashSet<>();
```

---

### Q17: What's the difference between owning and inverse side?
**A:**
- **Owning Side** (Student.java): Defines @JoinTable
  - Responsible for maintaining the database relationship
  - Has the annotation details

- **Inverse Side** (Course.java): Uses mappedBy
  - Read-only from database perspective
  - References the owning side

---

### Q18: What is @Transactional?
**A:** It ensures:
- Either ALL database operations succeed
- Or NONE of them happen (rollback on error)

```java
@Transactional
public Student createStudent(Student student) {
    // If ANY of these fail, ALL changes are rolled back
    student = studentRepository.save(student);
    // ... more operations ...
    return student;
}
```

Without it:
- If operation 2 fails, operation 1 might have already changed the database
- Data becomes inconsistent

---

### Q19: What is dependency injection?
**A:** Spring automatically gives services what they need:

```java
@Service
@RequiredArgsConstructor  // Creates constructor
public class StudentService {
    private final StudentRepository studentRepository;  // Spring injects this
    
    // Now you can use studentRepository without creating it
}
```

Benefits:
- Less boilerplate code
- Easy to test (can inject test doubles)
- Loose coupling between classes

---

### Q20: How do I extend this project?
**A:** Ideas:
1. Add Grades/Marks entity (Many-to-Many between Student-Course)
2. Add validation annotations: @NotNull, @Email, @Size
3. Add pagination: `Page<Student> findAll(Pageable pageable)`
4. Add search: `findByNameContaining(String name)`
5. Add authentication with Spring Security
6. Add API documentation with Swagger/SpringDoc

---

## Troubleshooting

### Q21: Application starts but doesn't connect to database
**A:** Check:
1. MySQL server is running
2. Database `student_course_db` exists
3. Username/password correct in `application.properties`
4. Port 3306 is not blocked by firewall

Enable SQL logging to see what's happening:
```properties
logging.level.org.hibernate.SQL=DEBUG
logging.level.org.hibernate.type.descriptor.sql.BasicBinder=TRACE
```

---

### Q22: Port 8080 already in use
**A:** Change port in `application.properties`:
```properties
server.port=8081
```

Or find and kill the process using port 8080:
```bash
# Windows
netstat -ano | findstr :8080
taskkill /PID <PID> /F

# Mac/Linux
lsof -i :8080
kill -9 <PID>
```

---

### Q23: How do I see the generated SQL?
**A:** In `application.properties`, already set:
```properties
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
```

SQL queries will appear in application logs.

---

### Q24: My changes aren't showing in the database
**A:** Possibilities:
1. Changes are not transactional (missing @Transactional)
2. Session is not flushed (add `em.flush()`)
3. Wrong database selected (check connection URL)

---

### Q25: How do I reset the database?
**A:** Delete all tables:
```bash
# Option 1: Via application.properties
spring.jpa.hibernate.ddl-auto=create  # Then restart app
```

Or manually:
```sql
DROP DATABASE student_course_db;
CREATE DATABASE student_course_db;
```

---

## 📞 Still Have Questions?

1. **Check the comments** in Java files - they explain everything
2. **Read README.md** - comprehensive documentation
3. **Check API_REFERENCE.md** - all endpoint details
4. **Look at test file** - see how things are used
5. **Google the specific error** - Spring Boot errors are well documented
6. **Check Stack Overflow** - Spring and JPA questions have many answers

---

**Good luck learning Spring Boot! 🎓**
