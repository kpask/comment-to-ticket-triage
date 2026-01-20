package com.example.pulsedesk.enums;

/**
 * Enumeration of possible ticket statuses for support tickets.
 * Used to track the current state of ticket processing and resolution.
 */
public enum Status {
    /** Newly created ticket awaiting assignment and initial review */
    OPEN,
    /** Ticket is actively being worked on by support staff */
    IN_PROGRESS,
    /** Ticket has been resolved and closed */
    CLOSED
}
