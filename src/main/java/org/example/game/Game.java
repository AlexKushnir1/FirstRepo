package org.example.game;

import org.example.dto.GameStepDTO;

import java.util.Arrays;
import java.util.Objects;

public class Game {
    private String[][] gameField;
    private Sign sign = Sign.X;
    private int fieldSize = 3;
    private boolean gameOver = false;

    public Game() {
    }

    public Boolean isFull() {
        if (!(wins() == sign.NULL)) {
            return false;
        }
        return Arrays.stream(gameField)
                .flatMap(Arrays::stream)
                .noneMatch(Objects::isNull);
    }

    public Boolean isNumbWithinAnArray(int x, int y) {
        return (y >= 0 && y < fieldSize && x >= 0 && x < fieldSize);
    }

    public void setStep(GameStepDTO data) {
        int x = data.getX();
        int y = data.getY();

        gameField[x][y] = sign.getTitle();

        for (int i = 0; i <= 2; i++) {
            for (int j = 0; j <= 2; j++) {
                System.out.print("   " + gameField[i][j]);
            }
            System.out.println(" ");
        }
    }

    public Sign wins() {
        Sign winner = Sign.NULL;

        // Check rows
        for (int i = 0; i < 3; i++) {
            String str = gameField[i][0];
            if (str != null && str.equals(gameField[i][1]) && str.equals(gameField[i][2])) {
                winner = Sign.valueOf(str.toUpperCase());
            }
        }

        // Check columns
        for (int j = 0; j < 3; j++) {
            String str = gameField[0][j];
            if (str != null && str.equals(gameField[1][j]) && str.equals(gameField[2][j])) {
                winner = Sign.valueOf(str.toUpperCase());
            }
        }

        // Check diagonals
        String center = gameField[1][1];
        if (center != null && ((center.equals(gameField[0][0]) && center.equals(gameField[2][2])) || (center.equals(gameField[0][2]) && center.equals(gameField[2][0])))) {
            winner = Sign.valueOf(center.toUpperCase());
        }

        if (winner != Sign.NULL) {
            gameOver = true;
        }

        return winner;
    }

    public boolean isCellNotNull(int x, int y) {
        return !(gameField[x][y] == null);

    }

    public boolean getGameOver() {
        return gameOver;
    }

    public int getFieldSize() {
        return fieldSize;
    }

    public String[][] getGameField() {
        return gameField;
    }

    public Sign getSign() {
        return sign;
    }

    public void setCleanArray() {
        this.gameField = new String[fieldSize][fieldSize];
    }

    public void setNextSign() {
        this.sign = sign.getNextSign();
    }

    public void setSign(Sign sign) {
        this.sign = sign;
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }
}
