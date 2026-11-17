package hu.rushhour.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/** JDBC adatbazis-kezeles a beepitett H2 adatbazissal. */
public final class ScoreRepository {

  private final String url;

  /**
   * Letrehozza az adatbazis-kapcsolat beallitasait.
   *
   * @param file adatbazis fajljanak utvonala
   */
  public ScoreRepository(String file) {
    url = "jdbc:h2:file:" + file;
  }

  private Connection open() throws SQLException {
    return DriverManager.getConnection(url, "sa", "");
  }

  /**
   * Letrehozza a pontszamok tarolasahoz szukseges tablat.
   *
   * @throws SQLException ha adatbazishiba tortenik
   */
  public void initialize() throws SQLException {
    try (Connection c = open();
         Statement s = c.createStatement()) {
      s.executeUpdate(
          "CREATE TABLE IF NOT EXISTS scores "
              + "(player VARCHAR(100) PRIMARY KEY, wins INT NOT NULL)"
      );
    }
  }

  /**
   * Egy gyozelemmel noveli a jatekos pontszamat.
   *
   * @param name jatekos neve
   * @throws SQLException ha adatbazishiba tortenik
   */
  public void win(String name) throws SQLException {
    String sql = "MERGE INTO scores (player,wins) KEY(player) "
        + "VALUES (?, COALESCE("
        + "(SELECT wins FROM scores WHERE player=?),0)+1)";
    try (Connection c = open();
         PreparedStatement p = c.prepareStatement(sql)) {
      p.setString(1, name);
      p.setString(2, name);
      p.executeUpdate();
    }
  }

  /**
   * Visszaadja a ranglistat.
   *
   * @return ranglista szovege
   * @throws SQLException ha adatbazishiba tortenik
   */
  public String highScores() throws SQLException {
    StringBuilder b = new StringBuilder("HIGH SCORES\n");
    String sql = "SELECT player,wins FROM scores "
        + "ORDER BY wins DESC,player";
    try (Connection c = open();
         Statement s = c.createStatement();
         ResultSet r = s.executeQuery(sql)) {
      while (r.next()) {
        b.append(r.getString(1))
            .append(": ")
            .append(r.getInt(2))
            .append('\n');
      }
    }
    return b.toString();
  }
}
