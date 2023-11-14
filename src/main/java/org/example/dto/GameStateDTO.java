package org.example.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GameStateDTO {
    @JsonProperty
    private String[][] gameField;
    @JsonProperty
    private String winner;
    @JsonProperty
    private String sign;
    @JsonProperty
    private Boolean tie;

    public GameStateDTO() {
    }

    public GameStateDTO(String[][] gameField, String winner, String sign, Boolean tie) {
        this.gameField = gameField;
        this.winner = winner;
        this.sign = sign;
        this.tie = tie;
    }

    public String[][] getGameField() {
        return gameField;
    }

    public void setGameField(String[][] gameField) {
        this.gameField = gameField;
    }

    public String getWinner() {
        return winner;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public Boolean getTie() {
        return tie;
    }

    public void setTie(Boolean tie) {
        this.tie = tie;
    }
}
