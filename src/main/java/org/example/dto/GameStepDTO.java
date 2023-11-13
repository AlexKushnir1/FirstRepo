package org.example.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GameStepDTO {
    @JsonProperty
    private int x;
    @JsonProperty
    private int y;
    public GameStepDTO(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public GameStepDTO() {
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
