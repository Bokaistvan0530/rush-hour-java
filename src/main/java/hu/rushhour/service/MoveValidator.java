package hu.rushhour.service;

import hu.rushhour.model.Board;

/** Keeps move validation separate from the terminal interface. */
public final class MoveValidator {

  /**
   * Tests whether a move is legal.
   *
   * @param board game board
   * @param id vehicle id
   * @param delta movement
   * @return true for a legal move
   */
  public boolean isLegal(Board board, char id, int delta) {
    return board != null && board.canMove(id, delta);
  }

  /**
   * Validates a move.
   *
   * @param board game board
   * @param id vehicle id
   * @param delta movement
   */
  public void validate(Board board, char id, int delta) {
    if (!isLegal(board, id, delta)) {
      throw new IllegalArgumentException("Illegal slide: " + id + " " + delta);
    }
  }
}
