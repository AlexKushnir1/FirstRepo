package org.example;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.dto.GameStepDTO;

import static spark.Spark.port;
import static spark.Spark.post;


public class Main {
    private static String[][] gameField = new String[3][3];

    public static void main(String[] args) {
        port(8080);
        ObjectMapper objectMapper = new ObjectMapper();
        post("/move", (request, response) -> {
            try {
                //gameStep -крок гри
                GameStepDTO step = objectMapper.readValue(request.body(), GameStepDTO.class);
                if (gameField[step.getX()][step.getY()] == null) {
                    setStep(step);
                    return "Received and processed the request.";
                } else {
                    return "Cell " + step.getX() + " : " + step.getY() + " is not empty";
                }
            } catch (JsonMappingException e) {
                System.out.println(e);
                response.status(400);
                return "Bad request. Json can`t map data";
            } catch (JsonProcessingException e) {
                System.out.println(e);
                response.status(400);
                return "Bad request. Json can`t process";
            }
        });
        post("/get", (request, response) -> {
            try {
                //Json that send to client
                response.body(objectMapper.writeValueAsString(gameField));
                return response.body();
            } catch (JsonProcessingException e) {
                response.status(400);
                return "Bad request. Can`t write data to Json";
            }
        });
        post("/new_game", (request, response) -> {
            response.body("New game created/ for moving send Post request with coordinates");
            gameField = new String[3][3];
            return response.body();
        });
    }

    public static void setStep(GameStepDTO data) {
        int x = data.getX();
        int y = data.getY();
        String sign = data.getSign();

        gameField[x][y] = sign;

        for (int i = 0; i <= 2; i++) {
            for (int j = 0; j <= 2; j++) {
                System.out.print("   " + gameField[i][j]);
            }
            System.out.println(" ");
        }
    }
}