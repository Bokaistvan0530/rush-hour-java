package hu.rushhour.event;

/** Receives notifications about game events. */
@FunctionalInterface
public interface GameEventListener {

  /**
   * Receives an event.
   *
   * @param event the published event
   */
  void onEvent(GameEvent event);
}
