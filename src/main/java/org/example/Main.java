package org.example;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import org.example.handlers.PostDataHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

public class Main {
    public static void main(String[] args) throws IOException {
        int port = 8080; // Port to listen on

        // Create an HTTP server that listens on the specified port
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);

        // Create a context for a simple "Hello, World!" response
        server.createContext("/", new HelloWorldHandler());
        server.createContext("/postdata", new PostDataHandler());
        // Start the server
        server.setExecutor(null); // Use the default executor
        server.start();

        System.out.println("Server is running on port " + port);
    }
}

class HelloWorldHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        // Встановлюємо HTTP-заголовок "Content-Type" на "text/plain"
        Headers headers = exchange.getResponseHeaders();
        headers.set("Content-Type", "text/plain");

        // Встановлюємо HTTP-статус на "200 OK"
        exchange.sendResponseHeaders(200, 0);

        // Отримуємо вихідний потік для відповіді
        OutputStream os = exchange.getResponseBody();

        // Відправляємо відповідь
        String response = "Hello, World!";
        os.write(response.getBytes());

        // Завершуємо відправку відповіді
        os.close();
    }
}