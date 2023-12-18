package org.example.requestsAndConnections;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.dto.*;
import org.example.myExeptions.MyCustomExceptions;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.UUID;


public class WebSocketHandler extends WebSocketServer {

    private final ObjectMapper objectMapper = new ObjectMapper();
    MessageDeserializer messageDeserializer = new MessageDeserializer();
    GameController gameController = new GameController();

    public WebSocketHandler(InetSocketAddress address) {
        super(address);
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        System.out.println("Connection opened: " + conn);
        SessionIdDTO sessionId = new SessionIdDTO();
        sessionId.setSession_id(gameController.newSession());
        System.out.println("Session id that will be sended" + sessionId.getSession_id());
        try {
            System.out.println(objectMapper.writeValueAsString(sessionId));
            conn.send(objectMapper.writeValueAsString(sessionId));

        } catch (JsonProcessingException e) {
            conn.send("Json parsing error");
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        System.out.println("Connection closed: " + conn.getRemoteSocketAddress());
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        try {
            JsonParser jsonParser = new ObjectMapper().getFactory().createParser(message);
            DeserializationContext deserializationContext = new ObjectMapper().getDeserializationContext();
            AbstractDataDTO abstractMessageDTO = messageDeserializer.deserialize(jsonParser, deserializationContext);
            System.out.println(abstractMessageDTO.toString());
            System.out.println("Server receive message :" +message);
            if (abstractMessageDTO instanceof MoveDTO) {
                MoveDTO moveDTO = (MoveDTO) abstractMessageDTO;
                System.out.println("Received moveDTO: " + moveDTO);
                ;
                conn.send(objectMapper.writeValueAsString(gameController.move(moveDTO)));
            } else if (abstractMessageDTO instanceof GetStateDTO) {
                GetStateDTO getStateDTO = (GetStateDTO) abstractMessageDTO;
                System.out.println("Received getStateDTO: " + getStateDTO);
                conn.send(objectMapper.writeValueAsString(gameController.getState(getStateDTO.getSession_id())));
            } else if (abstractMessageDTO instanceof NewGameDTO) {
                NewGameDTO newGameDTO = (NewGameDTO) abstractMessageDTO;
                System.out.println("Received NewGameDTO: " + newGameDTO);
                gameController.newGame(newGameDTO.getSession_id());
                conn.send(objectMapper.writeValueAsString(gameController.newGame(newGameDTO.getSession_id())));
            } else {
                conn.send("Invalid message");
            }
        } catch (IOException | MyCustomExceptions e) {
            conn.send(String.valueOf(e));
        }
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        System.err.println("Error occurred: ");
        ex.printStackTrace();
    }

    @Override
    public void onStart() {
        System.out.println("New connection on Start");
    }

}
