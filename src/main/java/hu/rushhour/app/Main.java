package hu.rushhour.app;

import hu.rushhour.db.ScoreRepository;
import hu.rushhour.game.Game;
import hu.rushhour.io.BoardIo;
import hu.rushhour.io.DefaultBoardLoader;
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
    Board board;
    try {
      Path path = args.length > 0 ? Path.of(args[0]) : null;
      board = new DefaultBoardLoader().loadOrDefault(path);
    } catch (Exception e) {
      // Hibás elérési út esetén is a beépített pálya indul.
      System.out.println("Using default board: " + e.getMessage());
      board = BoardIo.defaults();
    }
    ScoreRepository scores = new ScoreRepository("./rushhour-scores");
    Game game = new Game(board, new Scanner(System.in), new Random(), scores);
    game.start();
  }
}
