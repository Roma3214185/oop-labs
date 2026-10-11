package ua.medicine.transform;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import ua.medicine.TestData;
import ua.medicine.exception.XmlProcessingException;

class XslTransformerTest {

    private final XslTransformer transformer = new XslTransformer();

    @Test
    void drugsAreSplitIntoGroups(@TempDir Path dir) throws Exception {
        Path output = dir.resolve("nested").resolve("grouped.xml");

        transformer.transform(TestData.XML, TestData.XSL, output);

        Document document = DocumentBuilderFactory.newInstance()
                .newDocumentBuilder().parse(output.toFile());
        Element root = document.getDocumentElement();
        assertEquals("MedicineByGroup", root.getTagName());
        NodeList groups = root.getElementsByTagName("group");
        List<String> names = new ArrayList<>();
        for (int i = 0; i < groups.getLength(); i++) {
            Element group = (Element) groups.item(i);
            names.add(group.getAttribute("name") + ":"
                    + group.getAttribute("count") + ":"
                    + group.getElementsByTagName("drug").getLength());
        }
        assertEquals(List.of("analgesics:1:1", "antibiotics:2:2",
                "antihistamines:1:1", "antipyretics:1:1", "antivirals:1:1",
                "vitamins:2:2"), names);
    }

    @Test
    void groupElementIsRemovedFromDrugs(@TempDir Path dir)
            throws Exception {
        Path output = dir.resolve("grouped.xml");

        transformer.transform(TestData.XML, TestData.XSL, output);

        String text = Files.readString(output);
        assertTrue(text.startsWith(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"));
        assertTrue(text.contains("<drug id=\"drug-001\" "
                + "prescription=\"true\">"));
        assertTrue(!text.contains("<group>") && !text.contains("xmlns"));
    }

    @Test
    void brokenStylesheetIsReported(@TempDir Path dir) {
        assertThrows(XmlProcessingException.class,
                () -> transformer.transform(TestData.XML,
                        TestData.resource("broken.xsl"),
                        dir.resolve("out.xml")));
    }

    @Test
    void outputInsideFileIsReported(@TempDir Path dir) throws Exception {
        Path file = Files.createFile(dir.resolve("file"));

        assertThrows(XmlProcessingException.class,
                () -> transformer.transform(TestData.XML, TestData.XSL,
                        file.resolve("out.xml")));
    }
}
