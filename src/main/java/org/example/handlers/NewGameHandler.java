package org.example.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;

public class NewGameHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        // Get input stream for response

        OutputStream os = exchange.getResponseBody();
        String response;
        if ("/new_game".equals(exchange.getRequestURI().getPath())){

        // Set HTTP-status : "200 OK"
        exchange.sendResponseHeaders(200, 0);

        // Send response
        response = "New game created/ for moving send Post request with coordinates";

        // Finish sending response

        }else{
            exchange.sendResponseHeaders(400,0);
            response = "Bad request";
        }
        os.write(response.getBytes());
        os.close();

    }
}
