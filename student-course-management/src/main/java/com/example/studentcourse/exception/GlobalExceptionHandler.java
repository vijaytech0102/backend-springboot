package com.example.studentcourse.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * GlobalExceptionHandler
 * 
 * This is a centralized exception handler for the entire application
 * Using @RestControllerAdvice automatically makes this handler apply to all REST controllers
 * 
 * Benefits:
 * - Consistent error response format across the application
 * - Prevents stack traces from being exposed to clients
 * - Allows custom error messages and HTTP status codes
 * - Reduces code duplication in controllers
 * 
 * How it works:
 * When any exception is thrown in a controller method:
 * 1. Spring catches the exception
 * 2. Looks for a matching @ExceptionHandler method
 * 3. Calls the handler method
 * 4. Returns the ResponseEntity created by the handler
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handler for ResourceNotFoundException
     * Called when a student, course, or other resource is not found
     * Returns HTTP 404 (Not Found) status
     * 
     * @param ex The exception that was thrown
     * @param request The web request that caused the exception
     * @return ResponseEntity with error details and 404 status
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Object> handleResourceNotFoundException(
            ResourceNotFoundException ex,
            WebRequest request) {

        // Create a response body with error details
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.NOT_FOUND.value());
        body.put("error", "Resource Not Found");
        body.put("message", ex.getMessage());

        // Return the response with 404 status
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    /**
     * Handler for DuplicateResourceException
     * Called when trying to create a resource that already exists
     * Returns HTTP 409 (Conflict) status
     * 
     * @param ex The exception that was thrown
     * @param request The web request that caused the exception
     * @return ResponseEntity with error details and 409 status
     */
    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<Object> handleDuplicateResourceException(
            DuplicateResourceException ex,
            WebRequest request) {

        // Create a response body with error details
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.CONFLICT.value());
        body.put("error", "Duplicate Resource");
        body.put("message", ex.getMessage());

        // Return the response with 409 status
        return new ResponseEntity<>(body, HttpStatus.CONFLICT);
    }

    /**
     * Handler for CourseCapacityException
     * Called when trying to enroll a student in a full course
     * Returns HTTP 400 (Bad Request) status
     * 
     * @param ex The exception that was thrown
     * @param request The web request that caused the exception
     * @return ResponseEntity with error details and 400 status
     */
    @ExceptionHandler(CourseCapacityException.class)
    public ResponseEntity<Object> handleCourseCapacityException(
            CourseCapacityException ex,
            WebRequest request) {

        // Create a response body with error details
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("error", "Course Capacity Full");
        body.put("message", ex.getMessage());

        // Return the response with 400 status
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    /**
     * Generic handler for any other unexpected exceptions
     * Returns HTTP 500 (Internal Server Error) status
     * 
     * This is a fallback handler for exceptions that don't have specific handlers
     * 
     * @param ex The exception that was thrown
     * @param request The web request that caused the exception
     * @return ResponseEntity with error details and 500 status
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGlobalException(
            Exception ex,
            WebRequest request) {

        // Create a response body with error details
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        body.put("error", "Internal Server Error");
        body.put("message", "An unexpected error occurred. Please contact support.");

        // Return the response with 500 status
        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
