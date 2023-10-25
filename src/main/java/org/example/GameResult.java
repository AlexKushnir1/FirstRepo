package org.example;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GameResult {
    @JsonProperty
    private String[][] gameField;
    @JsonProperty
    private String winner;

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

    public GameResult(String[][] gameField, String winner) {
        this.gameField = gameField;
        this.winner = winner;
    }

    public GameResult() {
    }
}
