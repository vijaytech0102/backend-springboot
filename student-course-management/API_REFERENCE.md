# API Reference - Student Course Management System

## 📌 Base URL
```
http://localhost:8080/api
```

---

## 👥 STUDENT ENDPOINTS

### 1. Create Student
```
POST /students
Content-Type: application/json

Request Body:
{
    "name": "Ali Khan",
    "email": "ali@example.com",
    "rollNumber": "STU001",
    "phoneNumber": "9876543210"
}

Response: 201 Created
{
    "id": 1,
    "name": "Ali Khan",
    "email": "ali@example.com",
    "rollNumber": "STU001",
    "phoneNumber": "9876543210",
    "courses": [],
    "createdAt": "2024-03-23T10:30:00",
    "updatedAt": "2024-03-23T10:30:00"
}

Error: 409 Conflict (if email or roll number already exists)
{
    "timestamp": "2024-03-23T10:30:00",
    "status": 409,
    "error": "Duplicate Resource",
    "message": "Student with email 'ali@example.com' already exists!"
}
```

### 2. Get All Students
```
GET /students

Response: 200 OK
[
    {
        "id": 1,
        "name": "Ali Khan",
        "email": "ali@example.com",
        "rollNumber": "STU001",
        "phoneNumber": "9876543210",
        "courses": [],
        "createdAt": "2024-03-23T10:30:00",
        "updatedAt": "2024-03-23T10:30:00"
    },
    ...
]
```

### 3. Get Student by ID
```
GET /students/1

Response: 200 OK
{
    "id": 1,
    "name": "Ali Khan",
    "email": "ali@example.com",
    "rollNumber": "STU001",
    "phoneNumber": "9876543210",
    "courses": [],
    "createdAt": "2024-03-23T10:30:00",
    "updatedAt": "2024-03-23T10:30:00"
}

Error: 404 Not Found
{
    "timestamp": "2024-03-23T10:31:00",
    "status": 404,
    "error": "Resource Not Found",
    "message": "Student with ID 1 not found!"
}
```

### 4. Get Student by Email
```
GET /students/email/ali@example.com

Response: 200 OK
{
    "id": 1,
    "name": "Ali Khan",
    "email": "ali@example.com",
    "rollNumber": "STU001",
    "phoneNumber": "9876543210",
    "courses": [],
    "createdAt": "2024-03-23T10:30:00",
    "updatedAt": "2024-03-23T10:30:00"
}

Error: 404 Not Found
{
    "timestamp": "2024-03-23T10:31:00",
    "status": 404,
    "error": "Resource Not Found",
    "message": "Student with email 'invalid@example.com' not found!"
}
```

### 5. Update Student
```
PUT /students/1
Content-Type: application/json

Request Body:
{
    "name": "Ali Khan Updated",
    "email": "newemail@example.com",
    "rollNumber": "STU001",
    "phoneNumber": "1234567890"
}

Response: 200 OK
{
    "id": 1,
    "name": "Ali Khan Updated",
    "email": "newemail@example.com",
    "rollNumber": "STU001",
    "phoneNumber": "1234567890",
    "courses": [],
    "createdAt": "2024-03-23T10:30:00",
    "updatedAt": "2024-03-23T10:32:00"
}

Error: 404 Not Found
Error: 409 Conflict (if new email already exists)
```

### 6. Delete Student
```
DELETE /students/1

Response: 204 No Content
(Empty response body)

Error: 404 Not Found
{
    "timestamp": "2024-03-23T10:33:00",
    "status": 404,
    "error": "Resource Not Found",
    "message": "Student with ID 1 not found!"
}
```

---

## 📚 COURSE ENDPOINTS

### 1. Create Course
```
POST /courses
Content-Type: application/json

Request Body:
{
    "courseCode": "CS101",
    "courseName": "Introduction to Computer Science",
    "description": "Learn the basics of CS",
    "credits": 3,
    "instructorName": "Dr. Ahmed",
    "maxStudents": 50
}

Response: 201 Created
{
    "id": 1,
    "courseCode": "CS101",
    "courseName": "Introduction to Computer Science",
    "description": "Learn the basics of CS",
    "credits": 3,
    "instructorName": "Dr. Ahmed",
    "maxStudents": 50,
    "students": [],
    "enrolledStudentCount": 0,
    "createdAt": "2024-03-23T10:40:00",
    "updatedAt": "2024-03-23T10:40:00"
}

Error: 409 Conflict (if course code already exists)
```

### 2. Get All Courses
```
GET /courses

Response: 200 OK
[
    {
        "id": 1,
        "courseCode": "CS101",
        "courseName": "Introduction to Computer Science",
        "description": "Learn the basics of CS",
        "credits": 3,
        "instructorName": "Dr. Ahmed",
        "maxStudents": 50,
        "students": [],
        "enrolledStudentCount": 0,
        "createdAt": "2024-03-23T10:40:00",
        "updatedAt": "2024-03-23T10:40:00"
    },
    ...
]
```

### 3. Get Course by ID
```
GET /courses/1

Response: 200 OK
{
    "id": 1,
    "courseCode": "CS101",
    "courseName": "Introduction to Computer Science",
    "description": "Learn the basics of CS",
    "credits": 3,
    "instructorName": "Dr. Ahmed",
    "maxStudents": 50,
    "students": [],
    "enrolledStudentCount": 0,
    "createdAt": "2024-03-23T10:40:00",
    "updatedAt": "2024-03-23T10:40:00"
}

Error: 404 Not Found
```

