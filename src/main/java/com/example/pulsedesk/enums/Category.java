package com.example.pulsedesk.enums;

/**
 * Enumeration of possible ticket categories for support tickets.
 * Used to classify the type of issue or request described in a ticket.
 */
public enum Category {
    /** Software defects, errors, or unexpected behavior that need fixing */
    BUG,
    /** New feature requests or enhancement suggestions for the product */
    FEATURE,
    /** Issues related to billing, payments, or subscription management */
    BILLING,
    /** Problems with user accounts, authentication, or profile management */
    ACCOUNT,
    /** Issues that don't fit into the other predefined categories */
    OTHER
}
