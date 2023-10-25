package org.example.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GameStepDTO {
    @JsonProperty
    private int x;
    @JsonProperty
    private int y;
    @JsonProperty
    private String sign;
    public GameStepDTO(int x, int y, String sign) {
        this.x = x;
        this.y = y;
        this.sign = sign;
    }

    public GameStepDTO() {
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public String getSign() {
        return sign;
    }
}
