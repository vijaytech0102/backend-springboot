# Quick Start Guide - Student Course Management System

## ⏱️ 5-Minute Setup

### Step 1: Prerequisites Check (1 minute)

Verify you have installed:
```bash
# Check Java version (must be 17+)
java -version

# Check Maven version
mvn -version

# Check MySQL
mysql --version

# And MySQL service should be running
```

### Step 2: Create Database (1 minute)

Open MySQL and execute:
```sql
CREATE DATABASE student_course_db;
```

### Step 3: Configure Application (1 minute)

Edit `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/student_course_db?createDatabaseIfNotExist=true
spring.datasource.username=root
spring.datasource.password=       (leave blank if no password)
```

### Step 4: Build & Run (2 minutes)

```bash
mvn clean install
mvn spring-boot:run
```

✅ **Application is running at http://localhost:8080**

---

## 🧪 Quick Test

### Test with cURL

```bash
# 1. Create a Student
curl -X POST http://localhost:8080/api/students \
  -H "Content-Type: application/json" \
  -d '{"name":"Ali Khan","email":"ali@example.com","rollNumber":"STU001","phoneNumber":"9876543210"}'

# 2. Create a Course
curl -X POST http://localhost:8080/api/courses \
  -H "Content-Type: application/json" \
  -d '{"courseCode":"CS101","courseName":"Java Programming","description":"Learn Java","credits":3,"instructorName":"Dr. Ahmed","maxStudents":50}'

# 3. Enroll Student in Course
curl -X POST http://localhost:8080/api/courses/1/students/1

# 4. Get all Students in Course
curl -X GET http://localhost:8080/api/courses/1/students

# 5. Get all Courses
curl -X GET http://localhost:8080/api/courses
```

---

## 📂 Project Files Explained

### Entities (Many-to-Many Mapping)
- **Student.java**: Student entity with @ManyToMany relationship to courses
- **Course.java**: Course entity with inverse @ManyToMany relationship

### Exception Handling
- **ResourceNotFoundException.java**: When resource not found (404)
- **DuplicateResourceException.java**: When duplicate created (409)
- **CourseCapacityException.java**: When course is full (400)
- **GlobalExceptionHandler.java**: Centralized exception handling

### Business Logic
- **StudentService.java**: Create, read, update, delete students
- **CourseService.java**: Manage courses + student enrollments

### REST API
- **StudentController.java**: Student endpoints
- **CourseController.java**: Course + enrollment endpoints

### Database
- **StudentRepository.java**: Database queries for students
- **CourseRepository.java**: Database queries for courses

---

## 🎯 Key Concepts Demonstrated

### 1. Many-to-Many Relationship
```
Student (Many) ←→ (Many) Course

A student can take multiple courses.
A course can have multiple students.
Relationship stored in 'student_course' join table.
```

### 2. Exception Handling
```
Controller → Service throws custom exception
                            ↓
        GlobalExceptionHandler catches it
                            ↓
        Returns formatted error response
```

### 3. Layered Architecture
```
Controller (REST Endpoints)
    ↓
Service (Business Logic)
    ↓
Repository (Database Access)
    ↓
Database (MySQL)
```

---

## 📊 Database Schema

```
students               courses            student_course (Join Table)
├─ id                  ├─ id              ├─ student_id (FK)
├─ name                ├─ course_code     └─ course_id (FK)
├─ email (UNIQUE)      ├─ course_name
├─ roll_number         ├─ credits
├─ phone_number        ├─ instructor_name
├─ created_at          └─ max_students
└─ updated_at
```

---

## ❌ Common Issues & Solutions

| Issue | Solution |
|-------|----------|
| "Can't connect to MySQL" | Ensure MySQL server is running: `sudo service mysql start` |
| "Database not found" | Create database: `CREATE DATABASE student_course_db;` |
| "Port 8080 in use" | Change in application.properties: `server.port=8081` |
| "Connection refused" | Check MySQL username/password in application.properties |

---

## 📚 Learning Resources

Inside the project:
- Read comments in Java files for detailed explanations
- Check `README.md` for comprehensive documentation
- Review `application.properties` for configuration options

---

## 🎓 Student Tasks

1. ✅ Get the application running
2. ✅ Create 3 students with different emails
3. ✅ Create 3 courses
4. ✅ Enroll students in courses (one student in multiple courses)
5. ✅ Try creating duplicate student (observe exception handling)
6. ✅ Try enrolling in non-existent course (observe exception)
7. ✅ View enrolled students in a course
8. ✅ **Extend**: Add date/time for course enrollment

---

## 💻 Using Postman (Optional)

1. Download: https://www.postman.com/downloads/
2. Create requests for each API endpoint
3. Test successful requests and error scenarios
4. Observe response status codes and messages

---

**You're ready to learn! Start with the Student endpoints first, then Course endpoints, then Enrollment (Many-to-Many) endpoints.**
