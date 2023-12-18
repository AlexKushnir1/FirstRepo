package org.example.game;

import org.example.dto.GameStateDTO;
import org.example.dto.MoveDTO;
import org.example.myExeptions.MyCustomExceptions;

import java.util.Arrays;

public class Game {
    private Sign currentSign = Sign.X;
    private final int fieldSize = 3;
    private CellValue[][] gameField = newGameField();
    private boolean gameOver = false;

    public Game() {
    }

    public GameStateDTO move(MoveDTO move) throws MyCustomExceptions {
        if (gameOver) {
            throw new MyCustomExceptions("Game Over. Must start a new game");
        }
        if (isFull()) {
            gameOver = false;
            throw new MyCustomExceptions("Tie");
        }

        if (!(isInRange(move.getX(), move.getY()))) {
            throw new MyCustomExceptions("Coordinates must be within " + fieldSize);
        }
        if (!(isCellNull(move.getX(), move.getY()))) {
            throw new MyCustomExceptions("Cell " + move.getX() + " : " + move.getY() + " is not empty");
        }
        setStep(move);
        GameStateDTO gameStateDTO = getGameState();
        currentSign = currentSign.changeSign();
        System.out.println(gameStateDTO);
        return gameStateDTO;

    }

    public boolean isFull() {
        if (winner() != CellValue.NULL) {
            return false;
        }
        return Arrays.stream(gameField)
                .flatMap(Arrays::stream)
                .allMatch(value -> value == CellValue.X || value == CellValue.Y);
    }
    public Boolean isInRange(int x, int y) {
        return (y >= 0 && y < fieldSize && x >= 0 && x < fieldSize);
    }

    public void setStep(MoveDTO data) {
        gameField[data.getX()][data.getY()] = cellValueFromSing(currentSign);
    }

    public CellValue winner() {
        CellValue winner = CellValue.NULL;

        // Check rows
        for (int i = 0; i < 3; i++) {
            CellValue value = gameField[i][0];
            if (value != null && value.equals(gameField[i][1]) && value.equals(gameField[i][2])) {
                winner = value;
            }
        }

        // Check columns
        for (int j = 0; j < 3; j++) {
            CellValue value = gameField[0][j];
            if (value != null && value.equals(gameField[1][j]) && value.equals(gameField[2][j])) {
                winner = value;
            }
        }

        // Check diagonals
        CellValue center = gameField[1][1];
        if (center != null && ((center.equals(gameField[0][0]) && center.equals(gameField[2][2])) || (center.equals(gameField[0][2]) && center.equals(gameField[2][0])))) {
            winner = center;
        }

        if (winner != CellValue.NULL) {
            gameOver = true;
        }
        return winner;
    }
    public GameStateDTO getGameState(){
        return new GameStateDTO(gameField, winner(),
                currentSign, isFull());
    }
    public static CellValue cellValueFromSing(Sign sign){
        if (sign == Sign.X) {
            return CellValue.X;
        } else if (sign == Sign.Y) {
            return CellValue.Y;
        } else {
        return CellValue.NULL;
        }
    }
    public boolean isCellNull(int x, int y) {
        return (gameField[x][y] == CellValue.NULL);
    }

    public CellValue[][] getGameField() {
        return gameField;
    }

    public Sign getCurrentSign() {
        return currentSign;
    }

    public CellValue[][] newGameField() {
        CellValue[][] matrix = new CellValue[fieldSize][fieldSize];

        for (int i = 0; i < fieldSize; i++) {
            Arrays.fill(matrix[i], CellValue.NULL);
        }
        gameField = matrix;
        return matrix;
    }

    public void setNextSign() {
        this.currentSign = currentSign.changeSign();
    }
    public CellValue[][] newArray(int fieldSize) {
        CellValue[][] matrix = new CellValue[fieldSize][fieldSize];

        for (int i = 0; i < fieldSize; i++) {
            Arrays.fill(matrix[i], CellValue.NULL);
        }
        return matrix;
    }
}
