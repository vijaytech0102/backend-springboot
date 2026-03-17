# Healthcare Management System

A complete, production-ready online healthcare management system built with Angular 17, Spring Boot 3, and MySQL.

## Features

### Patient Features
- **Find & Book Doctors**: Search doctors by specialization, view profiles and ratings, and book appointments
- **Manage Appointments**: View, confirm, or cancel appointments
- **Medical Records**: Access past medical records and prescriptions
- **Billing & Payments**: View bills and make online payments
- **Profile Management**: Update personal information

### Doctor Features
- **Appointment Management**: View and manage all appointments
- **Patient Records**: Add and update medical records
- **Schedule Management**: Set availability and working hours
- **Profile Management**: Update specialization and availability

### Admin Features
- **User Management**: View, activate/deactivate, and manage users
- **Doctor Management**: Manage doctor profiles and specializations
- **Appointment Tracking**: Monitor all system appointments
- **Reports & Analytics**: View system statistics and revenue reports

## Tech Stack

### Backend
- **Language**: Java 17 LTS
- **Framework**: Spring Boot 3.2.0
- **Database**: MySQL 8.x
- **Security**: JWT (JJWT 0.11.5), BCrypt
- **API**: RESTful with Spring Data JPA
- **Documentation**: Swagger/OpenAPI 3.0

### Frontend
- **Framework**: Angular 17
- **Language**: TypeScript 5.2
- **UI Library**: Angular Material 17
- **Bootstrap**: Bootstrap 5
- **State Management**: RxJS
- **Notifications**: ngx-toastr
- **JWT Handling**: @auth0/angular-jwt

### Database
- MySQL 8.x
- Proper indexing and constraints
- Foreign key relationships
- Sample seed data with doctors and patients

## Project Structure

```
healthcare-management/
├── backend/                    # Spring Boot application
│   ├── src/main/java/
│   │   └── com/healthcare/
│   │       ├── config/        # Security, CORS, Swagger config
│   │       ├── controller/    # REST endpoints
│   │       ├── service/       # Business logic
│   │       ├── repository/    # Database access
│   │       ├── entity/        # JPA entities
│   │       ├── dto/           # Data transfer objects
│   │       ├── security/      # JWT, authentication
│   │       ├── exception/     # Custom exceptions
│   │       └── utils/         # Utility classes
│   ├── pom.xml
│   └── application.properties
├── frontend/                   # Angular application
│   ├── src/
│   │   ├── app/
│   │   │   ├── auth/          # Login, register pages
│   │   │   ├── patient/       # Patient module
│   │   │   ├── doctor/        # Doctor module
│   │   │   ├── admin/         # Admin module
│   │   │   ├── shared/        # Shared components
│   │   │   ├── core/          # Services, guards, interceptors
│   │   │   ├── app.module.ts
│   │   │   └── app-routing.module.ts
│   │   ├── environments/      # Environment config
│   │   ├── styles.scss
│   │   └── main.ts
│   ├── package.json
│   ├── angular.json
│   └── tsconfig.json
└── database/                   # Database scripts
    ├── schema.sql
    └── seed.sql
```

## Getting Started

### Prerequisites
- Java 17 LTS
- Node.js 18+
- MySQL 8.0+
- Maven 3.8+
- Angular CLI 17

### Setup Instructions

#### Database Setup
1. Create MySQL database:
   ```sql
   CREATE DATABASE healthcare_db;
   ```

2. Execute schema and seed:
   ```bash
   mysql -u root -p healthcare_db < database/schema.sql
   mysql -u root -p healthcare_db < database/seed.sql
   ```

#### Backend Setup
1. Navigate to backend:
   ```bash
   cd backend
   ```

2. Update `application.properties` with your DB credentials:
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/healthcare_db
   spring.datasource.username=root
   spring.datasource.password=your_password
   ```

3. Build and run:
   ```bash
   mvn clean install
   mvn spring-boot:run
   ```

Backend starts at: `http://localhost:8080`
Swagger UI: `http://localhost:8080/swagger-ui.html`

