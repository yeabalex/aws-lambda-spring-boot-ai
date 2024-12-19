package org.aws.service;

import org.aws.schema.RequestSchema;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class PromptService {
    @Value("${gemini.api.key}")
    private String apiKey;
    private String url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash-latest:generateContent?key=";
    private final RestTemplate restTemplate;

    public PromptService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String prompt(String prompt) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            RequestSchema.Part part = new RequestSchema.Part();
            part.setText(prompt);
            RequestSchema.Content content = new RequestSchema.Content();
            content.setParts(List.of(part));
            RequestSchema request = new RequestSchema();
            request.setContents(List.of(content));
            System.out.println(apiKey);
            HttpEntity<RequestSchema> requestEntity = new HttpEntity<>(request, headers);
            ResponseEntity<String> responseEntity = restTemplate.exchange(
                    url + apiKey,
                    HttpMethod.POST,
                    requestEntity,
                    String.class
            );

            return responseEntity.getBody();
        } catch (Exception e) {
            throw new RuntimeException("Error making Gemini API request", e);
        }
    }
}
