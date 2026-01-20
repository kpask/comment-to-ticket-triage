package com.example.pulsedesk.service;

import com.example.pulsedesk.dtos.AiTicketResponse;
import com.example.pulsedesk.enums.Category;
import com.example.pulsedesk.enums.Priority;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.util.*;

/**
 * Service for analyzing user comments using the Hugging Face Inference API.
 * Determines if a comment should be converted into a support ticket and generates ticket details.
 */
@Service
public class AiAnalysisService {

    @Value("${huggingface.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    private static final String MODEL_URL = "https://router.huggingface.co/v1/chat/completions";
    private static final String MODEL_NAME = "zai-org/GLM-4.7:novita";

    private static final AiTicketResponse NO_TICKET =
            new AiTicketResponse(false, null, null, null, null);

    /**
     * Analyzes a user comment using the Hugging Face Inference API.
     *
     * @param commentText the text of the user comment to analyze.
     * @return an AiTicketResponse containing the analysis result and ticket details if applicable.
     */
    public AiTicketResponse analyzeComment(String commentText) {
        if (commentText == null || commentText.isBlank()) {
            return NO_TICKET;
        }

        Map<String, Object> requestBody = Map.of(
                "model", MODEL_NAME,
                "messages", List.of(
                        Map.of("role", "user", "content", buildPrompt(commentText))
                ),
                "stream", false
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(apiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<String> response =
                    restTemplate.postForEntity(MODEL_URL, entity, String.class);

            if (response.getBody() == null || response.getBody().isBlank()) {
                return NO_TICKET;
            }

            return parseAiResponse(response.getBody());

        } catch (Exception e) {
            System.err.println("AI service error: " + e.getMessage());
            return NO_TICKET;
        }
    }

    /**
     * Parses the AI response to extract ticket details.
     *
     * @param aiResponse the raw response from the AI API.
     * @return an AiTicketResponse containing the parsed ticket details.
     */
    private AiTicketResponse parseAiResponse(String aiResponse) {
        JsonNode content = objectMapper.readTree(aiResponse)
                .path("choices").path(0).path("message").path("content");

        if (content.isMissingNode()) return NO_TICKET;

        String json = extractJson(content.asText());
        if (json == null) return NO_TICKET;

        JsonNode data = objectMapper.readTree(json);
        if (!data.path("createTicket").asBoolean(false)) return NO_TICKET;

        Category category;
        Priority priority;

        try {
            category = Category.valueOf(data.path("category").asText().toUpperCase());
        } catch (IllegalArgumentException e) {
            category = Category.OTHER; // Default to OTHER if invalid
        }

        try {
            priority = Priority.valueOf(data.path("priority").asText().toUpperCase());
        } catch (IllegalArgumentException e) {
            priority = Priority.MEDIUM; // Default to MEDIUM if invalid
        }

        return new AiTicketResponse(
                true,
                data.path("title").asText(),
                data.path("summary").asText(),
                category,
                priority
        );
    }

    /**
     * Builds the prompt to send to the AI model for comment analysis.
     *
     * @param commentText the text of the user comment.
     * @return the formatted prompt string.
     */
    private String buildPrompt(String commentText) {
        return "You are a support ticket classifier.\n" +
                "Do not include markdown, code blocks, or explanations.\n" +
                "Analyze the following user comment and decide if it should become a support ticket.\n" +
                "If not, respond: {\"createTicket\": false}\n" +
                "If yes, respond with JSON containing the following fields ONLY:\n" +
                "{ \"createTicket\": true, \"title\": \"<short title>\", " +
                "\"category\": \"BUG|FEATURE|BILLING|ACCOUNT|OTHER\", " +
                "\"priority\": \"LOW|MEDIUM|HIGH\", \"summary\": \"<short summary>\" }\n" +
                "Comment: \"" + commentText + "\"";
    }

    /**
     * Extracts the JSON content from the AI response text.
     *
     * @param text the AI response text.
     * @return the extracted JSON string or null if extraction fails.
     */
    private String extractJson(String text) {
        if (text == null) return null;

        text = text.trim();
        if (!text.startsWith("{")) return null;

        try {
            objectMapper.readTree(text);
            return text;
        } catch (Exception e) {
            return null;
        }
    }

}
