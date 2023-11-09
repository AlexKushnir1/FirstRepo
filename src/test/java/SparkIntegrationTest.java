import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.example.GameResult;
import org.example.dto.GameStepDTO;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class SparkIntegrationTest {
    @Before
    public void setup() {
        RestAssured.baseURI = "http://localhost:8080";
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