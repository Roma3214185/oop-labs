package ua.medicine.parser;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

/**
 * SAX-парсер: читає документ потоком, сам викликає методи обробника на
 * кожен тег (push).
 */
public class SaxDrugParser extends DrugParser {

    @Override
    protected void read(InputStream xml, DrugBuilder builder)
            throws Exception {
        SAXParserFactory.newInstance()
                .newSAXParser()
                .parse(xml, new Handler(builder));
    }

    /**
     * Пересилає події SAX у {@link DrugBuilder}.
     */
    private static final class Handler extends DefaultHandler {

        private final DrugBuilder builder;
        private final StringBuilder text = new StringBuilder();

        Handler(DrugBuilder builder) {
            this.builder = builder;
        }

        @Override
        public void startElement(String uri, String localName, String qName,
                Attributes attributes) {
            text.setLength(0);
            Map<String, String> map = new HashMap<>();
            for (int i = 0; i < attributes.getLength(); i++) {
                map.put(attributes.getQName(i), attributes.getValue(i));
            }
            builder.startElement(qName, map);
        }

        @Override
        public void characters(char[] ch, int start, int length) {
            // текст може прийти кількома частинами, тому накопичуємо
            text.append(ch, start, length);
        }

        @Override
        public void endElement(String uri, String localName, String qName) {
            builder.endElement(qName, text.toString());
        }
    }
}
