package org.aws.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.aws.schema.ServerRequestSchema;
import org.aws.service.PromptService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class PromptController {
    private final PromptService promptService;
    private final ObjectMapper objectMapper;

    public PromptController(PromptService promptService, ObjectMapper objectMapper) {
        this.promptService = promptService;
        this.objectMapper = objectMapper;
    }

    @PostMapping(value = "/api/microservice/ai", consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> promptMicroservice(@RequestBody Map<String, Object> proxyEvent) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> body = (Map<String, Object>) proxyEvent.get("body");
            String bodyJson = objectMapper.writeValueAsString(body);
            ServerRequestSchema serverRequestSchema = objectMapper.readValue(bodyJson, ServerRequestSchema.class);

            String prompt = serverRequestSchema.getPrompt();
            String response = promptService.prompt(prompt);

            return ResponseEntity.ok(Map.of("response", response));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "An error occurred while processing the request", "details", e.getMessage()));
        }
    }

}