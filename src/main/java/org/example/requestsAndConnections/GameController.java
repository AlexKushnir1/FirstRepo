package org.example.requestsAndConnections;

import org.example.dto.GameStateDTO;
import org.example.dto.MoveDTO;
import org.example.game.Game;
import org.example.game.Sessions;
import org.example.myExeptions.MyCustomExceptions;

import java.util.UUID;

public class GameController {
    static Sessions sessions = new Sessions();

    public UUID newSession() {
        UUID sessionId = UUID.randomUUID();
        sessions.addSession(sessionId);
        return sessionId;
    }

    public GameStateDTO newGame(UUID sessionId) throws MyCustomExceptions {
        if (!sessions.containsSession(sessionId)) {
            throw new MyCustomExceptions("Invalid session id");
        }
        return createAndPutNewGameToSessions(sessionId).getGameState();
    }

    public GameStateDTO getLastGameState(UUID sessionId) throws MyCustomExceptions {
        if (!sessions.containsSession(sessionId)) {
            throw new MyCustomExceptions("Invalid session id");
        }
        if (sessions.hasGames(sessionId)) {
            Game game = sessions.getLastAddedGame(sessionId);
            return game.getGameState();
        } else return newGame(sessionId);
    }

    public GameStateDTO move(MoveDTO dataForStep) throws MyCustomExceptions {
        if (sessions.containsSession(dataForStep.getSessionId())) {
            if (!sessions.hasGames(dataForStep.getSessionId())) {
                return createAndPutNewGameToSessions(dataForStep.getSessionId()).move(dataForStep);
            }
        }
        else {
            throw new MyCustomExceptions("Invalid session id");
        }
        Game game = getLastGameBySessionId(dataForStep.getSessionId());
        return game.move(dataForStep);
    }

    public Game createAndPutNewGameToSessions(UUID sessionId) {
        UUID gameId = UUID.randomUUID();
        Game newGame = new Game();
        sessions.addGameToSession(sessionId, gameId, newGame);
        return newGame;
    }

    public static Game getLastGameBySessionId(UUID sessionId) {
        return sessions.getLastAddedGame(sessionId);
    }
}
