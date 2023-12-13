package org.example.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class StepForMoveDTO extends AbstractDataDTO{
    @JsonProperty
    private int x;
    @JsonProperty
    private int y;

    public StepForMoveDTO(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public StepForMoveDTO() {
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
