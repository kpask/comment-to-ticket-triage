package com.example.pulsedesk.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

/**
 * Data Transfer Object for comment responses.
 * Contains the comment text and creation timestamp returned by the API.
 */
public record CommentResponse(
        @NotBlank
        @Size(min = 5, message = "Comment must be at least 5 characters")
        String text,
        LocalDateTime createdAt
) {}