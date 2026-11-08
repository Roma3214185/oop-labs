package ua.medicine;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import ua.medicine.parser.DomDrugParser;
import ua.medicine.parser.DrugParser;
import ua.medicine.parser.SaxDrugParser;
import ua.medicine.parser.StaxDrugParser;

class MedicineApplicationTest {

    @TempDir
    Path tempDir;

    private static List<DrugParser> realParsers() {
        return List.of(new DomDrugParser(), new SaxDrugParser(),
                new StaxDrugParser());
    }

    @Test
    void runsAllStepsAndWritesGroupedXml() throws Exception {
        Path output = tempDir.resolve("out/medicines-by-group.xml");

        boolean success = new MedicineApplication(MedicineApplication.XML,
                output, realParsers()).run();

        assertTrue(success);
        String result = Files.readString(output);
        assertTrue(result.contains("<MedicineByGroup>"));
        assertTrue(result.contains("<VITAMIN>"));
    }

    @Test
    void stopsWhenDocumentIsInvalid() {
        DrugParser parser = mock(DrugParser.class);
        Path output = tempDir.resolve("result.xml");

        boolean success = new MedicineApplication("/invalid-medicines.xml",
                output, List.of(parser)).run();

        assertFalse(success);
        assertFalse(Files.exists(output));
        verify(parser, never()).getName();
    }

    @Test
    void stopsWhenParsersDisagree() throws Exception {
        DrugParser empty = mock(DrugParser.class);
        when(empty.parse(any())).thenReturn(List.of());
        Path output = tempDir.resolve("result.xml");

        boolean success = new MedicineApplication(MedicineApplication.XML,
                output, List.of(new DomDrugParser(), empty)).run();

        assertFalse(success);
        assertFalse(Files.exists(output));
    }

    @Test
    void stopsWhenParserFails() throws Exception {
        DrugParser failing = mock(DrugParser.class);
        when(failing.parse(any())).thenThrow(new XmlException("зламався"));

        boolean success = new MedicineApplication(MedicineApplication.XML,
                tempDir.resolve("result.xml"), List.of(failing)).run();

        assertFalse(success);
    }

    @Test
    void stopsWhenFileIsMissing() {
        boolean success = new MedicineApplication("/no-such-file.xml",
                tempDir.resolve("result.xml"), realParsers()).run();

        assertFalse(success);
    }

    @Test
    void stopsWhenOutputCannotBeWritten() throws Exception {
        Path file = Files.createFile(tempDir.resolve("file"));

        boolean success = new MedicineApplication(MedicineApplication.XML,
                file.resolve("result.xml"), realParsers()).run();

        assertFalse(success);
    }

    @Test
    void mainWritesOutputFile() throws Exception {
        MedicineApplication.main(new String[0]);

        assertTrue(Files.exists(MedicineApplication.OUTPUT));
        assertEquals("<?xml",
                Files.readString(MedicineApplication.OUTPUT).substring(0, 5));
    }
}
