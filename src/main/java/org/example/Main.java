package org.example;

import com.sun.net.httpserver.HttpServer;
import org.example.dto.GameStepDTO;
import org.example.handlers.NewGameHandler;
import org.example.handlers.PostDataHandler;
import org.example.handlers.PostGameStepHandler;

import java.io.IOException;
import java.net.InetSocketAddress;

public class Main {
    private static String[][] gameField = new String[3][3];

    public static void main(String[] args) throws IOException {
        int port = 8080; // Port to listen on

        // Create an HTTP server that listens on the specified port
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);

        // Context with response
        server.createContext("/new_game", new NewGameHandler());
        server.createContext("/post_data", new PostDataHandler());
        server.createContext("/game-state", new PostGameStepHandler());

        // Start the server
        server.setExecutor(null); // Use the default executor
        server.start();

        System.out.println("Server is running on port " + port);

    }

    public static void setGameStep(GameStepDTO data) {
        int x = data.getX();
        int y = data.getY();
        String sign = data.getSign();

        if (gameField[x][y] == null) {
            gameField[x][y] = sign;
        }
        for (int i = 0; i <= 2; i++) {
            for (int j = 0; j <= 2; j++) {
                System.out.print("   " + gameField[i][j]);
            }
            System.out.println(" ");
        }
    }

    public static String[][] getGameField() {
        return gameField;
    }
}