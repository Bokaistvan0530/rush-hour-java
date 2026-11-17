package hu.rushhour;

import hu.rushhour.io.BoardIo;
import hu.rushhour.model.Board;
import hu.rushhour.service.BoardPrinter;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class BoardPrinterTest {

  @Test
  void rendersBoardWithoutChangingIt() {
    Board board = BoardIo.defaults();
    assertEquals(board.render(), new BoardPrinter().print(board));
  }
}
