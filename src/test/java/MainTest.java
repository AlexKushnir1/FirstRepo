import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.example.Main;
import org.example.dto.GameStepDTO;
import org.example.game.GameLogic;
import org.example.game.Sign;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MainTest {
    GameStepDTO data;
    static String[][] gameField;
    Sign nextSign;

    @BeforeEach
    void setUp() {
        data = new GameStepDTO(1, 1);
        nextSign = Sign.X;
    }

    @Before
    public void setup() {
        RestAssured.baseURI = "http://localhost:8080";
    }

    @Test
    void setCleanArrayTest() {
        int arraySize = 3;
        gameField = GameLogic.setCleanArray(arraySize);
        assertEquals(gameField.equals(new String[3][3]), true);
    }

    @Test
    void signChangeTest() {
        nextSign = nextSign.getNextSign();
        assertEquals(nextSign.getTitle().toLowerCase(), "y");
    }

    @Test
    void arrayCleanUpTest() {
        //Додати на місце [2][2] щось і перевірити чи він там є
        gameField = GameLogic.setCleanArray(3);
        assertEquals(gameField[2][2], null);
    }

    @Test
    void arrayFullnessTest() {
        //заповнити всі клітинки перевірити boolean isFull = true;
        assertEquals(GameLogic.isFull(gameField), false);
    }

    @Test
    void numberWithinAnArrayTest() {
        assertEquals(GameLogic.isNumbWithinAnArray(3, 2, 3), true);
    }

    @Test
    void testMovingFunction() {
        GameLogic.setStep(data, gameField, nextSign);
        assertEquals("x", gameField[1][1]);
    }

    @Test
    void testSearchingWinner() {
        gameField[1][1] = "x";
        assertEquals(Sign.X, GameLogic.wins(gameField));
    }

    @Test
    void testWithWrongEquals() {
        assertEquals(gameField[2][0], null);
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
        assertThat(mustHaveErrorBecauseWrongCoordinates, is("Coordinates must be within " + Main.getArraySize()));
    }
}
