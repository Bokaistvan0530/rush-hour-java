package hu.rushhour.event;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/** Observer pattern: listeners can react to game events. */
public final class GameEventBus {

  private final List<GameEventListener> listeners = new ArrayList<>();

  /**
   * Adds a listener.
   *
   * @param listener event listener
   */
  public void subscribe(GameEventListener listener) {
    listeners.add(Objects.requireNonNull(listener));
  }

  /**
   * Publishes an event.
   *
   * @param event event to publish
   */
  public void publish(GameEvent event) {
    for (GameEventListener listener : List.copyOf(listeners)) {
      listener.onEvent(event);
    }
  }
}
