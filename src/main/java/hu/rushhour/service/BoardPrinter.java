package hu.rushhour.service;

import hu.rushhour.model.Board;

/** ASCII presentation of the board. */
public final class BoardPrinter {

  /**
   * Renders the board.
   *
   * @param board game board
   * @return board as text
   */
  public String print(Board board) {
    return board.render();
  }
}
