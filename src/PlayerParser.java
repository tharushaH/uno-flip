import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import java.io.StringWriter;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class PlayerParser extends DefaultHandler {
    private ArrayList<Player> players;
    private StringBuilder elementContent;
    private static final String PLAYER_XML_STR = "Player";

    @Override
    public void startDocument() {
        players = new ArrayList<>();
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) {
        if(qName.equalsIgnoreCase(PLAYER_XML_STR)){
            Player currentPlayer = new Player(null);
            players.add(currentPlayer);
        }
        elementContent = new StringBuilder();
    }

    @Override
    public void characters(char[] ch, int start, int length){
        elementContent.append(ch, start, length);
    }

    @Override
    public void endElement(String uri, String localName, String qName){}

    public ArrayList<Player> readXMLPlayer(String fileName) {
        return players;
    }
}
