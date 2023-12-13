package org.example;

import org.example.requestsAndConnections.WebSocketHandler;

import java.net.InetSocketAddress;

import static spark.Spark.*;

public class Main {
    public static void main(String[] args) {
        int portForSocket = 4567;
//        port(8080);
        WebSocketHandler socket = new WebSocketHandler(new InetSocketAddress(portForSocket));
        socket.start();
//        options("/*", (request, response) -> {
//            String accessControlRequestHeaders = request.headers("Access-Control-Request-Headers");
//            if (accessControlRequestHeaders != null) {
//                response.header("Access-Control-Allow-Headers", accessControlRequestHeaders);
//            }
//            String accessControlRequestMethod = request.headers("Access-Control-Request-Method");
//            if (accessControlRequestMethod != null) {
//                response.header("Access-Control-Allow-Methods", accessControlRequestMethod);
//            }
//            return "OK";
//        });
//
//        before((request, response) -> {
//            response.header("Access-Control-Allow-Origin", "*"); // Accept request from any port (*).
//        });
    }
}
