package org.example;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.dto.GameStepDTO;

import static spark.Spark.port;
import static spark.Spark.post;


public class Main {
    private static String[][] gameField = new String[3][3];
    private static boolean gameOver = true;

    public static void main(String[] args) {
        port(8080);
        ObjectMapper objectMapper = new ObjectMapper();
        post("/move", (request, response) -> {
            if (gameOver) {

                try {
                    //gameStep -крок гри
                    GameStepDTO step = objectMapper.readValue(request.body(), GameStepDTO.class);
                    if (gameField[step.getX()][step.getY()] == null) {
                        gameField = setStep(step, gameField);
                        System.out.println(wins(gameField));
                        GameResult gameResult = new GameResult(gameField, wins(gameField));
                        response.body(objectMapper.writeValueAsString(gameResult));
                        return response.body();
                    } else if (wins(gameField).isEmpty()) {
                        return "Tie";
                    } else {
                        return "Cell " + step.getX() + " : " + step.getY() + " is not empty";
                    }
                } catch (JsonMappingException e) {
                    System.out.println(e);
                    response.status(400);
                    return "Bad request. Json can`t map data";
                } catch (JsonProcessingException e) {
                    System.out.println(e);
                    response.status(400);
                    return "Bad request. Json can`t process";
                }
            } else
                return "Must start a new game";
        });
        post("/get", (request, response) -> {
            try {
                //Json that send to client
                response.body(objectMapper.writeValueAsString(gameField));
                return response.body();
            } catch (JsonProcessingException e) {
                response.status(400);
                return "Bad request. Can`t write data to Json";
            }
        });
        post("/new_game", (request, response) -> {
//            response.body(objectMapper.writeValueAsString(gameField));
//            response.body("New game created/ for moving send Post request with coordinates");
            gameField = new String[3][3];
            return response.body();
        });
    }

    private static String wins(String[][] board) {
        //check rows
        for (int i = 0; i < 3; i++) {
            if (board[i][0] == null) {
                break;
            }
            String str = board[i][0];
            if (str.equals(board[i][1]) && str.equals(board[i][2])) {
                return getWinner(str);
            }
        }
        //check columns
        for (int j = 0; j < 3; j++) {
            if (board[0][j] == null) {
                break;
            }
            String str = board[0][j];
            if (str.equals(board[1][j]) && str.equals(board[2][j])) {
                return getWinner(str);
            }
        }
        //check diagonals
        if (board[1][1] == null) {
            return "";
        }
        String str = board[1][1];
        if (str.equals(board[0][0]) && str.equals(board[2][2]) || (str.equals(board[0][2]) && str.equals(board[2][0]))) {
            return getWinner(str);
        }
        return "";
    }

    private static String getWinner(String str) {
        gameOver = false;
        if (str.equals("x")) {
            return "x";
        } else {
            return "o";
        }
    }


    public static String[][] setStep(GameStepDTO data, String[][] mainGameField) {
        int x = data.getX();
        int y = data.getY();
        String sign = data.getSign();

        mainGameField[x][y] = sign;

        for (int i = 0; i <= 2; i++) {
            for (int j = 0; j <= 2; j++) {
                System.out.print("   " + mainGameField[i][j]);
            }
            System.out.println(" ");
        }
        return mainGameField;
    }
}