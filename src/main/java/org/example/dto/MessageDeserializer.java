package org.example.dto;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

public class MessageDeserializer extends JsonDeserializer<AbstractDataDTO> {
    @Override
    public AbstractDataDTO deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonParseException {
        ObjectCodec codec = p.getCodec();
        JsonNode node = codec.readTree(p);

        String messageType = node.get("message").asText();

        switch (messageType) {
            case "move":
                return codec.readValue(node.traverse(), StepForMoveDTO.class);
            case "getState":
                return codec.readValue(node.traverse(), GetStateDTO.class);
            case "newGame":
                return codec.readValue(node.traverse(), NewGameDTO.class);
            default:
                throw new IllegalArgumentException("Unknown message type: " + messageType);
        }
    }
}
