package hu.rushhour.app;

import hu.rushhour.db.ScoreRepository;
import hu.rushhour.game.Game;
import hu.rushhour.io.BoardIo;
import hu.rushhour.model.Board;
import java.nio.file.Path;
import java.util.Random;
import java.util.Scanner;

/** A Rush Hour jatek inditasaert felelos osztaly. */
public final class Main {

  private Main() {
  }

  /**
   * Elinditja a jatekot, es betolti a megadott palyat.
   *
   * @param args parancssori argumentumok
   */
  public static void main(String[] args) {
    Board board = BoardIo.defaults();
    if (args.length > 0) {
      try {
        board = BoardIo.load(Path.of(args[0]));
      } catch (Exception e) {
        System.out.println("Using default board: " + e.getMessage());
      }
    }
    ScoreRepository scores = new ScoreRepository("./rushhour-scores");
    Game game = new Game(board, new Scanner(System.in), new Random(), scores);
    game.start();
  }
}
