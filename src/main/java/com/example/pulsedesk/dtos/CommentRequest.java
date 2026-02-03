package com.example.pulsedesk.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO representing a request to create a new comment.
 * Validates that the comment text is between 5 and 255 characters and not blank.
 */
public record CommentRequest(
        @NotBlank(message = "Comment text cannot be blank")
        @Size(min = 5, max = 255, message = "Comment must be between 5 and 255 characters")
        String text
) {}
