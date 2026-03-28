# Student Course Management System

A comprehensive Spring Boot application demonstrating **Many-to-Many relationship mapping** and **Exception handling** with MySQL database.

## Project Overview

This project is designed as an educational tool to help students understand:
1. **Many-to-Many Relationship**: How multiple students can enroll in multiple courses
2. **Exception Handling**: Custom exceptions and centralized exception handling
3. **Spring Boot Development**: Complete REST API with proper architecture

### Real-World Scenario
A university system where:
- Students can enroll in multiple courses
- Courses can have multiple students
- The system prevents duplicate enrollments and handles various error scenarios

---

## 📋 Prerequisites

Before running this application, ensure you have installed:

1. **Java 17 or higher**
   - Download from: https://adoptopenjdk.net/ or https://www.oracle.com/java/

2. **Maven 3.6 or higher**
   - Download from: https://maven.apache.org/download.cgi

3. **MySQL Server 8.0 or higher**
   - Download from: https://dev.mysql.com/downloads/mysql/
   - Start the MySQL service

4. **A Text Editor or IDE** (Recommended: IntelliJ IDEA or VS Code)

---

## 🚀 Setup and Installation

### Step 1: Create MySQL Database

Open MySQL command line or MySQL Workbench and execute:

```sql
CREATE DATABASE student_course_db;
USE student_course_db;
```

The database will be populated automatically by Hibernate when the application starts.

### Step 2: Update Database Configuration

Edit `src/main/resources/application.properties`:

```properties
# Database URL - change if your MySQL is on a different port
spring.datasource.url=jdbc:mysql://localhost:3306/student_course_db?createDatabaseIfNotExist=true

# Database username (default: root)
spring.datasource.username=root

# Database password (default: empty)
spring.datasource.password=
```

### Step 3: Build the Project

Navigate to the project directory and run:

```bash
mvn clean install
```

or using Maven wrapper (if on Windows):

```bash
mvnw clean install
```

### Step 4: Run the Application

```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

---

## 📚 Project Structure

```
student-course-management/
│
├── src/
│   │
│   ├── main/
│   │   ├── java/com/example/studentcourse/
│   │   │   ├── entity/
│   │   │   │   ├── Student.java          # Student entity with @ManyToMany
│   │   │   │   └── Course.java           # Course entity with @ManyToMany (inverse)
│   │   │   │
│   │   │   ├── repository/
│   │   │   │   ├── StudentRepository.java # Database access for students
│   │   │   │   └── CourseRepository.java  # Database access for courses
│   │   │   │
│   │   │   ├── service/
│   │   │   │   ├── StudentService.java   # Business logic for students
│   │   │   │   └── CourseService.java    # Business logic for courses + enrollment
│   │   │   │
│   │   │   ├── controller/
│   │   │   │   ├── StudentController.java # REST endpoints for students
│   │   │   │   └── CourseController.java  # REST endpoints for courses + enrollment
│   │   │   │
│   │   │   ├── exception/
│   │   │   │   ├── ResourceNotFoundException.java       # Custom exception
│   │   │   │   ├── DuplicateResourceException.java      # Custom exception
│   │   │   │   ├── CourseCapacityException.java         # Custom exception
│   │   │   │   └── GlobalExceptionHandler.java          # Centralized exception handling
│   │   │   │
│   │   │   └── StudentCourseApplication.java # Main Spring Boot class
│   │   │
│   │   └── resources/
│   │       └── application.properties    # Configuration file
│   │
│   └── test/
│       └── java/com/example/studentcourse/
│           └── StudentCourseApplicationTests.java
│
├── pom.xml                              # Maven configuration
└── README.md                            # This file
```

---

## 🔑 Key Concepts Explained

### 1. Many-to-Many Relationship

#### What is Many-to-Many?
- One Student can enroll in many Courses
- One Course can have many Students
- Stored in a join table (`student_course`) in the database

#### In Student Entity:
```java
@ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
@JoinTable(
    name = "student_course",
    joinColumns = @JoinColumn(name = "student_id"),
    inverseJoinColumns = @JoinColumn(name = "course_id")
)
private Set<Course> courses = new HashSet<>();
```

**Explanation:**
- `@ManyToMany`: Defines many-to-many relationship
- `@JoinTable`: Creates and defines the join table (`student_course`)
- `joinColumns`: Column in join table referencing Student
- `inverseJoinColumns`: Column in join table referencing Course
- `cascade = CascadeType.PERSIST`: When saving student, courses are also saved
- `fetch = FetchType.LAZY`: Courses are loaded only when needed (better performance)

#### In Course Entity:
```java
@ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY, mappedBy = "courses")
private Set<Student> students = new HashSet<>();
```

**Explanation:**
- This is the INVERSE side of the relationship
- `mappedBy = "courses"`: Indicates the owning side is Student.courses
- No `@JoinTable` here because it's already defined in Student

### Database Schema Generated:
```sql
-- Student Table
CREATE TABLE students (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255),
    email VARCHAR(255) UNIQUE,
    roll_number VARCHAR(255) UNIQUE,
    phone_number VARCHAR(20),
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);

