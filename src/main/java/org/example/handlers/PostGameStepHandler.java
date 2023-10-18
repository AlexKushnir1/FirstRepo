package org.example.handlers;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.example.Main;
import org.example.dto.GameStepDTO;
import org.example.dto.MessageDTO;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.stream.Stream;

public class PostGameStepHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange httpExchange) {
        if ("POST".equals(httpExchange.getRequestMethod())) {
            // Read the request body as a JSON string

            ObjectMapper objectMapper = new ObjectMapper();
            InputStream requestBody = httpExchange.getRequestBody();
            GameStepDTO gameStepDTO = null;
            try {
                gameStepDTO = objectMapper.readValue(requestBody, GameStepDTO.class);
            } catch (StreamReadException e) {
                sendResponse(httpExchange, "Server stream reading error", 500);
            } catch (DatabindException e) {
                sendResponse(httpExchange, "Bad request", 400);
            } catch (IOException e) {
                sendResponse(httpExchange, "Server error", 500);
            }
//            if (!gameStepDTO.getSign().equals("x")||!gameStepDTO.getSign().equals("y")){
//                sendResponse(httpExchange,"Bad request. Sign can be only x or y",400);
//            }
            if (gameStepDTO.getX()<0&&gameStepDTO.getX()>2&&gameStepDTO.getX()<0&&gameStepDTO.getX()>2){
                sendResponse(httpExchange,"Bad request. Coordinates must be between 0 and 2",400);
            }
            Main.setGameStep(gameStepDTO);
            // Access the parsed data
            int x = gameStepDTO.getX();
            int y = gameStepDTO.getY();
            String sign = gameStepDTO.getSign();

            // Process the data as needed
            String response = "Received POST data: " + x + " " + y + " " + sign;
            sendResponse(httpExchange, response, 200);
        } else {
            sendResponse(httpExchange, "This endpoint only accepts POST requests", 400);
        }

    }

    // Helper method to send a response
    private static void sendResponse(HttpExchange exchange, String response, int status) {
        OutputStream os = exchange.getResponseBody();
        try {
            exchange.sendResponseHeaders(status, response.length());
            os.write(response.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
