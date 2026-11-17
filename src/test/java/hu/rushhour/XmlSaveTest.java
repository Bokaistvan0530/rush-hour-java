package hu.rushhour;

import hu.rushhour.io.BoardIo;
import hu.rushhour.io.XmlSave;
import hu.rushhour.model.Board;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class XmlSaveTest {
    @TempDir
    Path dir;

    @Test
    void savesAndRestoresPlayerTurnAndBoard() throws Exception {
        Board b = BoardIo.defaults();
        Path f = dir.resolve("save.xml");
        XmlSave.save(f, new XmlSave.Snapshot(b, false, "Alice"));
        XmlSave.Snapshot restored = XmlSave.load(f);
        assertEquals(b.render(), restored.board().render());
        assertFalse(restored.humanTurn());
        assertEquals("Alice", restored.player());
    }

    @Test
    void rejectsUntrustedDoctype() throws Exception {
        Path f = dir.resolve("bad.xml");
        Files.writeString(f, "<!DOCTYPE x [<!ENTITY e SYSTEM 'file:///etc/passwd'>]><rushhour/>");
        assertThrows(Exception.class, () -> XmlSave.load(f));
    }
}