-- Course Table
CREATE TABLE courses (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    course_code VARCHAR(255) UNIQUE,
    course_name VARCHAR(255),
    description TEXT,
    credits INT,
    instructor_name VARCHAR(255),
    max_students INT,
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);

-- Join Table (Many-to-Many)
CREATE TABLE student_course (
    student_id BIGINT,
    course_id BIGINT,
    PRIMARY KEY (student_id, course_id),
    FOREIGN KEY (student_id) REFERENCES students(id),
    FOREIGN KEY (course_id) REFERENCES courses(id)
);
```

---

### 2. Exception Handling

#### Custom Exceptions

**ResourceNotFoundException**
- Thrown when a Student or Course is not found
- HTTP Status: 404 (Not Found)

```java
throw new ResourceNotFoundException("Student with ID 1 not found!");
```

**DuplicateResourceException**
- Thrown when trying to create duplicate Student or Course
- HTTP Status: 409 (Conflict)

```java
throw new DuplicateResourceException(
    "Student with email 'john@example.com' already exists!"
);
```

**CourseCapacityException**
- Thrown when trying to enroll in a full course
- HTTP Status: 400 (Bad Request)

```java
throw new CourseCapacityException(
    "Course 'CS101' is at full capacity!"
);
```

#### Global Exception Handler

The `GlobalExceptionHandler` class provides centralized exception handling:

```java
@RestControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Object> handleResourceNotFoundException(...) {
        // Returns 404 with formatted error message
    }
    
    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<Object> handleDuplicateResourceException(...) {
        // Returns 409 with formatted error message
    }
    
    // ... more exception handlers
}
```

**Benefits:**
- Consistent error response format across the application
- No stack traces exposed to clients
- Centralized error handling logic
- Easy to add new exception handlers

---

## 🔌 REST API Endpoints

### Student Endpoints

| Method | Endpoint | Purpose |
|--------|----------|---------|
| POST | `/api/students` | Create a new student |
| GET | `/api/students` | Get all students |
| GET | `/api/students/{id}` | Get student by ID |
| GET | `/api/students/email/{email}` | Get student by email |
| PUT | `/api/students/{id}` | Update a student |
| DELETE | `/api/students/{id}` | Delete a student |

### Course Endpoints

| Method | Endpoint | Purpose |
|--------|----------|---------|
| POST | `/api/courses` | Create a new course |
| GET | `/api/courses` | Get all courses |
| GET | `/api/courses/{id}` | Get course by ID |
| PUT | `/api/courses/{id}` | Update a course |
| DELETE | `/api/courses/{id}` | Delete a course |

### Many-to-Many Enrollment Endpoints

| Method | Endpoint | Purpose |
|--------|----------|---------|
| POST | `/api/courses/{courseId}/students/{studentId}` | Enroll student in course |
| DELETE | `/api/courses/{courseId}/students/{studentId}` | Unenroll student from course |
| GET | `/api/courses/{courseId}/students` | Get all students in a course |
| GET | `/api/courses/{courseId}/students/count` | Get enrollment count |

---

## 📝 API Usage Examples

### 1. Create a Student

**Request:**
```bash
curl -X POST http://localhost:8080/api/students \
  -H "Content-Type: application/json" \
  -d '{
    "name": "John Doe",
    "email": "john@example.com",
    "rollNumber": "STU001",
    "phoneNumber": "9876543210"
  }'
```

**Response (201 Created):**
```json
{
    "id": 1,
    "name": "John Doe",
    "email": "john@example.com",
    "rollNumber": "STU001",
    "phoneNumber": "9876543210",
    "courses": [],
    "createdAt": "2024-03-23T10:30:00",
    "updatedAt": "2024-03-23T10:30:00"
}
```

### 2. Create a Course

**Request:**
```bash
curl -X POST http://localhost:8080/api/courses \
  -H "Content-Type: application/json" \
  -d '{
    "courseCode": "CS101",
    "courseName": "Introduction to Computer Science",
    "description": "Learn the basics of computer science",
    "credits": 3,
    "instructorName": "Dr. Smith",
    "maxStudents": 50
  }'
