package org.example;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.dto.GameStateDTO;
import org.example.dto.GameStepDTO;
import org.example.game.Game;
import org.example.game.Sign;

import static spark.Spark.*;

public class Main {
    static ObjectMapper objectMapper = new ObjectMapper();

    public static void main(String[] args) {
        Game game = new Game();
        game.setCleanArray();
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
            if (game.getGameOver()) {
                response.status(400);
                return "Game Over. Must start a new game";
            }
            try {
                GameStepDTO step = objectMapper.readValue(request.body(), GameStepDTO.class);
                if (game.isFull()) {
                    response.status(400);
                    game.setGameOver(false);
                    return "Tie";
                }

                if (!(game.isNumbWithinAnArray(step.getX(), step.getY()))) {
                    response.status(400);
                    return "Coordinates must be within " + game.getFieldSize();
                }
                if (game.isCellNotNull(step.getX(), step.getY())) {
                    response.status(400);
                    return "Cell " + step.getX() + " : " + step.getY() + " is not empty";
                }
                game.setStep(step);
                response.body(objectMapper.writeValueAsString(new GameStateDTO(game.getGameField(), game.wins().getTitle(), game.getSign().getTitle(), game.isFull())));
                game.setNextSign();
                return response.body();

            } catch (JacksonException e) {
                System.out.println(e);
                response.status(400);
                return "Bad request. Json can`t map data";
            }
        });

        post("/new_game", (request, response) ->

        {
            game.setSign(Sign.X);
            game.setCleanArray();
            for (int i = 0; i <= 2; i++) {
                for (int j = 0; j <= 2; j++) {
                    System.out.print("   " + game.getGameField()[i][j]);
                }
                System.out.println(" ");
            }
            game.setGameOver(false);
            response.body(objectMapper.writeValueAsString(game.getGameField()));
            return response.body();
        });
    }
}
