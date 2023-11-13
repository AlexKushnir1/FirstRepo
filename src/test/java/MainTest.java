import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.example.Main;
import org.example.dto.GameStepDTO;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MainTest {
    GameStepDTO data;
    String[][] gameField;
    String nextSign;

    @BeforeEach
    void setUp() {
        data = new GameStepDTO(1, 1);
        nextSign = "x";
        gameField = new String[3][3];
        gameField[0][0] = "x";
        gameField[2][2] = "x";
    }
    @Before
    public void setup() {
        RestAssured.baseURI = "http://localhost:8080";
    }

    @Test
    void testMovingFunction() {
        Main.setStep(data, gameField, nextSign);
        assertEquals("x", gameField[1][1]);
    }

    @Test
    void testSearchingWinner(){
        gameField[1][1] = "x";
        assertEquals("x",Main.wins(gameField));
    }

    @Test
    void testWithWrongEquals(){
        assertEquals(gameField[2][0],null);
    }
    @Test
    public void testHttpPostRequestForClearArray() {
        String gameFieldJson = given()
                .contentType(ContentType.JSON)
                .when()
                .post("/new_game")
                .then()
                .statusCode(200)
                .extract()
                .asString();
        assertThat(gameFieldJson, is("[[null,null,null],[null,null,null],[null,null,null]]"));
    }
    @Test
    public void testHttpPostForSendWrongCoordinates() {
        String mustHaveErrorBecauseWrongCoordinates = given()
                .contentType(ContentType.JSON)
                .body("{\"x\":3, \"y\":2}")
                .when()
                .post("/move")
                .then()
                .statusCode(400)
                .extract()
                .asString();
        assertThat(mustHaveErrorBecauseWrongCoordinates, is("Coordinate should be 0, 1, or 2"));
    }
}
