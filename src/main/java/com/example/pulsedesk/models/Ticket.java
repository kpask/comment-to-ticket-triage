package com.example.pulsedesk.models;

import com.example.pulsedesk.enums.Category;
import com.example.pulsedesk.enums.Priority;

public record Ticket(
    int id,
    Category category,
    Priority priority,
    String title,
    String summary,
    Comment comment
){}
