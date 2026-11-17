package hu.rushhour.ui;

import java.util.Arrays;
import java.util.Locale;

/** Turns user input into a command name and arguments. */
public final class CommandParser {

  /**
   * A parsed user command.
   *
   * @param name command name
   * @param args command arguments
   */
  public record Command(String name, String[] args) {
  }

  /**
   * Parses a command.
   *
   * @param text input text
   * @return parsed command
   */
  public Command parse(String text) {
    if (text == null || text.isBlank()) {
      throw new IllegalArgumentException("Empty command");
    }
    String[] tokens = text.trim().split("\\s+");
    return new Command(tokens[0].toLowerCase(Locale.ROOT),
        Arrays.copyOfRange(tokens, 1, tokens.length));
  }
}
