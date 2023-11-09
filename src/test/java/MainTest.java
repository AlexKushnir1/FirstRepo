import org.example.Main;
import org.example.dto.GameStepDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
}
