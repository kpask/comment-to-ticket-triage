package com.example.pulsedesk.dtos;

import jakarta.validation.constraints.Size;

public record CommentRequest(
        @Size(max = 255, message = "Comment must not exceed 255 characters.")
        String text
) {}
