package org.example.dto;
import com.fasterxml.jackson.annotation.JsonProperty;

public class MessageDTO {
    @JsonProperty("message")
    private String message;

    public String getMessage() {
        return message;

    }
}
