import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;

public class CardParser extends DefaultHandler {
    private Card card;
    private StringBuilder elementContent;

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) {
        if(qName.equalsIgnoreCase("Card")){
            Card card = new Card(null, null, null, null);
        }
        elementContent = new StringBuilder();
    }

    @Override
    public void characters(char[] ch, int start, int length){
        elementContent.append(ch, start, length);
    }

    @Override
    public void endElement(String uri, String localName, String qName){
        if (qName.equalsIgnoreCase("lightRank")) {
            card.setLightRank(Card.Rank.valueOf(elementContent.toString()));
        } else if (qName.equalsIgnoreCase("lightColour")){
            card.setLightColour(Card.Colour.valueOf(elementContent.toString()));
        } else if (qName.equalsIgnoreCase("darkRank")) {
            card.setDarkRank(Card.Rank.valueOf(elementContent.toString()));
        } else if (qName.equalsIgnoreCase("darkColour")){
            card.setDarkColour(Card.Colour.valueOf(elementContent.toString()));
        }
    }

    public Card readXMLCard(String fileName) throws IOException {
        try {
            SAXParserFactory spf = SAXParserFactory.newInstance();
            SAXParser parser = spf.newSAXParser();
            File file = new File(fileName);
            parser.parse(file, this);
            return card;
        } catch (IOException | ParserConfigurationException | SAXException e) {
            throw new IOException(e);
        }
    }
}
