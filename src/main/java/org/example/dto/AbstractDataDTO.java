package org.example.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.UUID;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "message")
@JsonSubTypes({
        @JsonSubTypes.Type(value = MoveDTO.class, name = "move"),
        @JsonSubTypes.Type(value = NewGameDTO.class, name = "newGame"),
        @JsonSubTypes.Type(value = GetStateDTO.class, name = "getState")
})

public abstract class AbstractDataDTO {
    @JsonProperty
    private UUID sessionId;

    public UUID getSessionId() {
        return sessionId;
    }

    public void setSessionId(UUID sessionId) {
        this.sessionId = sessionId;
    }

    @Override
    public String toString() {
        return "AbstractDataDTO{" +
                "session_id=" + sessionId +
                '}';
    }
}
