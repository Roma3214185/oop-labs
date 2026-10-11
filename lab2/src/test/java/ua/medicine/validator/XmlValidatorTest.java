package ua.medicine.validator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import ua.medicine.TestData;
import ua.medicine.exception.XmlProcessingException;

class XmlValidatorTest {

    private final XmlValidator validator = new XmlValidator();

    @Test
    void validDocumentPasses() throws XmlProcessingException {
        ValidationResult result = validator.validate(TestData.XML,
                TestData.XSD);

        assertTrue(result.isValid());
        assertEquals(List.of(), result.errors());
    }

    @Test
    void documentWithOptionalPartsOmittedPasses()
            throws XmlProcessingException {
        assertTrue(validator.validate(TestData.resource("minimal.xml"),
                TestData.XSD).isValid());
    }

    @Test
    void everySchemaViolationIsReported() throws XmlProcessingException {
        ValidationResult result = validator.validate(
                TestData.resource("invalid.xml"), TestData.XSD);

        assertFalse(result.isValid());
        String errors = String.join("\n", result.errors());
        assertTrue(errors.contains("'42'"), errors);
        assertTrue(errors.contains("'sweets'"), errors);
        assertTrue(errors.contains("'-1'"), errors);
        assertTrue(errors.contains("'20'"), errors);
        assertTrue(errors.contains("'form'"), errors);
        assertTrue(result.errors().get(0).startsWith("line "));
    }

    @Test
    void duplicateIdsAreReported(@TempDir Path dir) throws Exception {
        String xml = Files.readString(TestData.XML)
                .replace("drug-002", "drug-001");
        Path file = dir.resolve("duplicate.xml");
        Files.writeString(file, xml);

        ValidationResult result = validator.validate(file, TestData.XSD);

        assertFalse(result.isValid());
        assertTrue(result.errors().get(0).contains("drug-001"));
    }

    @Test
    void malformedDocumentIsReportedAsError()
            throws XmlProcessingException {
        ValidationResult result = validator.validate(
                TestData.resource("malformed.xml"), TestData.XSD);

        assertEquals(1, result.errors().size());
    }

    @Test
    void missingFilesAreReported(@TempDir Path dir) {
        assertThrows(XmlProcessingException.class, () -> validator.validate(
                TestData.XML, dir.resolve("absent.xsd")));
        assertThrows(XmlProcessingException.class, () -> validator.validate(
                dir.resolve("absent.xml"), TestData.XSD));
    }

    @Test
    void resultIsImmutable() throws IOException {
        List<String> errors = new ArrayList<>(List.of("e"));
        ValidationResult result = new ValidationResult(errors);
        errors.clear();

        assertEquals(List.of("e"), result.errors());
        assertThrows(UnsupportedOperationException.class,
                () -> result.errors().clear());
    }
}
