package hu.rushhour.event;

/**
 * Simple event describing a move or a game outcome.
 *
 * @param type event type
 * @param message event message
 */
public record GameEvent(String type, String message) {
}
