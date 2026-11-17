package hu.rushhour.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * A jatektabla szabalyait kezeli.
 * A felulet es az adatbazis nem resze.
 */
public final class Board {

  private final int rows;
  private final int cols;
  private final Map<Character, Vehicle> vehicles = new LinkedHashMap<>();

  /**
   * Letrehoz egy jatektablat.
   *
   * @param rows a sorok szama
   * @param cols az oszlopok szama
   */
  public Board(int rows, int cols) {
    if (rows < 2 || rows > 99 || cols < 4 || cols > 26) {
      throw new IllegalArgumentException("Invalid board size");
    }
    this.rows = rows;
    this.cols = cols;
  }

  /**
   * Visszaadja a sorok szamat.
   *
   * @return a sorok szama
   */
  public int rows() {
    return rows;
  }

  /**
   * Visszaadja az oszlopok szamat.
   *
   * @return az oszlopok szama
   */
  public int cols() {
    return cols;
  }

  /**
   * Visszaadja a tablan levo jarmuveket.
   *
   * @return a jarmuvek listaja
   */
  public Collection<Vehicle> vehicles() {
    return List.copyOf(vehicles.values());
  }

  /**
   * Megkeresi a jarmuvet.
   *
   * @param id a jarmu azonositoja
   * @return a jarmu vagy null
   */
  public Vehicle get(char id) {
    return vehicles.get(Character.toUpperCase(id));
  }

  /**
   * Hozzaad egy jarmuvet.
   *
   * @param v a jarmu
   */
  public void add(Vehicle v) {
    if (v == null) {
      throw new IllegalArgumentException("Invalid placement: null");
    }
    if (vehicles.containsKey(v.id()) || !fits(v, v.id())) {
      throw new IllegalArgumentException("Invalid placement: " + v.id());
    }
    vehicles.put(v.id(), v);
  }

  private boolean fits(Vehicle v, char ignore) {
    for (int i = 0; i < v.length(); i++) {
      int r = v.row() + (v.horizontal() ? 0 : i);
      int c = v.col() + (v.horizontal() ? i : 0);
      if (r < 0 || r >= rows || c < 0 || c >= cols) {
        return false;
      }
      for (Vehicle other : vehicles.values()) {
        if (other.id() != ignore) {
          for (int j = 0; j < other.length(); j++) {
            int otherRow = other.row() + (other.horizontal() ? 0 : j);
            int otherCol = other.col() + (other.horizontal() ? j : 0);
            if (r == otherRow && c == otherCol) {
              return false;
            }
          }
        }
      }
    }
    return true;
  }

  /**
   * Ellenorzi, hogy egy jarmu mozgathato-e.
   *
   * @param id a jarmu azonositoja
   * @param delta az elmozdulas
   * @return igaz, ha a mozgas szabalyos
   */
  public boolean canMove(char id, int delta) {
    Vehicle v = get(id);
    if (v == null || delta == 0) {
      return false;
    }
    for (int n = 1; n <= Math.abs(delta); n++) {
      int step = Integer.signum(delta) * n;
      int newRow = v.row() + (v.horizontal() ? 0 : step);
      int newCol = v.col() + (v.horizontal() ? step : 0);
      if (newRow < 0 || newCol < 0) {
        return false;
      }
      Vehicle shifted = v.shifted(step);
      if (!fits(shifted, v.id())) {
        return false;
      }
    }
    return true;
  }

  /**
   * Vegrehajt egy szabalyos lepest.
   *
   * @param id jarmu azonosito
   * @param delta elmozdulas
   */
  public void move(char id, int delta) {
    if (!canMove(id, delta)) {
      throw new IllegalArgumentException("Illegal move");
    }
    Vehicle v = get(id);
    vehicles.put(v.id(), v.shifted(delta));
  }

  /**
   * Osszegyujti az osszes szabalyos lepest.
   *
   * @return az osszes szabalyos lepes
   */
  public List<Move> moves() {
    List<Move> result = new ArrayList<>();
    for (Vehicle v : vehicles.values()) {
      for (int d = -Math.max(rows, cols); d <= Math.max(rows, cols); d++) {
        if (canMove(v.id(), d)) {
          result.add(new Move(v.id(), d));
        }
      }
    }
    return result;
  }

  /**
   * Ellenorzi a kijarat elereset: A jobbra, B balra.
   *
   * @param id jarmu azonosito
   * @return igaz, ha a jarmu kijaratnal van
   */
  public boolean canExit(char id) {
    id = Character.toUpperCase(id);
    Vehicle v = get(id);
    if (v == null || !v.horizontal()) {
      return false;
    }
    return id == 'A' ? v.col() + v.length() == cols : id == 'B' && v.col() == 0;
  }

  /**
   * Eltavolitja a tavozo jarmuvet.
   *
   * @param id jarmu azonosito
   * @return igaz, ha tavozhatott
   */
  public boolean exit(char id) {
    id = Character.toUpperCase(id);
    if (!canExit(id)) {
      return false;
    }
    vehicles.remove(id);
    return true;
  }

  /** Ellenorzi a ket fo jarmu jelenletet. */
  public void validateHeroes() {
    Vehicle a = get('A');
    Vehicle b = get('B');
    if (a == null || b == null || !a.horizontal() || !b.horizontal()) {
      throw new IllegalArgumentException("A and B must be horizontal hero cars");
    }
  }

  /**
   * Kirajzolja a jatektablat szoveges formaban.
   *
   * @return a jatektabla szoveges kepe
   */
  public String render() {
    char[][] cells = new char[rows][cols];
    for (char[] row : cells) {
      Arrays.fill(row, '.');
    }
    for (Vehicle v : vehicles.values()) {
      for (int i = 0; i < v.length(); i++) {
        int r = v.row() + (v.horizontal() ? 0 : i);
        int c = v.col() + (v.horizontal() ? i : 0);
        cells[r][c] = v.id();
      }
    }
    StringBuilder out = new StringBuilder("   ");
    for (int c = 0; c < cols; c++) {
      out.append((char) ('a' + c)).append(' ');
    }
    out.append(System.lineSeparator());
    for (int r = 0; r < rows; r++) {
      out.append(String.format("%2d ", r + 1));
      for (char ch : cells[r]) {
        out.append(ch).append(' ');
      }
      out.append(System.lineSeparator());
    }
    return out.toString();
  }

  /**
   * Egy jarmu lepese.
   *
   * @param id jarmu azonosito
   * @param delta elmozdulas
   */
  public record Move(char id, int delta) {
  }
}
