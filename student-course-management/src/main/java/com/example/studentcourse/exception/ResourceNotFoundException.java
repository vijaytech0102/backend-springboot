package com.example.studentcourse.exception;

/**
 * ResourceNotFoundException
 * Custom exception thrown when a requested resource (Student or Course) is not found
 * 
 * This is a simple RuntimeException that extends Exception
 * RuntimeException is an unchecked exception (does not need to be declared in method signature)
 */
public class ResourceNotFoundException extends RuntimeException {

    /**
     * Constructor with message only
     * 
     * @param message The error message describing what resource was not found
     */
    public ResourceNotFoundException(String message) {
        super(message);
    }

    /**
     * Constructor with message and cause
     * Useful for wrapping other exceptions
     * 
     * @param message The error message
     * @param cause The original exception that caused this
     */
    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
