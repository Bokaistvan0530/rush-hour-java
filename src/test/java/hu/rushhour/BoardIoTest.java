package hu.rushhour;

import hu.rushhour.io.BoardIo;
import hu.rushhour.model.Board;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BoardIoTest {
    @TempDir
    Path tmp;

    @Test
    void roundtrip() throws Exception {
        Board b = BoardIo.defaults();
        Path p = tmp.resolve("board.txt");
        BoardIo.save(b, p);
        Board loaded = BoardIo.load(p);
        assertEquals(b.render(), loaded.render());
    }

    @Test
    void rejectsMissingHeroes() throws Exception {
        Path p = tmp.resolve("invalid.txt");
        Files.writeString(p, "4 8\nC 1 1 2 H\n");
        assertThrows(java.io.IOException.class, () -> BoardIo.load(p));
    }
}
