import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Stack;

public class ModelDeckParser extends DefaultHandler {
    private Deck modelDeck;
    private Stack<Card> deck;   // tracking deck to draw from
    private Stack<Card> discard;    // tracking discard pile
    private ArrayList<Card> inPlay; // keep track of cards not in deck to draw from or discard
    private Boolean deckFlag;   // set if we should be adding currentCard to deck
    private Boolean discardFlag;    // set if we should be adding currentCard to discard
    private Boolean inPlayFlag; // set if we should be adding currentCard to inPlay
    private Card currentCard;   // keeps getting reassigned to a new Card object
    private StringBuilder elementContent;

    @Override
    public void startDocument() {
        deck = new Stack<Card>();
        discard = new Stack<>();
        inPlay = new ArrayList<>();
        modelDeck = new Deck();
        deckFlag = false;
        discardFlag = false;
        inPlayFlag = false;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) {
        if (qName.equalsIgnoreCase("deck")) {
            deckFlag = true;    // going forwards, all cards should be added to deck
        } else if (qName.equalsIgnoreCase("discard")) {
            discardFlag = true; // going forwards, all cards should be added to discard
        } else if (qName.equalsIgnoreCase("inPlay")) {
            inPlayFlag = true;  // going forwards, all cards should be added to inPlay
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
    public void endElement(String uri, String localName, String qName) {
        if (qName.equalsIgnoreCase("deck")) {
            deckFlag = false;   // finished adding all deck cards
        } else if (qName.equalsIgnoreCase("discard")) {
            discardFlag = false;    // finished adding all discard cards
        } else if (qName.equalsIgnoreCase("inPlay")) {
            inPlayFlag = false; // finished adding all inPlay cards
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
        } else if (qName.equalsIgnoreCase("Card")) {
            // add a card to either deck, discard, or inPlay depending on which element we are in (indicated by flags)
            if (deckFlag && !discardFlag && !inPlayFlag) {
                deck.push(currentCard);
            } else if (!deckFlag && discardFlag && !inPlayFlag) {
                discard.push(currentCard);
            } else if (!deckFlag && !discardFlag && inPlayFlag) {
                inPlay.add(currentCard);
            }
        }
    }

    public Deck readXMLModelDeckFile(String fileName) throws IOException {
        try {
            SAXParserFactory spf = SAXParserFactory.newInstance();
            SAXParser parser = spf.newSAXParser();
            File file = new File(fileName);
            parser.parse(file, this);


            modelDeck.setDeck(deck);
            modelDeck.setDiscard(discard);
            modelDeck.setInPlay(inPlay);

            return modelDeck;
        } catch (IOException | ParserConfigurationException | SAXException e) {
            throw new IOException(e);
        }
    }
}
