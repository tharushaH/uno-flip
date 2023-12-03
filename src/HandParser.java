import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class HandParser extends DefaultHandler {
    private ArrayList<Card> cards;
    private StringBuilder elementContent;

    @Override
    public void startDocument() {
        cards = new ArrayList<>();
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) {
        if(qName.equalsIgnoreCase("Card")){
            Card currentCard = new Card(null, null, null, null);
        }
        elementContent = new StringBuilder();
    }

    @Override
    public void characters(char[] ch, int start, int length){
        elementContent.append(ch, start, length);
    }

    @Override
    public void endElement(String uri, String localName, String qName) {
        if (qName.equalsIgnoreCase("lightRank")) {
            cards.get(cards.size()-1).setLightRank(Card.Rank.valueOf(elementContent.toString()));
        } else if (qName.equalsIgnoreCase("lightColour")){
            cards.get(cards.size()-1).setLightColour(Card.Colour.valueOf(elementContent.toString()));
        } else if (qName.equalsIgnoreCase("darkRank")) {
            cards.get(cards.size()-1).setDarkRank(Card.Rank.valueOf(elementContent.toString()));
        } else if (qName.equalsIgnoreCase("darkColour")){
            cards.get(cards.size()-1).setDarkColour(Card.Colour.valueOf(elementContent.toString()));
        }
    }

    public ArrayList<Card> readXMLHand(String fileName) throws IOException {
        try {
            SAXParserFactory spf = SAXParserFactory.newInstance();
            SAXParser parser = spf.newSAXParser();
            File file = new File(fileName);
            parser.parse(file, this);
            return cards;
        } catch (IOException | ParserConfigurationException | SAXException e) {
            throw new IOException(e);
        }
    }
}
