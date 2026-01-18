package com.example.pulsedesk.dtos;

import com.example.pulsedesk.enums.Category;
import com.example.pulsedesk.enums.Priority;

public record AiTicketResponse(
        boolean createTicket,
        String title,
        String summary,
        Category category,
        Priority priority
){}
