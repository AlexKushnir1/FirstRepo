package org.example;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.dto.GameStepDTO;

import java.util.Arrays;
import java.util.Objects;

import static spark.Spark.*;


public class Main {
    private static String[][] gameField = new String[3][3];
    private static boolean gameOver = true;

    public static void main(String[] args) {
        port(8080);
        ObjectMapper objectMapper = new ObjectMapper();
        options("/*", (request, response) -> {
            String accessControlRequestHeaders = request.headers("Access-Control-Request-Headers");
            if (accessControlRequestHeaders != null) {
                response.header("Access-Control-Allow-Headers", accessControlRequestHeaders);
            }

            String accessControlRequestMethod = request.headers("Access-Control-Request-Method");
            if (accessControlRequestMethod != null) {
                response.header("Access-Control-Allow-Methods", accessControlRequestMethod);
            }

            return "OK";
        });

        before((request, response) -> {
            response.header("Access-Control-Allow-Origin", "*"); // Дозволяє запити з будь-якого джерела (*).
        });

        post("/move", (request, response) -> {
            if (gameOver) {
                try {
                    //gameStep -крок гри
                    GameStepDTO step = objectMapper.readValue(request.body(), GameStepDTO.class);
                    boolean isFull = Arrays.stream(gameField)
                            .flatMap(Arrays::stream)
                            .noneMatch(Objects::isNull);

                    if (!(Objects.equals(step.getSign(), "x")) && !(Objects.equals(step.getSign(), "x"))) {
                        response.status(400);
                        return "Sign must be x or o";
                    }
                    if (step.getY() < 0 || step.getY() > 2 || step.getX() < 0 || step.getX() > 2) {
                        response.status(400);
                        return "Coordinate should be 0, 1, or 2";
                    }
                    if (gameField[step.getX()][step.getY()] == null) {
                        String[][] gameField1 = setStep(step, gameField);
                        GameResult gameResult = new GameResult(gameField1, wins(gameField1));
                        System.out.println(gameResult.getWinner());
                        response.body(objectMapper.writeValueAsString(gameResult));
                        return response.body();
                    } else if (isFull) {
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

        post("/new_game", (request, response) -> {
            gameField = new String[3][3];
            for (int i = 0; i <= 2; i++) {
                for (int j = 0; j <= 2; j++) {
                    System.out.print("   " + gameField[i][j]);
                }
                System.out.println(" ");
            }
            gameOver = true;
            response.body(objectMapper.writeValueAsString(gameField));
            return response.body();
        });
    }

    public static String wins(String[][] board) {
        String winner = "";

        // Check rows
        for (int i = 0; i < 3; i++) {
            String str = board[i][0];
            if (str != null && str.equals(board[i][1]) && str.equals(board[i][2])) {
                winner = str;
            }
        }

        // Check columns
        for (int j = 0; j < 3; j++) {
            String str = board[0][j];
            if (str != null && str.equals(board[1][j]) && str.equals(board[2][j])) {
                winner = str;
            }
        }

        // Check diagonals
        String center = board[1][1];
        if (center != null && ((center.equals(board[0][0]) && center.equals(board[2][2])) || (center.equals(board[0][2]) && center.equals(board[2][0])))) {
            winner = center;
        }

        if (!winner.isEmpty()) {
            gameOver = true;
        }

        return winner;
//        //check rows
//        for (int i = 0; i <= 3; i++) {
//            if (board[i][0] == null) {
//                break;
//            }
//            String str = board[i][0];
//            if (str.equals(board[i][1]) && str.equals(board[i][2])) {
//                return getWinner(str);
//            }
//        }
//        //check columns
//        for (int j = 0; j <= 3; j++) {
//            if (board[0][j] == null) {
//                break;
//            }
//            String str = board[0][j];
//            if (str.equals(board[1][j]) && str.equals(board[2][j])) {
//                return getWinner(str);
//            }
//        }
//        //check diagonals
//        if (board[1][1] == null) {
//            return "";
//        }
//        String str = board[1][1];
//        if (str.equals(board[0][0]) && str.equals(board[2][2]) || (str.equals(board[0][2]) && str.equals(board[2][0]))) {
//            return getWinner(str);
//        }
//        return "";
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