package com.example.studentcourse.exception;

/**
 * CourseCapacityException
 * Custom exception thrown when a course is full and cannot accept more students
 * 
 * This exception is used when trying to enroll a student in a course that has reached its maximum capacity
 */
public class CourseCapacityException extends RuntimeException {

    /**
     * Constructor with message only
     * 
     * @param message The error message describing the capacity issue
     */
    public CourseCapacityException(String message) {
        super(message);
    }

    /**
     * Constructor with message and cause
     * 
     * @param message The error message
     * @param cause The original exception that caused this
     */
    public CourseCapacityException(String message, Throwable cause) {
        super(message, cause);
    }
}
