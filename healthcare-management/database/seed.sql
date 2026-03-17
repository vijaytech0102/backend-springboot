-- Healthcare Management System Seed Data
-- Default credentials: passwords are hashed with BCrypt

USE healthcare_db;

-- Seed Admin User (admin@healthcare.com / Admin@123)
-- BCrypt hash of Admin@123: $2a$10$5ZI6DIDNXr6d8.oL5/sS0OYu2m8f3VzNrfp1VQz0T8Y9qKz2pGEFi
INSERT INTO users (full_name, email, password, role, phone, gender, address, is_active) 
VALUES ('Admin User', 'admin@healthcare.com', '$2a$10$5ZI6DIDNXr6d8.oL5/sS0OYu2m8f3VzNrfp1VQz0T8Y9qKz2pGEFi', 'ADMIN', '+91-9000000000', 'MALE', 'Healthcare Admin Office', TRUE);

-- Seed Doctors (Doctor@123 / BCrypt: $2a$10$Y0wBHVVXfOXe4mCCJeKLh.JQxJaC23sG3NfqHP1H7s0xnqCw1RUCi)

-- Doctor 1: Cardiology
INSERT INTO users (full_name, email, password, role, phone, date_of_birth, gender, address, is_active) 
VALUES ('Dr. Rajesh Kumar', 'rajesh.kumar@hospital.com', '$2a$10$Y0wBHVVXfOXe4mCCJeKLh.JQxJaC23sG3NfqHP1H7s0xnqCw1RUCi', 'DOCTOR', '+91-9100000001', '1980-05-15', 'MALE', 'Cardiology Ward, City Hospital', TRUE);

INSERT INTO doctors (user_id, specialisation, qualification, experience_years, consultation_fee, bio, availability_days, available_from, available_to, is_available) 
VALUES (2, 'Cardiology', 'MBBS, MD (Cardiology)', 15, 500.00, 'Expert cardiologist with 15 years of experience in treating heart diseases and preventive cardiology.', 'MON,TUE,WED,THU,FRI', '09:00:00', '17:00:00', TRUE);

-- Doctor 2: Dermatology
INSERT INTO users (full_name, email, password, role, phone, date_of_birth, gender, address, is_active) 
VALUES ('Dr. Priya Sharma', 'priya.sharma@hospital.com', '$2a$10$Y0wBHVVXfOXe4mCCJeKLh.JQxJaC23sG3NfqHP1H7s0xnqCw1RUCi', 'DOCTOR', '+91-9100000002', '1985-03-22', 'FEMALE', 'Dermatology Clinic, Medical Center', TRUE);

INSERT INTO doctors (user_id, specialisation, qualification, experience_years, consultation_fee, bio, availability_days, available_from, available_to, is_available) 
VALUES (3, 'Dermatology', 'MBBS, MD (Dermatology)', 10, 400.00, 'Specialized in treating skin conditions and cosmetic dermatology. Experienced in laser therapy.', 'TUE,WED,THU,FRI,SAT', '10:00:00', '18:00:00', TRUE);

-- Doctor 3: Orthopaedics
INSERT INTO users (full_name, email, password, role, phone, date_of_birth, gender, address, is_active) 
VALUES ('Dr. Amit Singh', 'amit.singh@hospital.com', '$2a$10$Y0wBHVVXfOXe4mCCJeKLh.JQxJaC23sG3NfqHP1H7s0xnqCw1RUCi', 'DOCTOR', '+91-9100000003', '1982-07-10', 'MALE', 'Orthopaedic Surgery Department, Hospital', TRUE);

INSERT INTO doctors (user_id, specialisation, qualification, experience_years, consultation_fee, bio, availability_days, available_from, available_to, is_available) 
VALUES (4, 'Orthopaedics', 'MBBS, MS (Orthopaedics)', 12, 450.00, 'Specialist in orthopedic surgery and sports medicine. Experienced in joint replacements.', 'MON,WED,THU,FRI', '08:00:00', '16:00:00', TRUE);

-- Doctor 4: Neurology
INSERT INTO users (full_name, email, password, role, phone, date_of_birth, gender, address, is_active) 
VALUES ('Dr. Neha Patel', 'neha.patel@hospital.com', '$2a$10$Y0wBHVVXfOXe4mCCJeKLh.JQxJaC23sG3NfqHP1H7s0xnqCw1RUCi', 'DOCTOR', '+91-9100000004', '1983-09-18', 'FEMALE', 'Neurology Center, Medical Institute', TRUE);

INSERT INTO doctors (user_id, specialisation, qualification, experience_years, consultation_fee, bio, availability_days, available_from, available_to, is_available) 
VALUES (5, 'Neurology', 'MBBS, MD (Neurology)', 11, 480.00, 'Expert in neurological disorders, migraine management, and stroke care. Certified neurologist.', 'MON,TUE,THU,FRI,SAT', '09:30:00', '17:30:00', TRUE);

