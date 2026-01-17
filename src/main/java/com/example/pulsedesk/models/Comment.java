package com.example.pulsedesk.models;

import java.time.LocalDateTime;

public record Comment(
    int id,
    String text,
    LocalDateTime createdAt
){}
