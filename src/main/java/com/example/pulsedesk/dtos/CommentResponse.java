package com.example.pulsedesk.dtos;

import java.time.LocalDateTime;

public record CommentResponse(
        String text,
        LocalDateTime createdAt
) {}