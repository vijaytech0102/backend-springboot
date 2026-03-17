# Healthcare Management System - Database Setup

## Prerequisites
- MySQL 8.0 or higher
- MySQL command-line client or MySQL Workbench

## Setup Instructions

### 1. Create Database
Run the `schema.sql` file to create the database and all tables:

```bash
mysql -u root -p < schema.sql
```

When prompted, enter your MySQL root password.

### 2. Seed Sample Data
Populate the database with sample data for testing:

```bash
mysql -u root -p healthcare_db < seed.sql
```

### 3. Verify Installation
Connect to the database and verify the tables:

```bash
mysql -u root -p healthcare_db
```

Then run:
```sql
SHOW TABLES;
SELECT COUNT(*) FROM users;
SELECT COUNT(*) FROM doctors;
SELECT COUNT(*) FROM appointments;
```

## Database Schema

The system includes the following tables:

- **users**: Contains all user records (Patients, Doctors, Admins)
- **doctors**: Extended information for doctor users
- **appointments**: Appointment records between patients and doctors
- **medical_records**: Medical records linked to appointments
- **bills**: Billing information for appointments
- **feedback**: Patient feedback and ratings for doctors

## Sample Data

After running seed.sql, you'll have:
- 1 Admin account
- 5 Doctors with different specializations
- 3 Patient accounts
- 10 Appointments in various statuses
- 5 Medical records
- 5 Bills
- 5 Feedback entries

### Default Credentials
- **Admin**: admin@healthcare.com / Admin@123
- **Doctor**: rajesh.kumar@hospital.com / Doctor@123
- **Patient**: patient1@test.com / Patient@123

## Notes
- All passwords are hashed using BCrypt
- Timestamps are automatically managed by MySQL triggers
- Foreign key constraints are enforced
- Unique constraints prevent duplicate entries where applicable
