package ua.medicine;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ua.medicine.comparator.DrugComparator;
import ua.medicine.exception.XmlProcessingException;
import ua.medicine.model.Drug;
import ua.medicine.model.DrugGroup;
import ua.medicine.parser.DrugParser;
import ua.medicine.transform.XslTransformer;
import ua.medicine.validator.ValidationResult;
import ua.medicine.validator.XmlValidator;

@ExtendWith(MockitoExtension.class)
class MedicineApplicationTest {

    private static final ApplicationPaths PATHS = new ApplicationPaths(
            Path.of("m.xml"), Path.of("m.xsd"), Path.of("m.xsl"),
            Path.of("out.xml"));

    @Mock
    private XmlValidator validator;

    @Mock
    private XslTransformer transformer;

    @Mock
    private DrugParser parser;

    private MedicineApplication application;

    private final List<Drug> drugs = List.of(
            TestData.drug("d-2", "B", DrugGroup.VITAMINS, "2"),
            TestData.drug("d-1", "A", DrugGroup.ANALGESICS, "1"));

    @BeforeEach
    void setUp() {
        application = new MedicineApplication(validator, transformer,
                type -> parser);
    }

    @Test
    void successfulRunValidatesParsesAndTransforms() throws Exception {
        when(validator.validate(PATHS.xml(), PATHS.xsd()))
                .thenReturn(new ValidationResult(List.of()));
        when(parser.parse(PATHS.xml())).thenReturn(drugs);

        assertTrue(application.run(PATHS));

        verify(transformer).transform(PATHS.xml(), PATHS.xsl(),
                PATHS.output());
    }

    @Test
    void invalidDocumentStopsProcessing() throws Exception {
        when(validator.validate(PATHS.xml(), PATHS.xsd()))
                .thenReturn(new ValidationResult(List.of("error")));

        assertFalse(application.run(PATHS));

        verifyNoInteractions(parser, transformer);
    }

    @Test
    void differentParserResultsAreReported() throws Exception {
        when(validator.validate(PATHS.xml(), PATHS.xsd()))
                .thenReturn(new ValidationResult(List.of()));
        when(parser.parse(PATHS.xml()))
                .thenReturn(drugs, drugs, List.of());

        assertFalse(application.run(PATHS));

        verify(transformer, never()).transform(any(), any(), any());
    }

    @Test
    void processingErrorIsReported() throws Exception {
        when(validator.validate(PATHS.xml(), PATHS.xsd()))
                .thenReturn(new ValidationResult(List.of()));
        when(parser.parse(PATHS.xml())).thenReturn(drugs);
        doThrow(new XmlProcessingException("boom"))
                .when(transformer).transform(any(), any(), any());

        assertFalse(application.run(PATHS));
    }

    @Test
    void logSortedReturnsSortedCopy() {
        List<Drug> sorted = application.logSorted(drugs,
                DrugComparator.BY_NAME);

        assertEquals(List.of(drugs.get(1), drugs.get(0)), sorted);
        assertEquals("B", drugs.get(0).getName());
    }

    @Test
    void mainProcessesRealFiles(@TempDir Path dir) {
        Path output = dir.resolve("grouped.xml");

        MedicineApplication.main(new String[] {TestData.XML.toString(),
                TestData.XSD.toString(), TestData.XSL.toString(),
                output.toString()});

        assertTrue(Files.exists(output));
    }

    @Test
    void mainStopsOnInvalidDocument(@TempDir Path dir) {
        Path output = dir.resolve("grouped.xml");

        MedicineApplication.main(new String[] {
                TestData.resource("invalid.xml").toString(),
                TestData.XSD.toString(), TestData.XSL.toString(),
                output.toString()});

        assertFalse(Files.exists(output));
    }
}