-- Doctor 5: General Medicine
INSERT INTO users (full_name, email, password, role, phone, date_of_birth, gender, address, is_active) 
VALUES ('Dr. Vikram Desai', 'vikram.desai@hospital.com', '$2a$10$Y0wBHVVXfOXe4mCCJeKLh.JQxJaC23sG3NfqHP1H7s0xnqCw1RUCi', 'DOCTOR', '+91-9100000005', '1980-11-25', 'MALE', 'General Medicine Ward, City Hospital', TRUE);

INSERT INTO doctors (user_id, specialisation, qualification, experience_years, consultation_fee, bio, availability_days, available_from, available_to, is_available) 
VALUES (6, 'General Medicine', 'MBBS, MD (Internal Medicine)', 14, 350.00, 'Senior physician with expertise in managing chronic diseases and general health conditions.', 'MON,TUE,WED,THU,FRI', '08:30:00', '18:00:00', TRUE);

-- Seed Patients (Patient@123 / BCrypt: $2a$10$nOQZVVXXfOXe4mCCJeKLh.JQxJaC23sG3NfqHP1H7s0xnqCw1RUCi)

INSERT INTO users (full_name, email, password, role, phone, date_of_birth, gender, address, is_active) 
VALUES ('John Doe', 'patient1@test.com', '$2a$10$nOQZVVXXfOXe4mCCJeKLh.JQxJaC23sG3NfqHP1H7s0xnqCw1RUCi', 'PATIENT', '+91-9200000001', '1995-01-10', 'MALE', '123 Main Street, City', TRUE);

INSERT INTO users (full_name, email, password, role, phone, date_of_birth, gender, address, is_active) 
VALUES ('Jane Smith', 'patient2@test.com', '$2a$10$nOQZVVXXfOXe4mCCJeKLh.JQxJaC23sG3NfqHP1H7s0xnqCw1RUCi', 'PATIENT', '+91-9200000002', '1998-05-20', 'FEMALE', '456 Oak Avenue, Town', TRUE);

INSERT INTO users (full_name, email, password, role, phone, date_of_birth, gender, address, is_active) 
VALUES ('Robert Wilson', 'patient3@test.com', '$2a$10$nOQZVVXXfOXe4mCCJeKLh.JQxJaC23sG3NfqHP1H7s0xnqCw1RUCi', 'PATIENT', '+91-9200000003', '1992-08-15', 'MALE', '789 Pine Road, Village', TRUE);

-- Seed Appointments

-- Appointment 1: COMPLETED
INSERT INTO appointments (patient_id, doctor_id, appointment_date, appointment_time, status, reason, notes) 
VALUES (7, 2, DATE_SUB(CURDATE(), INTERVAL 5 DAY), '10:00:00', 'COMPLETED', 'Regular checkup', 'Patient had normal checkup, all vitals are good');

-- Appointment 2: COMPLETED
INSERT INTO appointments (patient_id, doctor_id, appointment_date, appointment_time, status, reason, notes) 
VALUES (8, 3, DATE_SUB(CURDATE(), INTERVAL 3 DAY), '14:30:00', 'COMPLETED', 'Skin consultation', 'Acne treatment consultation completed');

-- Appointment 3: CONFIRMED
INSERT INTO appointments (patient_id, doctor_id, appointment_date, appointment_time, status, reason, notes) 
VALUES (9, 4, DATE_ADD(CURDATE(), INTERVAL 2 DAY), '11:00:00', 'CONFIRMED', 'Knee pain', 'Patient reports knee pain for 2 weeks');

-- Appointment 4: PENDING
INSERT INTO appointments (patient_id, doctor_id, appointment_date, appointment_time, status, reason, notes) 
VALUES (7, 5, DATE_ADD(CURDATE(), INTERVAL 5 DAY), '15:00:00', 'PENDING', 'Headache treatment', 'Severe migraine episodes');

-- Appointment 5: COMPLETED
INSERT INTO appointments (patient_id, doctor_id, appointment_date, appointment_time, status, reason, notes) 
VALUES (8, 6, DATE_SUB(CURDATE(), INTERVAL 7 DAY), '09:30:00', 'COMPLETED', 'Blood pressure check', 'Annual physical examination');

-- Appointment 6: CANCELLED
INSERT INTO appointments (patient_id, doctor_id, appointment_date, appointment_time, status, reason, notes) 
VALUES (9, 2, DATE_SUB(CURDATE(), INTERVAL 2 DAY), '16:00:00', 'CANCELLED', 'Urgent visit', 'Patient cancelled due to schedule conflict');

