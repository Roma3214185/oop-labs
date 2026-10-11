package ua.medicine.parser;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Tree based parser that uses DOM. The tree is walked recursively and
 * every element is reported to the assembler.
 */
public class DomDrugParser extends AbstractDrugParser {

    @Override
    protected void parse(InputStream in, DrugAssembler assembler)
            throws Exception {
        DocumentBuilderFactory factory =
                DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
        factory.setFeature(
                "http://apache.org/xml/features/disallow-doctype-decl",
                true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        visit(builder.parse(in).getDocumentElement(), assembler);
    }

    private static void visit(Element element, DrugAssembler assembler) {
        assembler.startElement(element.getLocalName(),
                attributes(element));
        StringBuilder text = new StringBuilder();
        NodeList children = element.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node child = children.item(i);
            if (child.getNodeType() == Node.ELEMENT_NODE) {
                visit((Element) child, assembler);
            } else if (child.getNodeType() == Node.TEXT_NODE
                    || child.getNodeType() == Node.CDATA_SECTION_NODE) {
                text.append(child.getNodeValue());
            }
        }
        assembler.endElement(element.getLocalName(), text.toString());
    }

    private static Map<String, String> attributes(Element element) {
        Map<String, String> map = new HashMap<>();
        NamedNodeMap attributes = element.getAttributes();
        for (int i = 0; i < attributes.getLength(); i++) {
            Node attribute = attributes.item(i);
            map.put(attribute.getLocalName(), attribute.getNodeValue());
        }
        return map;
    }
}
