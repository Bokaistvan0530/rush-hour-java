package hu.rushhour;

import hu.rushhour.io.BoardIo;
import hu.rushhour.service.MoveValidator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MoveValidatorTest {
    @Test
    void rejectsZeroDistance() {
        MoveValidator v = new MoveValidator();
        assertThrows(IllegalArgumentException.class, () -> v.validate(BoardIo.defaults(), 'A', 0));
    }

    @Test
    void acceptsLegalSlide() {
        MoveValidator v = new MoveValidator();
        assertTrue(v.isLegal(BoardIo.defaults(), 'A', -1));
    }
}
