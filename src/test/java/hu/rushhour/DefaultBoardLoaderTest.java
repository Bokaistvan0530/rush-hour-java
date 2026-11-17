package hu.rushhour;

import hu.rushhour.io.BoardIo;
import hu.rushhour.io.DefaultBoardLoader;
import hu.rushhour.model.Board;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import java.nio.file.Files;
import java.nio.file.Path;
import static org.junit.jupiter.api.Assertions.assertEquals;

class DefaultBoardLoaderTest {

  @TempDir
  Path tempDir;

  @Test
  void loadsDefaultWhenPathIsNull() {
    Board loaded = new DefaultBoardLoader().loadOrDefault(null);
    assertEquals(BoardIo.defaults().render(), loaded.render());
  }

  @Test
  void loadsRequestedBoardFromFile() throws Exception {
    Path file = tempDir.resolve("board.txt");
    Board original = BoardIo.defaults();
    BoardIo.save(original, file);
    Board loaded = new DefaultBoardLoader().loadOrDefault(file);
    assertEquals(original.render(), loaded.render());
  }

  @Test
  void usesDefaultWhenFileIsMissing() {
    Path missing = tempDir.resolve("missing.txt");
    Board loaded = new DefaultBoardLoader().loadOrDefault(missing);
    assertEquals(BoardIo.defaults().render(), loaded.render());
  }
}
