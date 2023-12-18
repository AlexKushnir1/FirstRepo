package org.example.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MoveDTO extends AbstractDataDTO{
    @JsonProperty
    private int x;
    @JsonProperty
    private int y;

    public MoveDTO(int x, int y) {
        super();
        this.x = x;
        this.y = y;
    }

    public MoveDTO() {
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }
}
