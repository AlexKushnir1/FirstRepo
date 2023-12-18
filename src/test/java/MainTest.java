import org.example.dto.GameStateDTO;
import org.example.dto.MoveDTO;
import org.example.game.CellValue;
import org.example.game.Game;
import org.example.game.Sign;
import org.example.myExeptions.MyCustomExceptions;
import org.example.requestsAndConnections.GameController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class MainTest {
    MoveDTO data;
    Game game;
    GameController gameController =new GameController();

    @BeforeEach
    void setUp() {
        UUID session = gameController.newSession();
        data = new MoveDTO(1, 1);
        data.setSession_id(session);
        game = gameController.createAndPutNewGameToSessions(session);
    }

    @Test
    void setCleanArrayTest() {
        game.newGameField();
        CellValue[][] expectedGameField = game.newGameField();
        assertTrue(Arrays.deepEquals(game.getGameField(), expectedGameField));
    }

    @Test
    void signChangeTest() {
        game.setNextSign();
        assertEquals(game.getSign(), Sign.Y);
    }

    @Test
    void arrayCleanUpTest() {
        //Add on a place [2][2] something and check it
        game.newGameField();
        assertEquals(game.getGameField()[2][2], CellValue.NULL);
        game.setStep(new MoveDTO(2, 2));
        assertEquals(game.getGameField()[2][2], CellValue.X);
        game.newGameField();
        assertEquals(game.getGameField()[2][2],CellValue.NULL);
    }

    @Test
    void arrayFullnessTest() {
        //feel all cells and check boolean isFull = true;
        game.newGameField();
        assertFalse(game.isFull());
    }

    @Test
    void numberInRangeTest() {
        assertEquals(game.isInRange(2, 2), true);
    }

    @Test
    void testSetStepFunction() {
        game.newGameField();
        game.setStep(data);
        assertEquals(CellValue.X, game.getGameField()[1][1]);
    }

    @Test
    void testSearchingWinner() {
        game.newGameField();
        game.setStep(new MoveDTO(0, 0));
        game.setStep(new MoveDTO(1, 1));
        game.setStep(new MoveDTO(2, 2));
        assertEquals(CellValue.X, game.winner());
    }

    @Test
    void movingTest() throws MyCustomExceptions {
        game.newGameField();
        GameStateDTO gameStateDTO = new GameStateDTO(new CellValue[3][3], null, Sign.X, false);
        GameStateDTO gameStateFromMethodMove = game.move(data);
        assertEquals(gameStateFromMethodMove.getTie(), gameStateDTO.getTie());
        assertEquals(gameStateFromMethodMove.getWinner(),CellValue.NULL);
        assertEquals(gameStateFromMethodMove.getSign(), gameStateDTO.getSign());
        assertEquals(game.getGameField()[1][1], CellValue.X);
    }

    @Test
    void fieldFilling(){
        CellValue[][] field = game.newGameField();
        game.newArray(3);
        assertEquals(game.newArray(3)[1][1],field[1][1]);
    }
}
