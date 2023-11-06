package org.example;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GameResult {
    @JsonProperty
    private String[][] gameField;
    @JsonProperty
    private String winner;
    @JsonProperty
    private String sign;

    public GameResult() {
    }

    public GameResult(String[][] gameField, String winner, String sign) {
        this.gameField = gameField;
        this.winner = winner;
        this.sign = sign;
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
}
