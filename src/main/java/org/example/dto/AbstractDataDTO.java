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
    private UUID session_id;

    public UUID getSession_id() {
        return session_id;
    }

    public void setSession_id(UUID session_id) {
        this.session_id = session_id;
    }

    @Override
    public String toString() {
        return "AbstractDataDTO{" +
                "session_id=" + session_id +
                '}';
    }
}
