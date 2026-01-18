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

    private AiTicketResponse parseAiResponse(String aiResponse) throws Exception {
        JsonNode content = objectMapper.readTree(aiResponse)
                .path("choices").path(0).path("message").path("content");

        if (content.isMissingNode()) return NO_TICKET;

        String json = extractJson(content.asText());
        if (json == null) return NO_TICKET;

        JsonNode data = objectMapper.readTree(json);
        if (!data.path("createTicket").asBoolean(false)) return NO_TICKET;

        return new AiTicketResponse(
                true,
                data.path("title").asText(),
                data.path("summary").asText(),
                Category.valueOf(data.path("category").asText().toUpperCase()),
                Priority.valueOf(data.path("priority").asText().toUpperCase())
        );
    }

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

    private String extractJson(String text) {
        int start = text.indexOf("{");
        int end = text.lastIndexOf("}");
        if (start >= 0 && end > start) {
            return text.substring(start, end + 1);
        }
        return null;
    }
}
