package com.example.pulsedesk.exceptions;

/**
 * Exception thrown when a requested comment is not found in the system.
 */
public class CommentNotFoundException extends RuntimeException {
    public CommentNotFoundException(String message) {
        super(message);
    }
}
