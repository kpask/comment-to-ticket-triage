package com.example.pulsedesk.dtos;

import com.example.pulsedesk.enums.Category;
import com.example.pulsedesk.enums.Priority;

/**
 * Data Transfer Object for AI analysis responses.
 * Contains the decision whether to create a ticket and the generated ticket details.
 */
public record AiTicketResponse(
        boolean createTicket,
        String title,
        String summary,
        Category category,
        Priority priority
){}
