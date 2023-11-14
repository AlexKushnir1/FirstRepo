package org.example.game;

import org.example.dto.GameStepDTO;

import java.util.Arrays;
import java.util.Objects;

public class GameLogic {

    private static boolean gameOver = false;

    public static String[][] setCleanArray(int arraySize) {
        return new String[arraySize][arraySize];
    }

    public static Boolean isFull(String[][] gameField) {
        if (!(wins(gameField) == Sign.NULL)) {
            return false;
        }
        return Arrays.stream(gameField)
                .flatMap(Arrays::stream)
                .noneMatch(Objects::isNull);
    }

    public static Boolean isNumbWithinAnArray(int x, int y, int n) {
        return (y < 0 || y > n || x < 0 || x > n);
    }

    public static String[][] setStep(GameStepDTO data, String[][] mainGameField, Sign sign) {
        int x = data.getX();
        int y = data.getY();

        mainGameField[x][y] = sign.getTitle();

        for (int i = 0; i <= 2; i++) {
            for (int j = 0; j <= 2; j++) {
                System.out.print("   " + mainGameField[i][j]);
            }
            System.out.println(" ");
        }
        return mainGameField;
    }

    public static Sign wins(String[][] board) {
        Sign winner = Sign.NULL;

        // Check rows
        for (int i = 0; i < 3; i++) {
            String str = board[i][0];
            if (str != null && str.equals(board[i][1]) && str.equals(board[i][2])) {
                winner = Sign.valueOf(str.toUpperCase());
            }
        }

        // Check columns
        for (int j = 0; j < 3; j++) {
            String str = board[0][j];
            if (str != null && str.equals(board[1][j]) && str.equals(board[2][j])) {
                winner = Sign.valueOf(str.toUpperCase());
            }
        }

        // Check diagonals
        String center = board[1][1];
        if (center != null && ((center.equals(board[0][0]) && center.equals(board[2][2])) || (center.equals(board[0][2]) && center.equals(board[2][0])))) {
            winner = Sign.valueOf(center.toUpperCase());
        }

        if (winner != Sign.NULL) {
            gameOver = true;
        }

        return winner;
    }

    public static boolean isGameOver() {
        return gameOver;
    }

    public static void setGameOver(boolean gameOver) {
        GameLogic.gameOver = gameOver;
    }
}
