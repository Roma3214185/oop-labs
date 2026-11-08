package ua.medicine.parser;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;

/**
 * StAX-парсер: читає документ потоком, але наступну подію запитуємо
 * самі в циклі (pull).
 */
public class StaxDrugParser extends DrugParser {

    @Override
    protected void read(InputStream xml, DrugBuilder builder)
            throws Exception {
        XMLStreamReader reader =
                XMLInputFactory.newInstance().createXMLStreamReader(xml);
        StringBuilder text = new StringBuilder();
        try {
            while (reader.hasNext()) {
                switch (reader.next()) {
                    case XMLStreamConstants.START_ELEMENT -> {
                        text.setLength(0);
                        builder.startElement(reader.getLocalName(),
                                attributesOf(reader));
                    }
                    case XMLStreamConstants.CHARACTERS ->
                            text.append(reader.getText());
                    case XMLStreamConstants.END_ELEMENT ->
                            builder.endElement(reader.getLocalName(),
                                    text.toString());
                    default -> {
                        // коментарі, кінець документа тощо
                    }
                }
            }
        } finally {
            reader.close();
        }
    }

    private static Map<String, String> attributesOf(XMLStreamReader reader) {
        Map<String, String> attributes = new HashMap<>();
        for (int i = 0; i < reader.getAttributeCount(); i++) {
            attributes.put(reader.getAttributeLocalName(i),
                    reader.getAttributeValue(i));
        }
        return attributes;
    }
}
