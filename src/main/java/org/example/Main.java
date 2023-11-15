package org.example;

import com.fasterxml.jackson.core.JacksonException;
import org.example.game.Game;
import org.example.myExeptions.MyCustomExceptions;

import static spark.Spark.*;

public class Main {
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
            response.header("Access-Control-Allow-Origin", "*"); // Accept request from any port (*).
        });

        post("/move", (request, response) -> {
            try {
                response.body(game.move(request));
            } catch (MyCustomExceptions | JacksonException e) {
                response.status(400);
                return e;
            }
            return response.body();
        });

        post("/new_game", (request, response) ->
        {
            response.body(game.new_game());
            return response.body();
        });
    }
}
