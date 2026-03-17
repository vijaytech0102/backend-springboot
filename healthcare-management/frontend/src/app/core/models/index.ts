export interface User {
  userId?: number;
  fullName: string;
  email: string;
  role: string;
  phone?: string;
  dateOfBirth?: string;
  gender?: string;
  address?: string;
  profilePicture?: string;
  isActive?: boolean;
  createdAt?: string;
  updatedAt?: string;
}

export interface Doctor {
  doctorId?: number;
  user?: User;
  specialisation: string;
  qualification: string;
  experienceYears?: number;
  consultationFee: number;
  bio?: string;
  availabilityDays?: string;
  availableFrom?: string;
  availableTo?: string;
  isAvailable?: boolean;
  averageRating?: number;
}

export interface Appointment {
  appointmentId?: number;
  patient?: User;
  doctor?: Doctor;
  appointmentDate: string;
  appointmentTime: string;
  status?: string;
  reason: string;
  notes?: string;
  createdAt?: string;
}

export interface MedicalRecord {
  recordId?: number;
  appointment?: Appointment;
  diagnosis: string;
  prescription?: string;
  labTests?: string;
  followUpDate?: string;
  doctorNotes?: string;
  createdAt?: string;
}

export interface Bill {
  billId?: number;
  appointmentId?: number;
  appointment?: Appointment;
  consultationFee: number;
  additionalCharges?: number;
  discount?: number;
  totalAmount: number;
  paymentStatus: string;
  paymentDate?: string;
  generatedAt?: string;
}

export interface Feedback {
  feedbackId?: number;
  patient?: User;
  doctor?: Doctor;
  appointment?: Appointment;
  rating: number;
  comment?: string;
  createdAt?: string;
}

export interface AuthResponse {
  access_token: string;
  token_type: string;
  user: User;
  message: string;
}

export interface ApiResponse<T> {
  success: boolean;
  message: string;
  data: T;
  error?: string;
}
