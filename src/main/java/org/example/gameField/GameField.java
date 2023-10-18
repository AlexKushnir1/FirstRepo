package org.example.gameField;

public class GameField {
    public static String[][] gameField = new String[3][3];

    public void makeMove(int x, int y, String sign) {
        gameField[x][y] = sign;
        System.out.println("make-move");
    }

    public String[][] getGameField() {
        return gameField;
    }
}
