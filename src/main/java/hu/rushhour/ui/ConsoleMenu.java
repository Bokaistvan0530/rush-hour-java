package hu.rushhour.ui;

/** Terminal help text. */
public final class ConsoleMenu {

  /**
   * Lists the supported console commands.
   *
   * @return available commands
   */
  public String help() {
    return "move C 2 | exit A | save board.txt | load board.txt | "
        + "savexml game.xml | loadxml game.xml | scores | help | quit";
  }
}
