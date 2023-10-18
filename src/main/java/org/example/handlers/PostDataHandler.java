package org.example.handlers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.example.Main;

import java.io.IOException;
import java.io.OutputStream;

public class PostDataHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange httpExchange) {
        if ("GET".equals(httpExchange.getRequestMethod())) {
            if ("/post_data".equals(httpExchange.getRequestURI().getPath())){

            // Process the data as needed
            ObjectMapper objectMapper = new ObjectMapper();
            String responseJson = "";
            try {
                responseJson = objectMapper.writeValueAsString(Main.getGameField());
            } catch (JsonProcessingException e) {
                sendResponse(httpExchange, "Json parsing error", 400);
            }
            sendResponse(httpExchange, responseJson, 200);
            }else {
                sendResponse(httpExchange, "Bad request", 400);
            }
        } else {
            sendResponse(httpExchange, "This endpoint only accepts GET requests", 400);
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
