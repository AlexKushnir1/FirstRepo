package org.example;

import org.example.requestsAndConnections.WebSocketHandler;

import java.net.InetSocketAddress;

import static spark.Spark.*;

public class Main {
    public static void main(String[] args) {
        final int portForSocket = 4567;
        WebSocketHandler socket = new WebSocketHandler(new InetSocketAddress(portForSocket));
        socket.start();
    }
}