-- Appointment 7: CONFIRMED
INSERT INTO appointments (patient_id, doctor_id, appointment_date, appointment_time, status, reason, notes) 
VALUES (7, 6, DATE_ADD(CURDATE(), INTERVAL 1 DAY), '10:30:00', 'CONFIRMED', 'Flu symptoms', 'Patient has cold and fever');

-- Appointment 8: PENDING
INSERT INTO appointments (patient_id, doctor_id, appointment_date, appointment_time, status, reason, notes) 
VALUES (8, 4, DATE_ADD(CURDATE(), INTERVAL 7 DAY), '13:00:00', 'PENDING', 'Back pain', 'Lower back pain investigation');

-- Appointment 9: COMPLETED
INSERT INTO appointments (patient_id, doctor_id, appointment_date, appointment_time, status, reason, notes) 
VALUES (9, 3, DATE_SUB(CURDATE(), INTERVAL 10 DAY), '11:00:00', 'COMPLETED', 'Dermatitis', 'Skin allergy treatment');

-- Appointment 10: PENDING
INSERT INTO appointments (patient_id, doctor_id, appointment_date, appointment_time, status, reason, notes) 
VALUES (7, 2, DATE_ADD(CURDATE(), INTERVAL 10 DAY), '14:00:00', 'PENDING', 'Cardiac checkup', 'Annual cardiac screening');

-- Seed Medical Records (for COMPLETED appointments)

INSERT INTO medical_records (appointment_id, diagnosis, prescription, lab_tests, follow_up_date, doctor_notes) 
VALUES (1, 'Normal health, no abnormalities', 'Multivitamin daily, Paracetamol as needed', 'Blood test normal, BP normal', DATE_ADD(CURDATE(), INTERVAL 6 MONTH), 'Patient is healthy, maintain regular exercise');

INSERT INTO medical_records (appointment_id, diagnosis, prescription, lab_tests, follow_up_date, doctor_notes) 
VALUES (2, 'Acne vulgaris, moderate severity', 'Benzoyl peroxide 2.5%, Azithromycin 250mg twice daily', 'Skin culture done', DATE_ADD(CURDATE(), INTERVAL 3 MONTH), 'Use prescribed cream twice daily, avoid oily foods');

INSERT INTO medical_records (appointment_id, diagnosis, prescription, lab_tests, follow_up_date, doctor_notes) 
VALUES (5, 'Hypertension stage 1, overall good health', 'Amlodipine 5mg daily, Lisinopril 10mg daily', 'ECG normal, Lipid profile normal', DATE_ADD(CURDATE(), INTERVAL 1 MONTH), 'Continue medications, reduce salt intake, regular exercise');

INSERT INTO medical_records (appointment_id, diagnosis, prescription, lab_tests, follow_up_date, doctor_notes) 
VALUES (9, 'Contact dermatitis due to allergic reaction', 'Hydrocortisone cream 1%, Cetrizine 10mg at night', 'Patch test positive', DATE_ADD(CURDATE(), INTERVAL 2 WEEK), 'Avoid allergen, keep area clean and dry');

-- Seed Bills (for COMPLETED appointments)

INSERT INTO bills (appointment_id, consultation_fee, additional_charges, discount, total_amount, payment_status) 
VALUES (1, 500.00, 0, 0, 500.00, 'PAID');

INSERT INTO bills (appointment_id, consultation_fee, additional_charges, discount, total_amount, payment_status) 
VALUES (2, 400.00, 50.00, 0, 450.00, 'PAID');

INSERT INTO bills (appointment_id, consultation_fee, additional_charges, discount, total_amount, payment_status) 
VALUES (5, 350.00, 0, 50.00, 300.00, 'UNPAID');

INSERT INTO bills (appointment_id, consultation_fee, additional_charges, discount, total_amount, payment_status) 
VALUES (9, 400.00, 100.00, 0, 500.00, 'PARTIALLY_PAID');

-- Seed Feedback (for COMPLETED appointments)

INSERT INTO feedback (patient_id, doctor_id, appointment_id, rating, comment) 
VALUES (7, 2, 1, 5, 'Excellent doctor! Very professional and caring. Will definitely visit again.');

INSERT INTO feedback (patient_id, doctor_id, appointment_id, rating, comment) 
VALUES (8, 3, 2, 4, 'Good consultation and treatment. Very knowledgeable about skin conditions.');

INSERT INTO feedback (patient_id, doctor_id, appointment_id, rating, comment) 
VALUES (8, 6, 5, 5, 'Dr. Vikram is amazing! Very patient and thorough in his examination.');

INSERT INTO feedback (patient_id, doctor_id, appointment_id, rating, comment) 
VALUES (9, 3, 9, 3, 'Adequate service, could have explained more about the condition.');

INSERT INTO feedback (patient_id, doctor_id, appointment_id, rating, comment) 
VALUES (7, 2, 10, 5, 'Perfect! Dr. Kumar is the best cardiologist I know.');

COMMIT;
