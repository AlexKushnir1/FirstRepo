import org.example.Main;
import org.example.dto.GameStepDTO;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class MainTest {
    GameStepDTO data;
    String[][] gameField;

    @BeforeEach
    void setUp() {
        data = new GameStepDTO(1, 1, "x");
        gameField = new String[3][3];
    }

    @Test
    void testFunction() {
    Main.setStep(data,gameField);
    assertEquals("x", gameField[1][1]);
    }
}
