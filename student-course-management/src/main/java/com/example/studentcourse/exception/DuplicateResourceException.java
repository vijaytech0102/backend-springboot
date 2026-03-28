package com.example.studentcourse.exception;

/**
 * DuplicateResourceException
 * Custom exception thrown when attempting to create a resource that already exists
 * 
 * For example:
 * - Creating a student with an email that is already registered
 * - Creating a course with a course code that already exists
 * 
 * This is also a simple RuntimeException (unchecked exception)
 */
public class DuplicateResourceException extends RuntimeException {

    /**
     * Constructor with message only
     * 
     * @param message The error message describing what resource already exists
     */
    public DuplicateResourceException(String message) {
        super(message);
    }

    /**
     * Constructor with message and cause
     * 
     * @param message The error message
     * @param cause The original exception that caused this
     */
    public DuplicateResourceException(String message, Throwable cause) {
        super(message, cause);
    }
}
