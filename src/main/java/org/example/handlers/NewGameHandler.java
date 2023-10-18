package org.example.handlers;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;

public class NewGameHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        // Set HTTP-status : "200 OK"
        exchange.sendResponseHeaders(200, 0);

        // Get input stream for response
        OutputStream os = exchange.getResponseBody();

        // Send response
        String response = "New game created/ for moving send Post request with coordinates";
        os.write(response.getBytes());

        // Finish sending response
        os.close();
    }
}
