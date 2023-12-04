import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.example.game.Game;
import org.junit.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;

public class IntegrationTestIT {
    UUID sessionId;
    UUID gameId;

    @Before
    public void setup() {
        RestAssured.baseURI = "http://localhost:8080";
    }

    @BeforeEach
    public void setSessionId() {
        String returnedDataFromServer = given()
                .contentType(ContentType.JSON)
                .when()
                .get("/new_game")
                .then()
                .statusCode(200)
                .extract()
                .asString();
        try {
            // Split the input string by "&"
            String[] keyValuePairs = returnedDataFromServer.split("&");

            // Initialize variables
            UUID sessionIdFromServer = null;
            UUID gameIdFromServer = null;

            // Iterate over key-value pairs
            for (String pair : keyValuePairs) {
                // Split each pair by "="
                String[] entry = pair.split("=");

                // Check for "session_id" and "game_id" keys
                if (entry.length == 2) {
                    String key = entry[0];
                    String value = URLDecoder.decode(entry[1], StandardCharsets.UTF_8.toString());

                    if ("session_id".equals(key)) {
                        sessionIdFromServer = UUID.fromString(value);
                    } else if ("game_id".equals(key)) {
                        gameIdFromServer = UUID.fromString(value);
                    }
                }
            }
            this.sessionId = sessionIdFromServer;
            this.gameId = gameIdFromServer;

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getRequestForNewGameWithoutSessionId() {
        String returnedData = given()
                .contentType(ContentType.JSON)
                .when()
                .get("/new_game")
                .then()
                .statusCode(200)
                .extract()
                .asString();
        assertNotNull(returnedData);
        assertTrue(!returnedData.isEmpty());
        parsingKeys(returnedData);
        assertNotEquals(IDs.get("session_id"), sessionId);
        assertNotEquals(IDs.get("game_id"), gameId);
    }

    @Test
    public void getRequestForNewGameWithSessionId() {
        String returnedData = given()
                .contentType(ContentType.JSON)
                .queryParam("session_id", sessionId)
                .when()
                .get("/new_game")
                .then()
                .statusCode(200)
                .extract()
                .asString();
        assertNotNull(returnedData);
        assertTrue(!returnedData.isEmpty());
        parsingKeys(returnedData);
        assertEquals(IDs.get("session_id"), sessionId);
        assertNotEquals(IDs.get("game_id"), gameId);
    }

    @Test
    public void postMoveForSendWrongCoordinates() {
        Game game = new Game();
        String mustHaveErrorBecauseWrongCoordinates = given()
                .contentType(ContentType.JSON)
                .body("{\"x\":3, \"y\":0}")
                .queryParam("session_id", sessionId)
                .queryParam("game_id", gameId)
                .when()
                .post("/move")
                .then()
                .statusCode(400)
                .extract()
                .asString();

        assertThat(mustHaveErrorBecauseWrongCoordinates, is("org.example.myExeptions.MyCustomExceptions: Coordinates must be within " + game.getFieldSize()));
    }

    @Test
    public void postMoveSendInvalidJson() {
        String mustHaveErrorBecauseWrongCoordinates = given()
                .contentType(ContentType.JSON)
                .body("{\"x\":\"A\", \"y\":0}")
                .queryParam("session_id", sessionId)
                .queryParam("game_id", gameId)
                .when()
                .post("/move")
                .then()
                .statusCode(400)
                .extract()
                .asString();
        assertEquals(mustHaveErrorBecauseWrongCoordinates, "Invalid JSON format");
    }

    @Test
    public void postMoveSendInvalidSessionId() {
        String mustHaveErrorBecauseWrongCoordinates = given()
                .contentType(ContentType.JSON)
                .body("{\"x\":1, \"y\":0}")
                .queryParam("session_id", "f3805fc3-a039-4d9d-bce4-0fe40736283c")
                .queryParam("game_id", gameId)
                .when()
                .post("/move")
                .then()
                .statusCode(403)
                .extract()
                .asString();
        assertEquals(mustHaveErrorBecauseWrongCoordinates, "Invalid session_id");
    }

    @Test
    public void postMoveSendInvalidGameId() {
        String mustHaveErrorBecauseWrongCoordinates = given()
                .contentType(ContentType.JSON)
                .body("{\"x\":1, \"y\":0}")
                .queryParam("session_id", sessionId)
                .queryParam("game_id", "f3805fc3-a039-4d9d-bce4-0fe40736283c")
                .when()
                .post("/move")
                .then()
                .statusCode(403)
                .extract()
                .asString();
        assertEquals(mustHaveErrorBecauseWrongCoordinates, "Invalid game_id");
    }

    @Test
    public void postMoveCheckForOverFlowCell() {
         given()
                .contentType(ContentType.JSON)
                .body("{\"x\":1, \"y\":0}")
                .queryParam("session_id", sessionId)
                .queryParam("game_id", gameId)
                .when()
                .post("/move")
                .then()
                .statusCode(200)
                .extract()
                .asString();
        String mustHaveErrorBecauseWrongCoordinates = given().contentType(ContentType.JSON)
                .body("{\"x\":1, \"y\":0}")
                .queryParam("session_id", sessionId)
                .queryParam("game_id", gameId)
                .when()
                .post("/move")
                .then()
                .statusCode(400)
                .extract()
                .asString();
        assertEquals(mustHaveErrorBecauseWrongCoordinates, "org.example.myExeptions.MyCustomExceptions: Cell " + 1 + " : " + 0 + " is not empty");
    }


    public Map<String, UUID> IDs = new HashMap<>();

    public void parsingKeys(String s) {
        try {
            // Split the input string by "&"
            String[] keyValuePairs = s.split("&");

            // Initialize variables
            UUID sessionId = null;
            UUID gameId = null;

            // Iterate over key-value pairs
            for (String pair : keyValuePairs) {
                // Split each pair by "="
                String[] entry = pair.split("=");

                // Check for "session_id" and "game_id" keys
                if (entry.length == 2) {
                    String key = entry[0];
                    String value = URLDecoder.decode(entry[1], StandardCharsets.UTF_8.toString());

                    if ("session_id".equals(key)) {
                        sessionId = UUID.fromString(value);
                    } else if ("game_id".equals(key)) {
                        gameId = UUID.fromString(value);
                    }
                }
            }

            IDs.put("session_id", sessionId);
            IDs.put("game_id", gameId);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
