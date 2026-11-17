package hu.rushhour.game;

import hu.rushhour.db.ScoreRepository;
import hu.rushhour.event.GameEvent;
import hu.rushhour.event.GameEventBus;
import hu.rushhour.io.BoardIo;
import hu.rushhour.io.XmlSave;
import hu.rushhour.model.Board;
import hu.rushhour.service.MoveValidator;
import hu.rushhour.ui.ConsoleMenu;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.Locale;
import java.util.Optional;
import java.util.Random;
import java.util.Scanner;

/**
* Console controller: human turn, computer turn, exit, save and resume.
*/
public final class Game {
  private final ConsoleMenu menu = new ConsoleMenu();
  private final MoveValidator validator = new MoveValidator();
  private final GameEventBus events = new GameEventBus();
  private Board board;
  private final Scanner input;
  private final ComputerPlayer ai;
  private final ScoreRepository scores;
  private String player = "Player";
  private boolean humanTurn = true;

  /**
  * Creates the game controller.
  *
  * @param board initial board
  * @param input user input
  * @param random random generator
  * @param scores score repository
  */
  public Game(Board board, Scanner input, Random random, ScoreRepository scores) {
    this.board = board;
    this.input = input;
    this.ai = new ComputerPlayer(random);
    this.scores = scores;
  }

  /** Starts the console game. */
  public void start() {
    System.out.print("Player name: ");
    if (input.hasNextLine()) {
      String n = input.nextLine().trim();
      if (!n.isEmpty()) {
        player = n;
      }
    }
    try {
      scores.initialize();
    } catch (SQLException e) {
      System.out.println("Database error: " + e.getMessage());
    }
    System.out.println("Commands: " + menu.help());
    while (true) {
      System.out.println(board.render());
      if (!humanTurn) {
        if (computerTurn()) {
          return;
        }
        humanTurn = true;
        continue;
      }
      System.out.print(player + "> ");
      if (!input.hasNextLine()) {
        return;
      }
      String command = input.nextLine().trim();
      if (command.isEmpty()) {
        continue;
      }
      String[] p = command.split("\\s+");
      try {
        switch (p[0].toLowerCase(Locale.ROOT)) {
          case "quit" -> {
            return;
          }
          case "help" -> System.out.println(menu.help());
          case "scores" -> {
            scores.initialize();
            System.out.println(scores.highScores());
          }
          case "save" -> {
            require(p, 2);
            BoardIo.save(board, Path.of(p[1]));
            System.out.println("Board saved.");
          }
          case "load" -> {
            require(p, 2);
            board = BoardIo.load(Path.of(p[1]));
            humanTurn = true;
            System.out.println("Board loaded.");
          }
          case "savexml" -> {
            require(p, 2);
            XmlSave.save(Path.of(p[1]), new XmlSave.Snapshot(board, humanTurn, player));
            System.out.println("Game saved.");
          }
          case "loadxml" -> {
            require(p, 2);
            XmlSave.Snapshot s = XmlSave.load(Path.of(p[1]));
            board = s.board();
            humanTurn = s.humanTurn();
            player = s.player();
            System.out.println("Game resumed.");
          }
          case "move" -> {
            require(p, 3);
            char id = p[1].toUpperCase(Locale.ROOT).charAt(0);
            int distance = Integer.parseInt(p[2]);
            validator.validate(board, id, distance);
            board.move(id, distance);
            events.publish(new GameEvent("move", id + " " + distance));
            humanTurn = false;
          }
          case "exit" -> {
            require(p, 2);
            if (Character.toUpperCase(p[1].charAt(0)) != 'A' || !board.exit('A')) {
              throw new IllegalArgumentException("A must reach the right edge first");
            }
            System.out.println("You win!");
            recordWin(player);
            return;
          }
          default -> System.out.println("Unknown command. Type help.");
        }
      } catch (Exception e) {
        System.out.println("Error: " + e.getMessage());
      }
    }
  }

  private boolean computerTurn() {
    if (board.canExit('B')) {
      board.exit('B');
      System.out.println("Computer wins!");
      recordWin("Computer");
      return true;
    }
    Optional<Board.Move> choice = ai.choose(board);
    if (choice.isEmpty()) {
      System.out.println("No legal moves: draw.");
      return true;
    }
    Board.Move m = choice.get();
    board.move(m.id(), m.delta());
    System.out.println("Computer moved " + m.id() + " by " + m.delta());
    return false;
  }

  private void recordWin(String name) {
    try {
      scores.win(name);
    } catch (SQLException e) {
      System.out.println("Could not save score: " + e.getMessage());
    }
  }

  private static void require(String[] p, int n) {
    if (p.length != n) {
      throw new IllegalArgumentException("Expected " + (n - 1) + " argument(s)");
    }
  }
}
