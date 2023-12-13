package org.example.game;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

public class Sessions {
    private Map<UUID, Map<UUID, Game>> sessions = new HashMap<>();

    public void addGameToSession(UUID sessionId, UUID gameId, Game game) {
        // Get or create map for session
        sessions.computeIfAbsent(sessionId, k -> new LinkedHashMap<>());

        // Add game to session map
        sessions.get(sessionId).put(gameId, game);
    }

    public Game getLastAddedGame(UUID sessionId) {
        // Get map for session
        Map<UUID, Game> sessionMap = sessions.get(sessionId);

        if (sessionMap != null && !sessionMap.isEmpty()) {
            // Get the last added game (Java 8+)
            return sessionMap.values().stream()
                    .reduce((first, second) -> second)
                    .orElse(null);
        } else {
            return null; // If map empty or sessionId incorrect
        }
    }

    public boolean containsSession(UUID sessionId) {
        return sessions.containsKey(sessionId);
    }

    public boolean containsGameId(UUID sessionId, UUID gameId) {
        Map<UUID, Game> sessionMap = sessions.get(sessionId);
        return sessionMap != null && sessionMap.containsKey(gameId);
    }
    public void addSession(UUID sessionId) {
        // Add session with an empty map
        sessions.put(sessionId, new LinkedHashMap<>());
    }
    public Game getGameById(UUID sessionId, UUID gameId) {
        Map<UUID, Game> sessionMap = sessions.get(sessionId);
        return sessionMap != null ? sessionMap.get(gameId) : null;
    }
    public boolean hasGames(UUID sessionId) {
        Map<UUID, Game> sessionMap = sessions.get(sessionId);
        return sessionMap != null && !sessionMap.isEmpty();
    }
}
