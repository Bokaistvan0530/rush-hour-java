package hu.rushhour;

import hu.rushhour.ui.CommandParser;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CommandParserTest {
    @Test
    void splitsCommand() {
        var c = new CommandParser().parse("MOVE A 2");
        assertEquals("move", c.name());
        assertArrayEquals(new String[]{"A", "2"}, c.args());
    }

    @Test
    void rejectsBlank() {
        assertThrows(IllegalArgumentException.class, () -> new CommandParser().parse(" "));
    }
}
