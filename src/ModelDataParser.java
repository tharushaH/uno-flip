import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.ElementType;
import java.util.ArrayList;

public class ModelDataParser extends DefaultHandler {
    private UnoFlipModel model;
    private Card topCard;
    private StringBuilder elementContent;

    @Override
    public void startDocument() {
        model = new UnoFlipModel();
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) {
        if (qName.equalsIgnoreCase("Card")) {
            topCard = new Card(null, null, null, null);
        }
        elementContent = new StringBuilder();
    }

    @Override
    public void characters(char[] ch, int start, int length){
        elementContent.append(ch, start, length);
    }

    @Override
    public void endElement(String uri, String localName, String qName){
        if (qName.equalsIgnoreCase("turnFinished")) {
            model.setTurnFinished(Boolean.parseBoolean(elementContent.toString()));
        } else if (qName.equalsIgnoreCase("skipTurn")) {
            model.setSkipTurn(Boolean.parseBoolean(elementContent.toString()));
        } else if (qName.equalsIgnoreCase("skipEveryone")) {
            model.setSkipEveryone(Boolean.parseBoolean(elementContent.toString()));
        } else if (qName.equalsIgnoreCase("turnDirection")) {
            model.setTurnDirection(Boolean.parseBoolean(elementContent.toString()));
        } else if (qName.equalsIgnoreCase("numPlayer")) {
            model.setNumPlayers(Integer.parseInt(elementContent.toString()));
        } else if (qName.equalsIgnoreCase("chosenCardIndex")) {
            model.setChosenCardIndex(Integer.parseInt(elementContent.toString()));
        } else if (qName.equalsIgnoreCase("currentTurn")) {
            model.setCurrentTurn(Integer.parseInt(elementContent.toString()));
        } else if (qName.equalsIgnoreCase("nextPlayerIndex")) {
            model.setNextPlayerIndex(Integer.parseInt(elementContent.toString()));
        } else if (qName.equalsIgnoreCase("status")) {
            model.setStatus(elementContent.toString());
        } else if (qName.equalsIgnoreCase("currentColour")) {
            model.setCurrentColour(Card.Colour.valueOf(elementContent.toString()));
        } else if (qName.equalsIgnoreCase("previousColour")) {
            model.setPreviousColour(Card.Colour.valueOf(elementContent.toString().toUpperCase()));
        } else if (qName.equalsIgnoreCase("currentRank")) {
            model.setCurrentRank(Card.Rank.valueOf(elementContent.toString()));
        } else if (qName.equalsIgnoreCase("previousRank")) {
            model.setPreviousRank(Card.Rank.valueOf(elementContent.toString().toUpperCase()));
        } else if (qName.equalsIgnoreCase("isWinner")) {
            model.setWinner(Boolean.parseBoolean(elementContent.toString()));
        } else if (qName.equalsIgnoreCase("Card")){
            model.setTopCard(topCard);  // by now, the card is fully initialized
        } else if (qName.equalsIgnoreCase("lightRank")) {
            topCard.setLightRank(Card.Rank.valueOf(elementContent.toString()));
        } else if (qName.equalsIgnoreCase("lightColour")) {
            topCard.setLightColour(Card.Colour.valueOf(elementContent.toString()));
        } else if (qName.equalsIgnoreCase("darkRank")) {
            topCard.setDarkRank(Card.Rank.valueOf(elementContent.toString()));
        } else if (qName.equalsIgnoreCase("darkColour")) {
            topCard.setDarkColour(Card.Colour.valueOf(elementContent.toString()));
        } else if (qName.equalsIgnoreCase("side")) {
            Card.setSide(Boolean.parseBoolean(elementContent.toString()));
        }
    }

    public UnoFlipModel readXMLModelDataFile(String dataFileName, String playersFileName, String deckFileName) throws IOException {
        try {
            SAXParserFactory spf = SAXParserFactory.newInstance();
            SAXParser parser = spf.newSAXParser();
            File file = new File(dataFileName);
            parser.parse(file, this);

            // get the players using the players parser and set the model's players
            ModelPlayersParser playersParser = new ModelPlayersParser();
            model.setPlayers(playersParser.readXMLModelPlayersFile(playersFileName));

            // get the deck using the deck parser and set the model's deck
            ModelDeckParser deckParser = new ModelDeckParser();
            model.setDeck(deckParser.readXMLModelDeckFile(deckFileName));

            return model;
        } catch (IOException | ParserConfigurationException | SAXException e) {
            throw new IOException(e);
        }
    }
}
