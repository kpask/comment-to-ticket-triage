package com.example.pulsedesk.exceptions;

/**
 * Exception thrown when a requested ticket is not found in the system.
 */
public class TicketNotFoundException extends RuntimeException {
    public TicketNotFoundException(String message) {
        super(message);
    }
}
