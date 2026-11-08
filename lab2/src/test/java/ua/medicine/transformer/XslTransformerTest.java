package ua.medicine.transformer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import ua.medicine.TestData;
import ua.medicine.XmlException;

class XslTransformerTest {

    @TempDir
    Path tempDir;

    private final XslTransformer transformer = new XslTransformer();

    private static InputStream xsl() {
        return XslTransformerTest.class
                .getResourceAsStream("/xml/medicines-by-group.xsl");
    }

    /** Повертає рядки виду "ГРУПА: id1 id2" для кожної групи результату. */
    private static List<String> groupsOf(Path file) throws Exception {
        Element root = DocumentBuilderFactory.newInstance()
                .newDocumentBuilder().parse(file.toFile())
                .getDocumentElement();
        assertEquals("MedicineByGroup", root.getTagName());
        List<String> groups = new ArrayList<>();
        for (Node group = root.getFirstChild(); group != null;
                group = group.getNextSibling()) {
            if (group instanceof Element element) {
                StringBuilder line = new StringBuilder(element.getTagName())
                        .append(':');
                for (Node drug = element.getFirstChild(); drug != null;
                        drug = drug.getNextSibling()) {
                    if (drug instanceof Element drugElement) {
                        line.append(' ').append(drugElement.getAttribute("id"));
                    }
                }
                groups.add(line.toString());
            }
        }
        return groups;
    }

    @Test
    void splitsDrugsIntoGroups() throws Exception {
        String xml = TestData.medicine(TestData.AMOXIL, TestData.ASCORBIC,
                TestData.AMOXIL.replace("d1", "d3"));
        Path output = tempDir.resolve("result.xml");

        transformer.transform(TestData.stream(xml), xsl(), output);

        assertEquals(List.of("ANTIBIOTIC: d1 d3", "VITAMIN: d2"),
                groupsOf(output));
    }

    @Test
    void groupsRealDataFile() throws Exception {
        Path output = tempDir.resolve("result.xml");

        transformer.transform(
                getClass().getResourceAsStream("/xml/medicines.xml"), xsl(),
                output);

        assertEquals(List.of("ANTIBIOTIC: d1 d4", "ANTIHISTAMINE: d6",
                "PAINKILLER: d2", "VITAMIN: d3 d5"), groupsOf(output));
    }

    @Test
    void createsMissingFolders() throws Exception {
        Path output = tempDir.resolve("a/b/result.xml");

        transformer.transform(TestData.stream(TestData.VALID_XML), xsl(),
                output);

        assertEquals(2, groupsOf(output).size());
    }

    @Test
    void failsOnBrokenStylesheet() {
        Path output = tempDir.resolve("result.xml");

        assertThrows(XmlException.class, () -> transformer.transform(
                TestData.stream(TestData.VALID_XML),
                TestData.stream("<xsl:stylesheet>"), output));
    }

    @Test
    void failsWhenFolderCannotBeCreated() throws Exception {
        Path file = Files.createFile(tempDir.resolve("file"));

        assertThrows(XmlException.class, () -> transformer.transform(
                TestData.stream(TestData.VALID_XML), xsl(),
                file.resolve("result.xml")));
    }
}
