package ua.medicine.parser;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

/**
 * Pull parser that uses StAX.
 */
public class StaxDrugParser extends AbstractDrugParser {

    @Override
    protected void parse(InputStream in, DrugAssembler assembler)
            throws XMLStreamException {
        XMLInputFactory factory = XMLInputFactory.newInstance();
        factory.setProperty(XMLInputFactory.SUPPORT_DTD, false);
        factory.setProperty(
                XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, false);
        XMLStreamReader reader = factory.createXMLStreamReader(in);
        StringBuilder text = new StringBuilder();
        try {
            while (reader.hasNext()) {
                switch (reader.next()) {
                    case XMLStreamConstants.START_ELEMENT -> {
                        text.setLength(0);
                        assembler.startElement(reader.getLocalName(),
                                attributes(reader));
                    }
                    case XMLStreamConstants.CHARACTERS,
                            XMLStreamConstants.CDATA ->
                            text.append(reader.getText());
                    case XMLStreamConstants.END_ELEMENT -> {
                        assembler.endElement(reader.getLocalName(),
                                text.toString());
                        text.setLength(0);
                    }
                    default -> {
                        // comments, processing instructions, etc.
                    }
                }
            }
        } finally {
            reader.close();
        }
    }

    private static Map<String, String> attributes(XMLStreamReader reader) {
        Map<String, String> map = new HashMap<>();
        for (int i = 0; i < reader.getAttributeCount(); i++) {
            map.put(reader.getAttributeLocalName(i),
                    reader.getAttributeValue(i));
        }
        return map;
    }
}
