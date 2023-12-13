import org.example.dto.GameStateDTO;
import org.example.dto.StepForMoveDTO;
import org.example.game.CellValue;
import org.example.game.Game;
import org.example.myExeptions.MyCustomExceptions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class MainTest {
    StepForMoveDTO data;
    Game game = new Game();

    @BeforeEach
    void setUp() {
        data = new StepForMoveDTO(1, 1);
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
        game.setStep(new StepForMoveDTO(2, 2));
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
    void testSetStepFunction() {
        game.newGameField();
        game.setStep(data);
        assertEquals("x", game.getGameField()[1][1]);
    }

    @Test
    void testSearchingWinner() {
        game.newGameField();
        game.setStep(new StepForMoveDTO(0, 0));
        game.setStep(new StepForMoveDTO(1, 1));
        game.setStep(new StepForMoveDTO(2, 2));
        assertEquals(CellValue.X, game.winner());
    }

    @Test
    void movingTest() throws MyCustomExceptions {
        game.newGameField();
        GameStateDTO gameStateDTO = new GameStateDTO(new String[3][3], null, "x", false);
        GameStateDTO gameStateFromMethodMove = game.move(data);
        assertEquals(gameStateFromMethodMove.getTie(), gameStateDTO.getTie());
        assertEquals(gameStateFromMethodMove.getWinner(), null);
        assertTrue(gameStateFromMethodMove.getSign().equals(gameStateDTO.getSign()));
        assertEquals(game.getGameField()[1][1], "x");
    }


}
