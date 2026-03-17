# API Quick Reference

## Base URL
```
http://localhost:8080/api
```

## Doctor Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/doctors` | Create a new doctor |
| GET | `/doctors` | Get all doctors |
| GET | `/doctors/{id}` | Get doctor by ID |
| GET | `/doctors/email/{email}` | Get doctor by email |
| GET | `/doctors/license/{licenseNumber}` | Get doctor by license number |
| GET | `/doctors/specialization/{specialization}` | Get doctors by specialization |
| GET | `/doctors/available` | Get available doctors |
| GET | `/doctors/exists/{id}` | Check if doctor exists |
| GET | `/doctors/count` | Get total doctors count |
| PUT | `/doctors/{id}` | Update doctor information |
| PATCH | `/doctors/{id}/availability?isAvailable=boolean` | Update doctor availability |
| DELETE | `/doctors/{id}` | Delete doctor by ID |
| DELETE | `/doctors` | Delete all doctors |

## Patient Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/patients` | Create a new patient |
| GET | `/patients` | Get all patients |
| GET | `/patients/{id}` | Get patient by ID |
| GET | `/patients/email/{email}` | Get patient by email |
| GET | `/patients/insurance/{insuranceNumber}` | Get patient by insurance number |
| GET | `/patients/active` | Get active patients |
| GET | `/patients/gender/{gender}` | Get patients by gender |
| GET | `/patients/exists/{id}` | Check if patient exists |
| GET | `/patients/count` | Get total patients count |
| PUT | `/patients/{id}` | Update patient information |
| PATCH | `/patients/{id}/status?isActive=boolean` | Update patient status |
| PATCH | `/patients/{id}/medical-history` | Update patient medical history |
| DELETE | `/patients/{id}` | Delete patient by ID |
| DELETE | `/patients` | Delete all patients |

## Example cURL Requests

### Create a Doctor
```bash
curl -X POST http://localhost:8080/api/doctors \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "John",
    "lastName": "Doe",
    "email": "john.doe@example.com",
    "phone": "9876543210",
    "specialization": "Cardiology",
    "licenseNumber": "LIC001",
    "experienceYears": 10,
    "isAvailable": true
  }'
```

### Get All Doctors
```bash
curl -X GET http://localhost:8080/api/doctors
```

### Get Doctor by ID
```bash
curl -X GET http://localhost:8080/api/doctors/1
```

### Update Doctor
```bash
curl -X PUT http://localhost:8080/api/doctors/1 \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "Jane",
    "lastName": "Smith",
    "email": "jane.smith@example.com",
    "phone": "9876543211",
    "specialization": "Neurology",
    "licenseNumber": "LIC002",
    "experienceYears": 12,
    "isAvailable": true
  }'
```

### Delete Doctor
```bash
curl -X DELETE http://localhost:8080/api/doctors/1
```

### Create a Patient
```bash
curl -X POST http://localhost:8080/api/patients \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "Alice",
    "lastName": "Johnson",
    "email": "alice@example.com",
    "phone": "9876543212",
    "dateOfBirth": "1990-05-15",
    "gender": "Female",
    "address": "123 Main St",
    "medicalHistory": "Diabetes",
    "insuranceNumber": "INS001",
    "isActive": true
  }'
```

### Get All Patients
```bash
curl -X GET http://localhost:8080/api/patients
```

### Get Patient by ID
```bash
curl -X GET http://localhost:8080/api/patients/1
```

### Update Patient
```bash
curl -X PUT http://localhost:8080/api/patients/1 \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "Bob",
    "lastName": "King",
    "email": "bob@example.com",
    "phone": "9876543213",
    "dateOfBirth": "1985-03-20",
    "gender": "Male",
    "address": "456 Oak Ave",
    "medicalHistory": "Hypertension",
    "insuranceNumber": "INS002",
    "isActive": true
  }'
```

### Delete Patient
```bash
curl -X DELETE http://localhost:8080/api/patients/1
```

## HTTP Status Codes

- `200 OK` - Successful GET or PUT request
- `201 Created` - Successful POST request
- `204 No Content` - Successful DELETE request
- `400 Bad Request` - Invalid request
- `404 Not Found` - Resource not found
- `500 Internal Server Error` - Server error

## Response Format

All responses are in JSON format.

### Success Response
```json
{
  "id": 1,
  "firstName": "John",
  "lastName": "Doe",
  "email": "john.doe@example.com",
  "phone": "9876543210",
  "specialization": "Cardiology",
  "licenseNumber": "LIC001",
  "experienceYears": 10,
  "isAvailable": true
}
```

### Error Response
```json
{
  "timestamp": "2026-03-14T10:30:00.000+00:00",
  "status": 404,
  "error": "Not Found",
  "message": "Doctor not found with id: 999",
  "path": "/api/doctors/999"
}
```