```

**Response (201 Created):**
```json
{
    "id": 1,
    "courseCode": "CS101",
    "courseName": "Introduction to Computer Science",
    "description": "Learn the basics of computer science",
    "credits": 3,
    "instructorName": "Dr. Smith",
    "maxStudents": 50,
    "students": [],
    "enrolledStudentCount": 0,
    "createdAt": "2024-03-23T10:31:00",
    "updatedAt": "2024-03-23T10:31:00"
}
```

### 3. Enroll Student in Course (Many-to-Many)

**Request:**
```bash
curl -X POST http://localhost:8080/api/courses/1/students/1 \
  -H "Content-Type: application/json"
```

**Response (200 OK):**
```json
{
    "id": 1,
    "courseCode": "CS101",
    "courseName": "Introduction to Computer Science",
    "description": "Learn the basics of computer science",
    "credits": 3,
    "instructorName": "Dr. Smith",
    "maxStudents": 50,
    "students": [
        {
            "id": 1,
            "name": "John Doe",
            "email": "john@example.com",
            "rollNumber": "STU001",
            "phoneNumber": "9876543210"
        }
    ],
    "enrolledStudentCount": 1,
    "createdAt": "2024-03-23T10:31:00",
    "updatedAt": "2024-03-23T10:31:00"
}
```

### 4. Error Example: Duplicate Email

**Request:**
```bash
curl -X POST http://localhost:8080/api/students \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Jane Doe",
    "email": "john@example.com",
    "rollNumber": "STU002",
    "phoneNumber": "9876543211"
  }'
```

**Response (409 Conflict):**
```json
{
    "timestamp": "2024-03-23T10:32:00",
    "status": 409,
    "error": "Duplicate Resource",
    "message": "Student with email 'john@example.com' already exists!"
}
```

### 5. Error Example: Student Not Found

**Request:**
```bash
curl -X GET http://localhost:8080/api/students/999
```

**Response (404 Not Found):**
```json
{
    "timestamp": "2024-03-23T10:33:00",
    "status": 404,
    "error": "Resource Not Found",
    "message": "Student with ID 999 not found!"
}
```

---

## 🧪 Testing the Application

### Using Postman

1. **Install Postman**: Download from https://www.postman.com/downloads/

2. **Create a new collection** called "Student Course API"

3. **Add the following requests:**
   - Create Student (POST)
   - Get All Students (GET)
   - Create Course (POST)
   - Enroll Student (POST)
   - Get Course Students (GET)

4. **Test each endpoint** and observe the responses

### Using cURL (Command Line)

See the "API Usage Examples" section above for cURL commands.

### Using Unit Tests

Create test files in `src/test/java/com/example/studentcourse/` to test your services and controllers.

---

## 💡 Learning Tasks for Students

1. **Understand Many-to-Many Relationship**
   - How is the relationship defined in entities?
   - What is the join table and why is it needed?
   - Test by enrolling multiple students in a course

2. **Understand Exception Handling**
   - Try creating duplicate students and observe the error
   - Try accessing non-existent students/courses
   - Understand how GlobalExceptionHandler works

3. **Extend the Application**
   - Add a grade/marks entity linked to Student-Course
   - Add validation annotations (@NotNull, @Email, etc.)
   - Add pagination to GET all endpoints
   - Add search functionality by course name

4. **Database Operations**
   - Write custom queries in repositories
   - Understand Hibernate generated SQL (enable logging)
   - Optimize queries with different fetch strategies

---

## 🔒 Important Notes

1. **Database Credentials**: Update `application.properties` with your MySQL credentials

2. **Hibernate DDL**: The property `spring.jpa.hibernate.ddl-auto=update` automatically creates tables. 
   - Use `create` only for development
   - Use `validate` for production

3. **MySQL Connector**: Ensure MySQL driver is in pom.xml dependencies

4. **Transaction Management**: All database modifications use `@Transactional` to ensure data consistency

---

## 📚 Further Reading

- [Spring Data JPA Documentation](https://spring.io/projects/spring-data-jpa)
- [Hibernate Many-to-Many Mapping](https://hibernate.org/orm/)
- [Spring Exception Handling](https://spring.io/blog/2013/11/01/exception-handling-in-spring-mvc)
- [MySQL Documentation](https://dev.mysql.com/doc/)

---

## ✅ Troubleshooting

### Application won't start
- Ensure MySQL server is running
- Check database credentials in application.properties
- Check if port 8080 is not in use

### Database connection error
```
com.mysql.cj.jdbc.exceptions.CommunicationsException: Communications link failure
```
**Solution**: Start MySQL server

### @ManyToMany not working
- Ensure @JoinTable is only on the owning side (Student)
- Use `mappedBy` on the inverse side (Course)
- Check table relationships in database

---

## 📧 Support

For questions and clarifications, refer to:
- Code comments in each file
- Spring Boot documentation
- Stack Overflow community

---

**Happy Learning!** 🎓
