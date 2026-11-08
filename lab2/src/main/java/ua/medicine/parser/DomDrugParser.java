package ua.medicine.parser;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * DOM-парсер: читає весь документ у дерево в пам'яті, потім обходить
 * дерево рекурсивно.
 */
public class DomDrugParser extends DrugParser {

    @Override
    protected void read(InputStream xml, DrugBuilder builder)
            throws Exception {
        Document document = DocumentBuilderFactory.newInstance()
                .newDocumentBuilder()
                .parse(xml);
        visit(document.getDocumentElement(), builder);
    }

    private void visit(Element element, DrugBuilder builder) {
        builder.startElement(element.getTagName(), attributesOf(element));
        NodeList children = element.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            if (children.item(i) instanceof Element child) {
                visit(child, builder);
            }
        }
        builder.endElement(element.getTagName(), element.getTextContent());
    }

    private static Map<String, String> attributesOf(Element element) {
        Map<String, String> attributes = new HashMap<>();
        NamedNodeMap nodes = element.getAttributes();
        for (int i = 0; i < nodes.getLength(); i++) {
            Node attribute = nodes.item(i);
            attributes.put(attribute.getNodeName(), attribute.getNodeValue());
        }
        return attributes;
    }
}
