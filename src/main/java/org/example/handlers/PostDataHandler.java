package org.example.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.example.dto.MessageDTO;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class PostDataHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        if ("POST".equals(httpExchange.getRequestMethod())) {
            // Read the request body as a JSON string
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                InputStream requestBody = httpExchange.getRequestBody();
                MessageDTO postData = objectMapper.readValue(requestBody, MessageDTO.class);

                // Access the parsed data
                String message = postData.getMessage();

                // Process the data as needed
                String response = "Received POST data: " + message;
                sendResponse(httpExchange, response);
            } catch (IOException e) {
                // Handle JSON parsing errors
                sendResponse(httpExchange, "Error parsing JSON data. Error:" + e);
            }
        } else {
            sendResponse(httpExchange, "This endpoint only accepts POST requests.");
        }

    }

    // Helper method to send a response
    private static void sendResponse(HttpExchange exchange, String response) throws IOException {
        exchange.sendResponseHeaders(200, response.length());
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response.getBytes());
        }
    }
}
