import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;


public class ModelParser extends DefaultHandler {

    @Override
    public void startDocument() throws SAXException {

    }
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {

    }
    @Override
    public void characters(char[] ch, int start, int length){

    }
    @Override
    public void endElement(String uri, String localName, String qName){

    }
    public UnoFlipModel readXML(String fileName) throws IOException {

        return null;
    }


}
