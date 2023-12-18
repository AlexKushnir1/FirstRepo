package org.example.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.example.game.CellValue;
import org.example.game.Sign;

public class GameStateDTO {
    @JsonProperty
    private CellValue[][] gameField;
    @JsonProperty
    private CellValue winner;
    @JsonProperty
    private Sign sign;
    @JsonProperty
    private Boolean tie;

    public GameStateDTO() {
    }

    public GameStateDTO(CellValue[][] gameField, CellValue winner, Sign sign, Boolean tie) {
        this.gameField = gameField;
        this.winner = winner;
        this.sign = sign;
        this.tie = tie;
    }

    public CellValue[][] getGameField() {
        return gameField;
    }

    public void setGameField(CellValue[][] gameField) {
        this.gameField = gameField;
    }

    public CellValue getWinner() {
        return winner;
    }

    public void setWinner(CellValue winner) {
        this.winner = winner;
    }

    public Sign getSign() {
        return sign;
    }

    public void setSign(Sign sign) {
        this.sign = sign;
    }

    public Boolean getTie() {
        return tie;
    }

    public void setTie(Boolean tie) {
        this.tie = tie;
    }
}
