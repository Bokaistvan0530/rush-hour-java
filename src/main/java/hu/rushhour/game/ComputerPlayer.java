package hu.rushhour.game;

import hu.rushhour.model.Board;
import hu.rushhour.model.Vehicle;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;

/** Veletlenszeruen kivalaszt egy mozgathato autot es egy szabalyos lepest. */
public final class ComputerPlayer {

  private final Random random;

  /**
   * Letrehozza a szamitogepes jatekost.
   *
   * @param random veletlenszam-generator
   */
  public ComputerPlayer(Random random) {
    this.random = Objects.requireNonNull(random);
  }

  /**
   * Kivalaszt egy veletlenszeru szabalyos lepest.
   *
   * @param board aktualis jatektabla
   * @return egy lepes, vagy ures eredmeny, ha nincs szabalyos lepes
   */
  public Optional<Board.Move> choose(Board board) {
    List<Vehicle> candidates = new ArrayList<>();
    for (Vehicle v : board.vehicles()) {
      if (board.moves().stream().anyMatch(m -> m.id() == v.id())) {
        candidates.add(v);
      }
    }
    if (candidates.isEmpty()) {
      return Optional.empty();
    }
    char id = candidates.get(random.nextInt(candidates.size())).id();
    List<Board.Move> legal = board.moves()
        .stream()
        .filter(m -> m.id() == id)
        .toList();
    return Optional.of(legal.get(random.nextInt(legal.size())));
  }
}
