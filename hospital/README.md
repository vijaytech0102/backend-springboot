# Hospital Management System

A Spring Boot application for managing hospital operations including doctor and patient information with complete CRUD operations.

## Project Structure

```
hospital/
├── src/
│   ├── main/
│   │   ├── java/com/example/hospital/
│   │   │   ├── HospitalApplication.java          (Main Spring Boot application)
│   │   │   ├── entity/
│   │   │   │   ├── Doctor.java                   (Doctor entity)
│   │   │   │   └── Patient.java                  (Patient entity)
│   │   │   ├── dto/
│   │   │   │   ├── DoctorDTO.java                (Doctor data transfer object)
│   │   │   │   └── PatientDTO.java               (Patient data transfer object)
│   │   │   ├── repository/
│   │   │   │   ├── DoctorRepository.java         (Doctor JPA repository)
│   │   │   │   └── PatientRepository.java        (Patient JPA repository)
│   │   │   ├── service/
│   │   │   │   ├── DoctorService.java            (Doctor business logic)
│   │   │   │   └── PatientService.java           (Patient business logic)
│   │   │   ├── controller/
│   │   │   │   ├── DoctorController.java         (Doctor REST endpoints)
│   │   │   │   └── PatientController.java        (Patient REST endpoints)
│   │   │   └── config/
│   │   │       └── ModelMapperConfig.java        (ModelMapper configuration)
│   │   └── resources/
│   │       └── application.properties            (Application configuration)
│   └── test/java/com/example/hospital/           (Test files)
└── pom.xml                                        (Maven configuration)
```

## Technologies Used

- **Spring Boot 3.2.0** - Framework
- **Spring Data JPA** - Database access
- **H2 Database** - In-memory database
- **ModelMapper 3.1.1** - DTO mapping
- **Lombok** - Boilerplate reduction
- **Maven** - Build tool
- **Java 17** - Programming language

## Database Configuration

The application uses H2 in-memory database with the following settings:
- **URL**: `jdbc:h2:mem:hospitaldb`
- **Console**: Available at `http://localhost:8080/h2-console`
- **DDL**: `create-drop` (creates schema on startup, drops on shutdown)

## Running the Application

### Build the project:
```bash
mvn clean install
```

### Run the application:
```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

## API Endpoints

### Doctor Management

#### Create Doctor
```
POST /api/doctors
Content-Type: application/json