#### Frontend Setup
1. Navigate to frontend:
   ```bash
   cd frontend
   ```

2. Install dependencies:
   ```bash
   npm install
   ```

3. Start development server:
   ```bash
   ng serve
   ```
   or
   ```bash
   npm start
   ```

Frontend runs at: `http://localhost:4200`

## Default Credentials

### Patient
- **Email**: patient@example.com
- **Password**: Patient@123

### Doctor
- **Email**: doctor1@example.com
- **Password**: Doctor@123

### Admin
- **Email**: admin@example.com
- **Password**: Admin@123

## API Documentation

Swagger UI provides complete API documentation. Access it at:
```
http://localhost:8080/swagger-ui.html
```

## Key Endpoints

### Authentication
- `POST /api/auth/register` - Register new user
- `POST /api/auth/login` - Login user

### Patient
- `GET /api/patients/profile` - Get patient profile
- `PUT /api/patients/profile` - Update patient profile
- `GET /api/doctors` - List all doctors
- `GET /api/doctors?specialisation=Cardiology` - Filter by specialization

### Appointments
- `POST /api/appointments` - Book appointment
- `GET /api/appointments/{id}` - Get appointment details
- `PUT /api/appointments/{id}/confirm` - Confirm appointment
- `PUT /api/appointments/{id}/cancel` - Cancel appointment
- `PUT /api/appointments/{id}/complete` - Complete appointment

### Medical Records
- `POST /api/medical-records` - Add medical record
- `GET /api/medical-records/patient` - Get patient records
- `GET /api/medical-records/appointment/{appointmentId}` - Get record by appointment

### Billing
- `GET /api/billing/patient` - Get patient bills
- `PUT /api/billing/{billId}/pay` - Mark bill as paid
- `GET /api/billing/appointment/{appointmentId}` - Get bill by appointment

### Admin
- `GET /api/admin/users` - List all users (Admin only)
- `PUT /api/admin/users/{userId}/toggle` - Toggle user status
- `DELETE /api/admin/users/{userId}` - Delete user
- `GET /api/admin/stats` - Get system statistics

## Security Features

- **JWT Authentication**: Token-based stateless authentication
- **Password Encryption**: BCrypt password encoding
- **Role-Based Access Control**: PATIENT, DOCTOR, ADMIN roles
- **CORS Configuration**: Configured for frontend URL
- **Method-level Security**: @PreAuthorize annotations on endpoints
- **Security Headers**: Proper HTTP security headers

## Features Implemented

✅ Complete user authentication and authorization
✅ Doctor search with filtering by specialization
✅ Appointment booking with double-booking prevention
✅ Auto-bill generation on appointment completion
✅ Medical records management
✅ Payment processing
✅ Feedback and rating system
✅ Doctor availability management
✅ Admin dashboard with statistics
✅ Responsive UI with Bootstrap 5
✅ Error handling and validation
✅ Loading indicators
✅ Toast notifications
✅ Form validation

## Building for Production

### Backend
```bash
cd backend
mvn clean package -DskipTests
```

### Frontend
```bash
cd frontend
ng build --configuration production
```

## Troubleshooting

### Port Already in Use
- Backend: Change `server.port` in `application.properties`
- Frontend: Use `ng serve --port 4300`

### Database Connection Issues
- Verify MySQL is running
- Check credentials in `application.properties`
- Ensure database is created

### CORS Errors
- Update `application.properties` allowed origins
- Verify frontend URL matches configuration

### Build Errors
- Clear Maven cache: `mvn clean`
- Clear npm cache: `npm cache clean --force`
- Delete node_modules: `rm -rf node_modules && npm install`

## Contributing

This is a complete educational project. Feel free to extend with additional features like:
- Email notifications
- SMS reminders
- Telemedicine support
- Insurance integration
- Advanced analytics

## License

This project is open source and available under the MIT License.

## Support

For issues and questions, refer to the documentation or check the API documentation at `/swagger-ui.html`.
