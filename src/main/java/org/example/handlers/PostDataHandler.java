package org.example.handlers;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.example.dto.MessageDTO;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class PostDataHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange httpExchange) {
        if ("POST".equals(httpExchange.getRequestMethod())) {
            // Read the request body as a JSON string

            ObjectMapper objectMapper = new ObjectMapper();
            InputStream requestBody = httpExchange.getRequestBody();
            MessageDTO postData = null;
            try {
                postData = objectMapper.readValue(requestBody, MessageDTO.class);
            } catch (StreamReadException e) {
                sendResponse(httpExchange, "Server stream reading error", 500);
            } catch (DatabindException e) {
                sendResponse(httpExchange, "Bad request", 400);
            } catch (IOException e) {
                sendResponse(httpExchange, "Server error", 500);
            }
            // Access the parsed data
            String message = postData.getMessage();

            // Process the data as needed
            String response = "Received POST data: " + message;
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
