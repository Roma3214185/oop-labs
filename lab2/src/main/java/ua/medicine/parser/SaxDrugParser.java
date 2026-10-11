package ua.medicine.parser;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.xml.XMLConstants;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Event based parser that uses SAX.
 */
public class SaxDrugParser extends AbstractDrugParser {

    @Override
    protected void parse(InputStream in, DrugAssembler assembler)
            throws Exception {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        factory.setNamespaceAware(true);
        factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
        factory.setFeature(
                "http://apache.org/xml/features/disallow-doctype-decl",
                true);
        SAXParser parser = factory.newSAXParser();
        parser.parse(in, new DrugHandler(assembler));
    }

    /**
     * Forwards SAX events to the assembler.
     */
    private static final class DrugHandler extends DefaultHandler {

        private final DrugAssembler assembler;
        private final StringBuilder text = new StringBuilder();

        DrugHandler(DrugAssembler assembler) {
            this.assembler = assembler;
        }

        @Override
        public void startElement(String uri, String localName,
                String qName, Attributes attributes) {
            text.setLength(0);
            Map<String, String> map = new HashMap<>();
            for (int i = 0; i < attributes.getLength(); i++) {
                map.put(attributes.getLocalName(i), attributes.getValue(i));
            }
            assembler.startElement(localName, map);
        }

        @Override
        public void characters(char[] ch, int start, int length) {
            text.append(ch, start, length);
        }

        @Override
        public void endElement(String uri, String localName,
                String qName) {
            assembler.endElement(localName, text.toString());
            text.setLength(0);
        }
    }
}
