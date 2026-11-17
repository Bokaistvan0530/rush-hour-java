package hu.rushhour.io;

import hu.rushhour.config.GameSettings;
import hu.rushhour.model.Board;
import hu.rushhour.model.Vehicle;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * TXT format: ROWS COLS, then ID ROW COL LENGTH H/V.
 * Coordinates start at 0.
 */
public final class BoardIo {

  private BoardIo() {
  }

  /**
   * Creates the default board layout.
   *
   * @return the default board
   */
  public static Board defaults() {
    Board b = new Board(GameSettings.DEFAULT_ROWS, GameSettings.DEFAULT_COLUMNS);
    b.add(new Vehicle('A', 2, 1, 2, true));
    b.add(new Vehicle('B', 4, 8, 2, true));
    b.add(new Vehicle('C', 0, 3, 3, false));
    b.add(new Vehicle('D', 1, 5, 2, false));
    b.add(new Vehicle('E', 3, 6, 2, true));
    b.add(new Vehicle('F', 0, 9, 3, false));
    return b;
  }

  /**
   * Loads the board.
   *
   * @param path file path
   * @return the loaded board
   * @throws IOException when the file is invalid
   */
  public static Board load(Path path) throws IOException {
    List<String> lines = Files.readAllLines(path)
        .stream()
        .map(String::trim)
        .filter(s -> !s.isBlank() && !s.startsWith("#"))
        .toList();
    if (lines.isEmpty()) {
      throw new IOException("Empty board file");
    }
    try {
      String[] h = lines.getFirst().split("\\s+");
      if (h.length != 2) {
        throw new IllegalArgumentException("Header must have 2 numbers");
      }
      Board b = new Board(Integer.parseInt(h[0]), Integer.parseInt(h[1]));
      for (int i = 1; i < lines.size(); i++) {
        String[] a = lines.get(i).split("\\s+");
        if (a.length != 5 || a[0].length() != 1
            || !(a[4].equalsIgnoreCase("H") || a[4].equalsIgnoreCase("V"))) {
          throw new IllegalArgumentException("Bad vehicle line " + (i + 1));
        }
        b.add(new Vehicle(Character.toUpperCase(a[0].charAt(0)),
            Integer.parseInt(a[1]), Integer.parseInt(a[2]),
            Integer.parseInt(a[3]), a[4].equalsIgnoreCase("H")));
      }
      b.validateHeroes();
      return b;
    } catch (RuntimeException ex) {
      throw new IOException("Invalid board file: " + ex.getMessage(), ex);
    }
  }

  /**
   * Saves the board.
   *
   * @param b board
   * @param path destination
   * @throws IOException when writing fails
   */
  public static void save(Board b, Path path) throws IOException {
    List<String> lines = new ArrayList<>();
    lines.add(b.rows() + " " + b.cols());
    for (Vehicle v : b.vehicles()) {
      lines.add(v.id() + " " + v.row() + " " + v.col() + " "
          + v.length() + " " + (v.horizontal() ? "H" : "V"));
    }
    Files.write(path, lines);
  }
}
