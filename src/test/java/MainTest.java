import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.example.dto.GameStepDTO;
import org.example.game.Game;
import org.example.game.CellValue;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;

public class MainTest {
    GameStepDTO data;
    Game game = new Game();

    @BeforeEach
    void setUp() {
        data = new GameStepDTO(1, 1);
    }

    @Before
    public void setup() {
        RestAssured.baseURI = "http://localhost:8080";
    }

    @Test
    void setCleanArrayTest() {
        game.newGameField();
        String[][] expectedGameField = new String[3][3];
        assertTrue(Arrays.deepEquals(game.getGameField(), expectedGameField));
    }

    @Test
    void signChangeTest() {
        game.setNextSign();
        assertEquals(game.getSign().getTitle().toLowerCase(), "y");
    }

    @Test
    void arrayCleanUpTest() {
        //Add on a place [2][2] something and check it
        game.newGameField();
        game.setStep(new GameStepDTO(2, 2));
        assertEquals(game.getGameField()[2][2], "x");
        game.newGameField();
        assertNull(game.getGameField()[2][2]);
    }

    @Test
    void arrayFullnessTest() {
        //feel all cells and check boolean isFull = true;
        game.newGameField();
        assertEquals(game.isFull(), false);
    }

    @Test
    void numberInRangeTest() {
        assertEquals(game.isInRange(2, 2), true);
    }

    @Test
    void testMovingFunction() {
        game.newGameField();
        game.setStep(data);
        assertEquals("x", game.getGameField()[1][1]);
    }

    @Test
    void testSearchingWinner() {
        game.newGameField();
        game.setStep(new GameStepDTO(0, 0));
        game.setStep(new GameStepDTO(1, 1));
        game.setStep(new GameStepDTO(2, 2));
        assertEquals(CellValue.X, game.winner());
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
                .body("{\"x\":3, \"y\":0}")
                .when()
                .post("/move")
                .then()
                .statusCode(400)
                .extract()
                .asString();
        assertThat(mustHaveErrorBecauseWrongCoordinates, is("org.example.myExeptions.MyCustomExceptions: Coordinates must be within " + game.getFieldSize()));
    }
}
