package org.example;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.dto.GameStateDTO;
import org.example.dto.GameStepDTO;
import org.example.game.GameLogic;
import org.example.game.Sign;

import static spark.Spark.*;


public class Main {
    private static String[][] gameField;
    private static Sign nextSign = Sign.X;
    static ObjectMapper objectMapper = new ObjectMapper();
    private static int arraySize = 3;

    public static void main(String[] args) {
        gameField = GameLogic.setCleanArray(arraySize);
        port(8080);
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
            response.header("Access-Control-Allow-Origin", "*"); // Accept request from any port (*).
        });

        post("/move", (request, response) -> {
            if (GameLogic.isGameOver()) {
                response.status(400);
                return "Game Over. Must start a new game";
            }
            try {
                GameStepDTO step = objectMapper.readValue(request.body(), GameStepDTO.class);
                if (GameLogic.isFull(gameField)) {
                    response.status(400);
                    GameLogic.setGameOver(false);
                    return "Tie";
                }

                if (GameLogic.isNumbWithinAnArray(step.getX(), step.getY(), arraySize)) {
                    response.status(400);
                    return "Coordinates must be within " + arraySize;
                }
                if (!(gameField[step.getX()][step.getY()] == null)) {
                    response.status(400);
                    return "Cell " + step.getX() + " : " + step.getY() + " is not empty";
                }
                GameLogic.setStep(step, gameField, nextSign);
                response.body(objectMapper.writeValueAsString(new GameStateDTO(gameField, GameLogic.wins(gameField).getTitle(), nextSign.getTitle(), GameLogic.isFull(gameField))));
                nextSign = nextSign.getNextSign();
                return response.body();

            } catch (JacksonException e) {
                System.out.println(e);
                response.status(400);
                return "Bad request. Json can`t map data";
            }
        });

        post("/new_game", (request, response) ->

        {
            nextSign = Sign.X;
            gameField = GameLogic.setCleanArray(arraySize);
            for (int i = 0; i <= 2; i++) {
                for (int j = 0; j <= 2; j++) {
                    System.out.print("   " + gameField[i][j]);
                }
                System.out.println(" ");
            }
            GameLogic.setGameOver(false);
            response.body(objectMapper.writeValueAsString(gameField));
            return response.body();
        });
    }

    public static int getArraySize() {
        return arraySize;
    }
}
