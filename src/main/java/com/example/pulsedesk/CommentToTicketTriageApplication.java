package com.example.pulsedesk;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * Main Spring Boot application class for the Comment-to-Ticket Triage system.
 * This application provides REST APIs for managing user comments and automatically
 * creating support tickets using AI analysis via Hugging Face.
 */
@SpringBootApplication
@EnableAsync
public class CommentToTicketTriageApplication {

	public static void main(String[] args) {
		SpringApplication.run(CommentToTicketTriageApplication.class, args);
	}

}
