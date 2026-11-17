package hu.rushhour.io;

import hu.rushhour.model.Board;
import java.nio.file.Path;

/** Betolti a palyat, vagy az alapertelmezett palyat hasznalja. */
public final class DefaultBoardLoader {

  /**
   * Betolti a megadott palyat, vagy az alapertelmezettet adja vissza.
   *
   * @param file a palya fajlja
   * @return a betoltott vagy alapertelmezett palya
   */
  public Board loadOrDefault(Path file) {
    if (file == null) {
      return BoardIo.defaults();
    }
    try {
      return BoardIo.load(file);
    } catch (Exception ignored) {
      return BoardIo.defaults();
    }
  }
}
