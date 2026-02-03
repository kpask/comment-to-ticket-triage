package com.example.pulsedesk.dtos;

import java.time.LocalDateTime;

/**
 * Data Transfer Object for comment responses.
 * Contains the comment text and creation timestamp returned by the API.
 */
public record CommentResponse(
        String text,
        LocalDateTime createdAt
) {}