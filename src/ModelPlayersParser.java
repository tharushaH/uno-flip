import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.ArrayList;

public class ModelPlayersParser extends DefaultHandler {
    private ArrayList<Player> players;
    private Card currentCard;   // keeps getting reassigned to a new Card object
    private StringBuilder elementContent;

    @Override
    public void startDocument() {
        players = new ArrayList<>();
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) {
        if(qName.equalsIgnoreCase("Player")){
            Player currentPlayer = new Player(null);
            players.add(currentPlayer);
        } else if (qName.equalsIgnoreCase("AI")) {
            AI currentAI = new AI();
            players.add(currentAI);
        } else if (qName.equalsIgnoreCase("Card")) {
                currentCard = new Card(null, null, null, null);
        }
        elementContent = new StringBuilder();
    }

    @Override
    public void characters(char[] ch, int start, int length){
        elementContent.append(ch, start, length);
    }

    @Override
    public void endElement(String uri, String localName, String qName){
        if (qName.equalsIgnoreCase("name")) {
            players.get(players.size()-1).setName(elementContent.toString());
        } else if (qName.equalsIgnoreCase("Card")){
            // by now, currentCard has been fully initialized
            players.get(players.size()-1).addCardToHand(currentCard);
        } else if (qName.equalsIgnoreCase("lightRank")) {
            currentCard.setLightRank(Card.Rank.valueOf(elementContent.toString()));
        } else if (qName.equalsIgnoreCase("lightColour")) {
            currentCard.setLightColour(Card.Colour.valueOf(elementContent.toString()));
        } else if (qName.equalsIgnoreCase("darkRank")) {
            currentCard.setDarkRank(Card.Rank.valueOf(elementContent.toString()));
        } else if (qName.equalsIgnoreCase("darkColour")) {
            currentCard.setDarkColour(Card.Colour.valueOf(elementContent.toString()));
        } else if (qName.equalsIgnoreCase("side")) {
            Card.setSide(Boolean.parseBoolean(elementContent.toString()));
        } else if(qName.equalsIgnoreCase("playerScore")) {
            players.get(players.size()-1).setPlayerScore(Integer.parseInt(elementContent.toString()));
        }
    }

    public ArrayList<Player> readXMLModelPlayersFile(String fileName) throws IOException {
        try {
            SAXParserFactory spf = SAXParserFactory.newInstance();
            SAXParser parser = spf.newSAXParser();
            File file = new File(fileName);
            parser.parse(file, this);
            return players;
        } catch (IOException | ParserConfigurationException | SAXException e) {
            throw new IOException(e);
        }
    }
}
