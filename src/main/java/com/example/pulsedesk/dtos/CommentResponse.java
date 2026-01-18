package com.example.pulsedesk.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public record CommentResponse(
        @NotBlank
        @Size(min = 5, message = "Comment must be at least 5 characters")
        String text,
        LocalDateTime createdAt
) {}