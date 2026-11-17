package hu.rushhour;

import hu.rushhour.io.BoardIo;
import hu.rushhour.model.Board;
import hu.rushhour.model.Vehicle;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {
    @Test
    void rejectsOverlap() {
        Board b = new Board(4, 8);
        b.add(new Vehicle('A', 1, 0, 2, true));
        assertThrows(IllegalArgumentException.class, () -> b.add(new Vehicle('B', 1, 1, 2, true)));
    }

    @Test
    void movesAndBlocks() {
        Board b = new Board(4, 8);
        b.add(new Vehicle('A', 1, 0, 2, true));
        b.add(new Vehicle('C', 1, 3, 2, false));
        assertTrue(b.canMove('A', 1));
        assertFalse(b.canMove('A', 2));
        b.move('A', 1);
        assertEquals(1, b.get('A').col());
    }

    @Test
    void exitDetection() {
        Board b = new Board(4, 8);
        b.add(new Vehicle('A', 1, 6, 2, true));
        b.add(new Vehicle('B', 2, 0, 2, true));
        assertTrue(b.canExit('A'));
        assertTrue(b.canExit('B'));
    }

    @Test
    void cannotRotateOrLeave() {
        Board b = new Board(4, 8);
        b.add(new Vehicle('C', 0, 0, 2, false));
        assertFalse(b.canMove('C', -1));
        assertFalse(b.canMove('C', 3));
    }

    @Test
    void exitRemovesHero() {
        Board b = new Board(4, 8);
        b.add(new Vehicle('A', 1, 6, 2, true));
        assertTrue(b.exit('A'));
        assertNull(b.get('A'));
        assertFalse(b.exit('A'));
    }

    @Test
    void validatesHeroes() {
        Board b = new Board(4, 8);
        b.add(new Vehicle('A', 1, 0, 2, true));
        assertThrows(IllegalArgumentException.class, b::validateHeroes);
        b.add(new Vehicle('B', 2, 0, 2, true));
        assertDoesNotThrow(b::validateHeroes);
    }

    @Test
    void rejectsZeroMove() {
        Board b = BoardIo.defaults();
        assertFalse(b.canMove('A', 0));
    }

    @Test
    void rejectsExtremeMovementDistances() {
        Board b = BoardIo.defaults();
        assertFalse(b.canMove('A', Integer.MIN_VALUE));
        assertFalse(b.canMove('A', Integer.MAX_VALUE));
    }

    @Test
    void extremeMoveDoesNotChangeTheBoard() {
        Board b = BoardIo.defaults();
        String before = b.render();
        assertThrows(IllegalArgumentException.class,
                () -> b.move('A', Integer.MIN_VALUE));
        assertEquals(before, b.render());
    }

    @Test
    void movesGenerated() {
        Board b = new Board(4, 8);
        b.add(new Vehicle('A', 1, 1, 2, true));
        assertFalse(b.moves().isEmpty());
    }
}