{
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

#### Get All Doctors
```
GET /api/doctors
```

#### Get Doctor by ID
```
GET /api/doctors/{id}
```

#### Get Doctor by Email
```
GET /api/doctors/email/{email}
```

#### Get Doctor by License Number
```
GET /api/doctors/license/{licenseNumber}
```

#### Get Doctors by Specialization
```
GET /api/doctors/specialization/{specialization}
```
Example: `/api/doctors/specialization/Cardiology`

#### Get Available Doctors
```
GET /api/doctors/available
```

#### Update Doctor
```
PUT /api/doctors/{id}
Content-Type: application/json

{
  "firstName": "Jane",
  "lastName": "Smith",
  "email": "jane.smith@example.com",
  "phone": "9876543211",
  "specialization": "Neurology",
  "licenseNumber": "LIC002",
  "experienceYears": 12,
  "isAvailable": true
}
```

#### Update Doctor Availability
```
PATCH /api/doctors/{id}/availability?isAvailable=false
```

#### Delete Doctor
```
DELETE /api/doctors/{id}
```

#### Delete All Doctors
```
DELETE /api/doctors
```

#### Check if Doctor Exists
```
GET /api/doctors/exists/{id}
```

#### Get Total Doctors Count
```
GET /api/doctors/count
```

---

### Patient Management

#### Create Patient
```
POST /api/patients
Content-Type: application/json

{
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
}
```

#### Get All Patients
```
GET /api/patients
```

#### Get Patient by ID
```
GET /api/patients/{id}
```

#### Get Patient by Email
```
GET /api/patients/email/{email}
```

#### Get Patient by Insurance Number
```
GET /api/patients/insurance/{insuranceNumber}
```

#### Get Active Patients
```
GET /api/patients/active
```

#### Get Patients by Gender
```
GET /api/patients/gender/{gender}
```
Example: `/api/patients/gender/Female`

#### Update Patient
```
PUT /api/patients/{id}
Content-Type: application/json

{
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
}
```

#### Update Patient Status
```
PATCH /api/patients/{id}/status?isActive=false
```

#### Update Patient Medical History
```
PATCH /api/patients/{id}/medical-history
Content-Type: application/json

"Asthma, Allergy to Penicillin"
```

#### Delete Patient
```
DELETE /api/patients/{id}
```

#### Delete All Patients
```
DELETE /api/patients
```

#### Check if Patient Exists
```
GET /api/patients/exists/{id}
```

#### Get Total Patients Count
```
GET /api/patients/count
```

## CRUD Operations

### Doctor Service Methods

- **Create**: `createDoctor(DoctorDTO)` - Add a new doctor
- **Read**: 
  - `getAllDoctors()` - Get all doctors
  - `getDoctorById(Long)` - Get doctor by ID
  - `getDoctorByEmail(String)` - Get doctor by email
  - `getDoctorByLicenseNumber(String)` - Get doctor by license number
  - `getDoctorsBySpecialization(String)` - Get doctors by specialization
  - `getAvailableDoctors()` - Get available doctors
- **Update**:
  - `updateDoctor(Long, DoctorDTO)` - Update doctor information
  - `updateDoctorAvailability(Long, Boolean)` - Update availability status
- **Delete**:
  - `deleteDoctor(Long)` - Delete doctor by ID
  - `deleteAllDoctors()` - Delete all doctors

### Patient Service Methods

- **Create**: `createPatient(PatientDTO)` - Add a new patient
- **Read**:
  - `getAllPatients()` - Get all patients
  - `getPatientById(Long)` - Get patient by ID
  - `getPatientByEmail(String)` - Get patient by email
  - `getPatientByInsuranceNumber(String)` - Get patient by insurance number
  - `getActivePatients()` - Get active patients
  - `getPatientsByGender(String)` - Get patients by gender
- **Update**:
  - `updatePatient(Long, PatientDTO)` - Update patient information
  - `updatePatientStatus(Long, Boolean)` - Update active status
  - `updatePatientMedicalHistory(Long, String)` - Update medical history
- **Delete**:
  - `deletePatient(Long)` - Delete patient by ID
  - `deleteAllPatients()` - Delete all patients

## Features

✅ Complete CRUD operations for both Doctor and Patient  
✅ Unidirectional entity relationships  
✅ Advanced query methods in repositories  
✅ Automatic DTO mapping using ModelMapper  
✅ RESTful API endpoints with proper HTTP status codes  
✅ Exception handling for resource not found scenarios  
✅ H2 database for easy development and testing  
✅ Lombok for reduced boilerplate code  
✅ CORS enabled for cross-origin requests  

## Testing

The application can be tested using:
- **Postman** - For manual API testing
- **cURL** - For command-line testing
- **H2 Console** - For direct database access at `http://localhost:8080/h2-console`

## Future Enhancements

- Add appointment management
- Add prescription management
- Add user authentication and authorization
- Add validation annotations
- Add exception handling with custom exceptions
- Add logging
- Add database migrations using Flyway/Liquibase
- Add unit tests
- Add integration tests

## Notes

- The application uses H2 in-memory database, so data is lost when the application restarts
- To use a persistent database, modify `application.properties` to use PostgreSQL or MySQL
- The application uses ModelMapper for automatic DTO conversion with null values skipped
- All timestamps are automatically managed by JPA

## Support

For issues or questions, please contact the development team or create an issue in the repository.
