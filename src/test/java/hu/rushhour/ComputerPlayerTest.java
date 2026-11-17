package hu.rushhour;

import hu.rushhour.game.ComputerPlayer;
import hu.rushhour.io.BoardIo;
import hu.rushhour.model.Board;
import hu.rushhour.model.Vehicle;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ComputerPlayerTest {
    @Test
    void alwaysChoosesLegalMove() {
        Board b = BoardIo.defaults();
        ComputerPlayer ai = new ComputerPlayer(new Random(5));
        for (int i = 0; i < 100; i++) {
            Board.Move m = ai.choose(b).orElseThrow();
            assertTrue(b.canMove(m.id(), m.delta()));
        }
    }

    @Test
    void noMoveReturnsEmpty() {
        Board b = new Board(2, 4);
        b.add(new Vehicle('C', 0, 0, 4, true));
        b.add(new Vehicle('D', 1, 0, 4, true));
        assertTrue(new ComputerPlayer(new Random(1)).choose(b).isEmpty());
    }
}
