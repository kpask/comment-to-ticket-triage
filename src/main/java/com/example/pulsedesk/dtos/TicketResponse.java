package com.example.pulsedesk.dtos;

import com.example.pulsedesk.enums.Category;
import com.example.pulsedesk.enums.Priority;
import com.example.pulsedesk.enums.Status;

/**
 * Data Transfer Object for ticket responses.
 * Contains all ticket information including the original comment text.
 */
public record TicketResponse(
        int id,
        String title,
        String summary,
        Category category,
        Priority priority,
        Status status,
        String originalComment
) {}

