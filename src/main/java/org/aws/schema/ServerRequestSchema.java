package org.aws.schema;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ServerRequestSchema {
    @JsonProperty("prompt")
    private String prompt;

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    public String getPrompt() {
        return prompt;
    }
}
