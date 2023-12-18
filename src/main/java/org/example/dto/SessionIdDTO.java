package org.example.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public class SessionIdDTO {
    @JsonProperty
    private UUID session_id;

    public UUID getSession_id() {
        return session_id;
    }

    public void setSession_id(UUID session_id) {
        this.session_id = session_id;
    }

    public SessionIdDTO() {
    }
}
