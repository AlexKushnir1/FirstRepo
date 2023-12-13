package org.example.game;

import org.example.dto.GameStateDTO;
import org.example.dto.StepForMoveDTO;
import org.example.myExeptions.MyCustomExceptions;

import java.util.Arrays;
import java.util.Objects;

public class Game {
    private CellValue cellValue = CellValue.X;
    private final int fieldSize = 3;
    private String[][] gameField = new String[fieldSize][fieldSize];
    private boolean gameOver = false;

    public Game() {
    }

    public GameStateDTO move(StepForMoveDTO step) throws MyCustomExceptions {
        if (gameOver) {
            throw new MyCustomExceptions("Game Over. Must start a new game");
        }
        if (isFull()) {
            gameOver = false;
            throw new MyCustomExceptions("Tie");
        }


        if (!(isInRange(step.getX(), step.getY()))) {
            throw new MyCustomExceptions("Coordinates must be within " + fieldSize);
        }
        if (!(isCellNull(step.getX(), step.getY()))) {
            throw new MyCustomExceptions("Cell " + step.getX() + " : " + step.getY() + " is not empty");
        }
        setStep(step);
        GameStateDTO gameStateDTO = getGameState();
        cellValue = cellValue.getNextSign();
        return gameStateDTO;

    }

    public String[][] new_game() {
        cellValue = CellValue.X;
        newGameField();
        gameOver = false;
        return getGameField();
    }

    public Boolean isFull() {
        if (!(winner() == CellValue.NULL)) {
            return false;
        }
        return Arrays.stream(gameField)
                .flatMap(Arrays::stream)
                .noneMatch(Objects::isNull);
    }

    public Boolean isInRange(int x, int y) {
        return (y >= 0 && y < fieldSize && x >= 0 && x < fieldSize);
    }

    public void setStep(StepForMoveDTO data) {
        gameField[data.getX()][data.getY()] = cellValue.getTitle();
    }

    public CellValue winner() {
        CellValue winner = CellValue.NULL;

        // Check rows
        for (int i = 0; i < 3; i++) {
            String str = gameField[i][0];
            if (str != null && str.equals(gameField[i][1]) && str.equals(gameField[i][2])) {
                winner = CellValue.valueOf(str.toUpperCase());
            }
        }

        // Check columns
        for (int j = 0; j < 3; j++) {
            String str = gameField[0][j];
            if (str != null && str.equals(gameField[1][j]) && str.equals(gameField[2][j])) {
                winner = CellValue.valueOf(str.toUpperCase());
            }
        }

        // Check diagonals
        String center = gameField[1][1];
        if (center != null && ((center.equals(gameField[0][0]) && center.equals(gameField[2][2])) || (center.equals(gameField[0][2]) && center.equals(gameField[2][0])))) {
            winner = CellValue.valueOf(center.toUpperCase());
        }

        if (winner != CellValue.NULL) {
            gameOver = true;
        }
        return winner;
    }
    public GameStateDTO getGameState(){
        return new GameStateDTO(gameField, winner().getTitle(),
                cellValue.getTitle(), isFull());
    }
    public boolean isCellNull(int x, int y) {
        return (gameField[x][y] == null);
    }

    public int getFieldSize() {
        return fieldSize;
    }

    public String[][] getGameField() {
        return gameField;
    }

    public CellValue getSign() {
        return cellValue;
    }

    public void newGameField() {
        this.gameField = new String[fieldSize][fieldSize];
    }

    public void setNextSign() {
        this.cellValue = cellValue.getNextSign();
    }
}
