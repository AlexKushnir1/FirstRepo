package org.example;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.dto.GameStepDTO;
import org.example.game.Game;
import org.example.myExeptions.MyCustomExceptions;

import static spark.Spark.*;
public class Main {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    public static void main(String[] args) {
        Game game = new Game();
        game.newGameField();
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
            response.header("Access-Control-Allow-Origin", "http://localhost:3000"); // Accept request from any port (*).
        });

        post("/move", (request, response) -> {
            try {
                GameStepDTO step = objectMapper.readValue(request.body(), GameStepDTO.class);
                response.body(objectMapper.writeValueAsString(game.move(step)));
            } catch (MyCustomExceptions | JacksonException e) {
                response.status(400);
                return e;
            }
            return response.body();
        });

        post("/new_game", (request, response) ->{
            try {
                response.body(objectMapper.writeValueAsString(game.new_game()));
            } catch (JacksonException e){
                response.status(400);
                return e;
            }
            return response.body();
        });
        get("/get_state",(request, response) ->{
            response.body(objectMapper.writeValueAsString(game.getGameState()));
            return response.body();
        });
    }
}
