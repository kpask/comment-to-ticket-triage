package com.example.pulsedesk.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CommentRequest(
        @NotNull(message = "Comment text cannot be null")
        @NotBlank(message = "Comment text cannot be blank")
        @Size(min = 5, max = 255, message = "Comment must be between 5 and 255 characters")
        String text
) {}
