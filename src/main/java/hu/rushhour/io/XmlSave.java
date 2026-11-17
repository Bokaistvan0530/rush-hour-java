package hu.rushhour.io;

import hu.rushhour.model.Board;
import hu.rushhour.model.Vehicle;
import java.io.IOException;
import java.nio.file.Path;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/** XML save and load with player turn, with secure XML parsing. */
public final class XmlSave {

  private XmlSave() {
  }

  /**
   * Stores the board and game state.
   *
   * @param board game board
   * @param humanTurn current turn
   * @param player player name
   */
  public record Snapshot(Board board, boolean humanTurn, String player) {
  }

  /**
   * Saves the game to XML.
   *
   * @param file destination
   * @param s snapshot
   * @throws Exception when saving fails
   */
  public static void save(Path file, Snapshot s) throws Exception {
    Document d = DocumentBuilderFactory.newInstance()
        .newDocumentBuilder()
        .newDocument();
    Element root = d.createElement("rushhour");
    root.setAttribute("rows", String.valueOf(s.board().rows()));
    root.setAttribute("cols", String.valueOf(s.board().cols()));
    root.setAttribute("humanTurn", String.valueOf(s.humanTurn()));
    root.setAttribute("player", s.player());
    d.appendChild(root);
    for (Vehicle v : s.board().vehicles()) {
      Element e = d.createElement("vehicle");
      e.setAttribute("id", String.valueOf(v.id()));
      e.setAttribute("row", String.valueOf(v.row()));
      e.setAttribute("col", String.valueOf(v.col()));
      e.setAttribute("length", String.valueOf(v.length()));
      e.setAttribute("orientation", v.horizontal() ? "H" : "V");
      root.appendChild(e);
    }
    TransformerFactory tf = TransformerFactory.newInstance();
    tf.setFeature(javax.xml.XMLConstants.FEATURE_SECURE_PROCESSING, true);
    Transformer t = tf.newTransformer();
    t.setOutputProperty("indent", "yes");
    t.transform(new DOMSource(d), new StreamResult(file.toFile()));
  }

  /**
   * Loads a snapshot from XML.
   *
   * @param file source file
   * @return loaded snapshot
   * @throws Exception when loading fails
   */
  public static Snapshot load(Path file) throws Exception {
    DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
    f.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
    f.setFeature("http://xml.org/sax/features/external-general-entities", false);
    f.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
    f.setXIncludeAware(false);
    f.setExpandEntityReferences(false);
    Document d = f.newDocumentBuilder().parse(file.toFile());
    Element r = d.getDocumentElement();
    if (!"rushhour".equals(r.getTagName())) {
      throw new IOException("Not a Rush Hour save");
    }
    Board b = new Board(Integer.parseInt(r.getAttribute("rows")),
        Integer.parseInt(r.getAttribute("cols")));
    NodeList cars = r.getElementsByTagName("vehicle");
    for (int i = 0; i < cars.getLength(); i++) {
      Element e = (Element) cars.item(i);
      b.add(new Vehicle(e.getAttribute("id").charAt(0),
          Integer.parseInt(e.getAttribute("row")),
          Integer.parseInt(e.getAttribute("col")),
          Integer.parseInt(e.getAttribute("length")),
          "H".equals(e.getAttribute("orientation"))));
    }
    b.validateHeroes();
    return new Snapshot(b, Boolean.parseBoolean(r.getAttribute("humanTurn")),
        r.getAttribute("player"));
  }
}
