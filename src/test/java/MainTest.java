import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.example.Main;
import org.example.dto.GameStepDTO;
import org.example.game.Game;
import org.example.game.Sign;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;

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
        game.setCleanArray();
        String[][] expectedGameField = new String[3][3];
        assertEquals(Arrays.deepEquals(game.getGameField(), expectedGameField), true);
    }

    @Test
    void signChangeTest() {
        game.setNextSign();
        assertEquals(game.getSign().getTitle().toLowerCase(), "y");
    }

    @Test
    void arrayCleanUpTest() {
        //Додати на місце [2][2] щось і перевірити чи він там є
        game.setCleanArray();
        game.setStep(new GameStepDTO(2, 2));
        assertEquals(game.getGameField()[2][2], "x");
        game.setCleanArray();
        assertEquals(game.getGameField()[2][2], null);
    }

    @Test
    void arrayFullnessTest() {
        //заповнити всі клітинки перевірити boolean isFull = true;
        game.setCleanArray();
        assertEquals(game.isFull(), false);
    }

    @Test
    void numberWithinAnArrayTest() {
        assertEquals(game.isNumbWithinAnArray(2, 2), true);
    }

    @Test
    void testMovingFunction() {
        game.setCleanArray();
        game.setStep(data);
        assertEquals("x", game.getGameField()[1][1]);
    }

    @Test
    void testSearchingWinner() {
        game.setCleanArray();
        game.setStep(new GameStepDTO(0, 0));
        game.setStep(new GameStepDTO(1, 1));
        game.setStep(new GameStepDTO(2, 2));
        assertEquals(Sign.X, game.wins());
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
        assertThat(mustHaveErrorBecauseWrongCoordinates, is("Coordinates must be within " + game.getFieldSize()));
    }
}