### 4. Update Course
```
PUT /courses/1
Content-Type: application/json

Request Body:
{
    "courseCode": "CS101",
    "courseName": "Advanced Computer Science",
    "description": "Updated description",
    "credits": 4,
    "instructorName": "Dr. Ahmed",
    "maxStudents": 60
}

Response: 200 OK
(Updated course object)

Error: 404 Not Found
Error: 409 Conflict (if new course code already exists)
```

### 5. Delete Course
```
DELETE /courses/1

Response: 204 No Content
(Empty response body)

Error: 404 Not Found
```

---

## 🎓 ENROLLMENT ENDPOINTS (Many-to-Many)

### 1. Enroll Student in Course
```
POST /courses/1/students/1
Content-Type: application/json

Response: 200 OK
{
    "id": 1,
    "courseCode": "CS101",
    "courseName": "Introduction to Computer Science",
    "description": "Learn the basics of CS",
    "credits": 3,
    "instructorName": "Dr. Ahmed",
    "maxStudents": 50,
    "students": [
        {
            "id": 1,
            "name": "Ali Khan",
            "email": "ali@example.com",
            "rollNumber": "STU001",
            "phoneNumber": "9876543210",
            "courses": [...]
        }
    ],
    "enrolledStudentCount": 1,
    "createdAt": "2024-03-23T10:40:00",
    "updatedAt": "2024-03-23T10:41:00"
}

Error: 404 Not Found (Student or Course not found)
{
    "timestamp": "2024-03-23T10:41:00",
    "status": 404,
    "error": "Resource Not Found",
    "message": "Student with ID 1 not found!"
}

Error: 409 Conflict (Student already enrolled)
{
    "timestamp": "2024-03-23T10:41:00",
    "status": 409,
    "error": "Duplicate Resource",
    "message": "Student with ID 1 is already enrolled in course 1"
}

Error: 400 Bad Request (Course at capacity)
{
    "timestamp": "2024-03-23T10:41:00",
    "status": 400,
    "error": "Course Capacity Full",
    "message": "Course 'CS101' is at full capacity!"
}
```

### 2. Unenroll Student from Course
```
DELETE /courses/1/students/1

Response: 200 OK
{
    "id": 1,
    "courseCode": "CS101",
    "courseName": "Introduction to Computer Science",
    "description": "Learn the basics of CS",
    "credits": 3,
    "instructorName": "Dr. Ahmed",
    "maxStudents": 50,
    "students": [],
    "enrolledStudentCount": 0,
    "createdAt": "2024-03-23T10:40:00",
    "updatedAt": "2024-03-23T10:42:00"
}

Error: 404 Not Found (Student not enrolled)
{
    "timestamp": "2024-03-23T10:42:00",
    "status": 404,
    "error": "Resource Not Found",
    "message": "Student with ID 1 is not enrolled in course 1"
}
```

### 3. Get All Students in a Course
```
GET /courses/1/students

Response: 200 OK
[
    {
        "id": 1,
        "name": "Ali Khan",
        "email": "ali@example.com",
        "rollNumber": "STU001",
        "phoneNumber": "9876543210",
        "courses": [...]
    },
    {
        "id": 2,
        "name": "Sara Ahmed",
        "email": "sara@example.com",
        "rollNumber": "STU002",
        "phoneNumber": "9876543211",
        "courses": [...]
    }
]

Error: 404 Not Found (Course doesn't exist)
```

### 4. Get Student Count in Course
```
GET /courses/1/students/count

Response: 200 OK
2

Error: 404 Not Found (Course doesn't exist)
```

---

## 🔢 HTTP Status Codes

| Code | Meaning | When Used |
|------|---------|-----------|
| 200 | OK | Successful GET, PUT, DELETE |
| 201 | Created | Successful POST (resource created) |
| 204 | No Content | Successful DELETE (no response body) |
| 400 | Bad Request | Course capacity full, invalid input |
| 404 | Not Found | Resource doesn't exist |
| 409 | Conflict | Duplicate email, course code, or enrollment |
| 500 | Server Error | Unexpected error |

---

## 🧪 cURL Examples

### Create Student
```bash
curl -X POST http://localhost:8080/api/students \
  -H "Content-Type: application/json" \
  -d '{"name":"Ali Khan","email":"ali@example.com","rollNumber":"STU001","phoneNumber":"9876543210"}'
```

### Create Course
```bash
curl -X POST http://localhost:8080/api/courses \
  -H "Content-Type: application/json" \
  -d '{"courseCode":"CS101","courseName":"Java Programming","description":"Learn Java","credits":3,"instructorName":"Dr. Ahmed","maxStudents":50}'
```

### Enroll Student
```bash
curl -X POST http://localhost:8080/api/courses/1/students/1 \
  -H "Content-Type: application/json"
```

### Get All Students in Course
```bash
curl -X GET http://localhost:8080/api/courses/1/students
```

### Update Student
```bash
curl -X PUT http://localhost:8080/api/students/1 \
  -H "Content-Type: application/json" \
  -d '{"name":"Ali Khan Updated","email":"newemail@example.com","rollNumber":"STU001","phoneNumber":"1234567890"}'
```

### Delete Student
```bash
curl -X DELETE http://localhost:8080/api/students/1
```

---

**Remember**: All endpoints require the application to be running at `http://localhost:8080`
