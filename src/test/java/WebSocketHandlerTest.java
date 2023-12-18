import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.dto.*;
import org.example.game.CellValue;
import org.example.game.Sign;
import org.example.requestsAndConnections.WebSocketHandler;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.UUID;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;

class WebSocketHandlerTest {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static WebSocketHandler server;
    private static WebSocketClient client;
    private static UUID session;
    private static GameStateDTO gameState;

    @BeforeAll
    static void setUp() throws URISyntaxException {
        server = new WebSocketHandler(new java.net.InetSocketAddress(8887));
        server.start();
        client = new WebSocketClient(new URI("ws://localhost:8887")) {
            @Override
            public void onOpen(ServerHandshake serverHandshake) {
                System.out.println("Client: Connection opened");
            }

            @Override
            public void onMessage(String message) {
                System.out.println("Client: Received message: " + message);
                if (message.startsWith("{\"session")) {
                    try {
                        SessionIdDTO sessionId = objectMapper.readValue(message, SessionIdDTO.class);
                        session = sessionId.getSession_id();
                        System.out.println("Parsed UUID in client: " + session);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    try {
                        gameState = objectMapper.readValue(message, GameStateDTO.class);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            @Override
            public void onClose(int code, String reason, boolean remote) {
                System.out.println("Client: Connection closed");
            }

            @Override
            public void onError(Exception ex) {
                ex.printStackTrace();
            }
        };
        client.connect();
    }

    @AfterAll
    static void tearDown() throws InterruptedException {
        server.stop();

        client.close();
    }

    @Test
    void testWebSocketConnection() throws InterruptedException {
        System.out.println("Test connection");
        Thread.sleep(1000);
        assertEquals(org.java_websocket.enums.ReadyState.OPEN, client.getConnection().getReadyState());
    }

    @Test
    void testWebSocketMessageHandling() throws InterruptedException {
        Thread.sleep(1000);

        System.out.println("Session id before sending getState: "+session);
        GetStateDTO getStateDTO = new GetStateDTO();
        getStateDTO.setSessionId(session);
        System.out.println("Session Id in get state dto: "+getStateDTO.getSessionId());
        try {
            System.out.println("send from client get type: "+objectMapper.writeValueAsString(getStateDTO));
            client.send(objectMapper.writeValueAsString(getStateDTO));
        } catch (JsonProcessingException e) {
            client.send("Catched the error" + e);
        }
        Thread.sleep(1000);
        assertEquals(gameState.getGameField()[1][2], CellValue.NULL);
        assertEquals(gameState.getSign(),Sign.X);
        assertFalse(gameState.getTie());


        System.out.println("Session id before sending move: "+ session);
        MoveDTO move = new MoveDTO(1,2);
        move.setSessionId(session);
        try {
            client.send(objectMapper.writeValueAsString(move));
        } catch (JsonProcessingException e) {
            client.send("Catched the error" + e);
        }
        Thread.sleep(1000);
        assertEquals(gameState.getGameField()[1][2], CellValue.X);
        assertEquals(gameState.getSign(), Sign.X);
        assertFalse(gameState.getTie());

        move.setX(1);
        move.setY(1);
        try {
            client.send(objectMapper.writeValueAsString(move));
        } catch (JsonProcessingException e) {
            client.send("Catched the error" + e);
        }
        Thread.sleep(1000);
        assertEquals(gameState.getGameField()[1][1], CellValue.Y);
        assertEquals(gameState.getSign(), Sign.Y);
        assertFalse(gameState.getTie());

        move.setX(0);
        move.setY(2);
        try {
            client.send(objectMapper.writeValueAsString(move));
        } catch (JsonProcessingException e) {
            client.send("Catched the error" + e);
        }
        Thread.sleep(1000);
        assertEquals(gameState.getGameField()[0][2], CellValue.X);
        assertEquals(gameState.getSign(), Sign.X);
        assertFalse(gameState.getTie());

        move.setX(0);
        move.setY(1);
        try {
            client.send(objectMapper.writeValueAsString(move));
        } catch (JsonProcessingException e) {
            client.send("Catched the error" + e);
        }
        Thread.sleep(1000);
        assertEquals(gameState.getGameField()[0][1], CellValue.Y);
        assertEquals(gameState.getSign(), Sign.Y);
        assertFalse(gameState.getTie());

        move.setX(2);
        move.setY(2);
        try {
            client.send(objectMapper.writeValueAsString(move));
        } catch (JsonProcessingException e) {
            client.send("Catched the error" + e);
        }
        Thread.sleep(1000);
        assertEquals(gameState.getGameField()[2][2], CellValue.X);
        assertEquals(gameState.getWinner(),CellValue.X);
        assertEquals(gameState.getSign(), Sign.X);
        assertFalse(gameState.getTie());

        System.out.println("Session id before sending newGame: "+session);
        NewGameDTO newGameDTO = new NewGameDTO();
        newGameDTO.setSessionId(session);
        try {
            client.send(objectMapper.writeValueAsString(newGameDTO));
        } catch (JsonProcessingException e) {
            client.send("Catched the error" + e);
        }
        Thread.sleep(1000);

        assertEquals(gameState.getGameField()[1][2], CellValue.NULL);
    }
}
