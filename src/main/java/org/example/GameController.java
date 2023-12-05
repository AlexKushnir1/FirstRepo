package org.example;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.dto.GameStepDTO;
import org.example.game.Game;
import org.example.myExeptions.MyCustomExceptions;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static spark.Spark.*;

public class GameController {
    private final ObjectMapper objectMapper = new ObjectMapper();

    private Map<UUID, Map<UUID, Game>> sessions = new HashMap<>();

    public GameController() {
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

        get("/new_session", (req, res) -> {
            UUID sessionId = UUID.randomUUID();
            sessions.put(sessionId, new HashMap<>());
            return sessionId;
        });

        get("/new_game", (req, res) -> {
            if (req.queryParams().isEmpty()) {
                UUID sessionId = UUID.randomUUID();
                sessions.put(sessionId, new HashMap<>());

                UUID gameId = UUID.randomUUID();
                Game newGame = new Game();
                sessions.get(sessionId).put(gameId, newGame);
                return "session_id=" + sessionId + "&game_id=" + gameId;
            } else {
                UUID sessionId = UUID.fromString(req.queryParams("session_id"));

                if (!sessions.containsKey(sessionId)) {
                    res.status(400);
                    return "Invalid session_id";
                }

                UUID gameId = UUID.randomUUID();
                Game newGame = new Game();
                sessions.get(sessionId).put(gameId, newGame);
                return "session_id=" + sessionId + "&game_id=" + gameId;
            }

        });

        get("/get_state", (req, res) -> {
            UUID sessionId = UUID.fromString(req.queryParams("session_id"));
            UUID gameId = UUID.fromString(req.queryParams("game_id"));

            if (!sessions.containsKey(sessionId)) {
                res.status(403);
                return "Invalid session_id";
            }

            if (!sessions.get(sessionId).containsKey(gameId)) {
                res.status(403);
                return "Invalid game_id";
            }

            Game game = sessions.get(sessionId).get(gameId);
            return objectMapper.writeValueAsString(game.getGameState());
        });

        post("/move", (req, res) -> {
            GameStepDTO step;
            try {
                step = objectMapper.readValue(req.body(), GameStepDTO.class);
            } catch (JacksonException e) {
                res.status(400);
                System.out.println(e);
                return "Invalid JSON format";

            }
            UUID sessionId = UUID.fromString(req.queryParams("session_id"));
            UUID gameId = UUID.fromString(req.queryParams("game_id"));

            if (!sessions.containsKey(sessionId)) {
                res.status(403);
                return "Invalid session_id";
            }

            if (!sessions.get(sessionId).containsKey(gameId)) {
                res.status(403);
                return "Invalid game_id";
            }

            Game game = sessions.get(sessionId).get(gameId);
            try {
                res.body(objectMapper.writeValueAsString(game.move(step)));
            } catch (MyCustomExceptions e) {
                res.status(400);
                return e;
            } catch (JacksonException e) {
                res.status(400);
                return "Invalid JSON format" + e.getMessage();
            }
            return res.body();
        });
    }
}
