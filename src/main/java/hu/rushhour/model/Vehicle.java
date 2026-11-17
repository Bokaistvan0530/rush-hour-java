package hu.rushhour.model;

/**
 * Immutable vehicle position; orientation cannot change.
 *
 * @param id vehicle identifier
 * @param row top row of vehicle
 * @param col left column of vehicle
 * @param length vehicle length
 * @param horizontal true for horizontal orientation
 */
public record Vehicle(char id, int row, int col, int length, boolean horizontal) {

  /** Validates a vehicle. */
  public Vehicle {
    if (!Character.isLetterOrDigit(id) || row < 0 || col < 0 || length < 2) {
      throw new IllegalArgumentException("Invalid vehicle");
    }
  }

  /**
   * Moves a vehicle along its orientation.
   *
   * @param distance signed distance
   * @return the shifted vehicle
   */
  public Vehicle shifted(int distance) {
    return new Vehicle(id, row + (horizontal ? 0 : distance),
        col + (horizontal ? distance : 0), length, horizontal);
  }
}
