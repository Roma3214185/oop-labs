package ua.medicine.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import ua.medicine.TestData;
import ua.medicine.exception.XmlProcessingException;
import ua.medicine.model.Currency;
import ua.medicine.model.Drug;
import ua.medicine.model.DrugForm;
import ua.medicine.model.DrugGroup;
import ua.medicine.model.Producer;

/**
 * Every test runs for SAX, StAX and DOM parsers.
 */
class DrugParserTest {

    @ParameterizedTest
    @EnumSource(ParserType.class)
    void parsesAllDrugsInDocumentOrder(ParserType type) throws Exception {
        List<Drug> drugs = type.createParser().parse(TestData.XML);

        assertEquals(8, drugs.size());
        assertEquals(List.of("drug-001", "drug-002", "drug-003",
                "drug-004", "drug-005", "drug-006", "drug-007",
                "drug-008"), drugs.stream().map(Drug::getId).toList());
    }

    @ParameterizedTest
    @EnumSource(ParserType.class)
    void parsesEveryPropertyOfDrug(ParserType type) throws Exception {
        List<Drug> drugs = type.createParser().parse(TestData.XML);

        assertEquals(TestData.aevit(), drugs.get(4));
    }

    @ParameterizedTest
    @EnumSource(ParserType.class)
    void parsesAnalogsVersionsAndSeveralProducers(ParserType type)
            throws Exception {
        Drug amoxil = type.createParser().parse(TestData.XML).get(0);

        assertTrue(amoxil.isPrescription());
        assertEquals(DrugGroup.ANTIBIOTICS, amoxil.getGroup());
        assertEquals(List.of("Flemoxin Solutab", "Ospamox"),
                amoxil.getAnalogs());
        assertEquals(2, amoxil.getVersions().size());
        assertEquals(DrugForm.POWDER, amoxil.getVersions().get(1).getForm());
        List<Producer> producers = amoxil.getVersions().get(0)
                .getProducers();
        assertEquals(List.of("GlaxoSmithKline UK", "Teva Pharmaceuticals"),
                producers.stream().map(Producer::getName).toList());
        assertEquals(new BigDecimal("142.10"),
                producers.get(1).getDrugPackage().getPrice());
    }

    @ParameterizedTest
    @EnumSource(ParserType.class)
    void appliesDefaultsForOmittedAttributes(ParserType type)
            throws Exception {
        List<Drug> drugs = type.createParser()
                .parse(TestData.resource("minimal.xml"));

        assertEquals(List.of(TestData.validol()), drugs);
        Drug validol = drugs.get(0);
        assertFalse(validol.isPrescription());
        assertTrue(validol.getAnalogs().isEmpty());
        assertEquals(Currency.UAH, validol.getVersions().get(0)
                .getProducers().get(0).getDrugPackage().getCurrency());
    }

    @ParameterizedTest
    @EnumSource(ParserType.class)
    void malformedDocumentIsReported(ParserType type) {
        assertThrows(XmlProcessingException.class, () -> type.createParser()
                .parse(TestData.resource("malformed.xml")));
    }

    @ParameterizedTest
    @EnumSource(ParserType.class)
    void unknownElementIsReported(ParserType type) {
        XmlProcessingException e = assertThrows(XmlProcessingException.class,
                () -> type.createParser()
                        .parse(TestData.resource("unknown-element.xml")));
        assertTrue(e.getMessage().contains("Unknown element: tablet"));
    }

    @ParameterizedTest
    @EnumSource(ParserType.class)
    void externalEntitiesAreRejected(ParserType type) {
        assertThrows(XmlProcessingException.class, () -> type.createParser()
                .parse(TestData.resource("doctype.xml")));
    }

    @ParameterizedTest
    @EnumSource(ParserType.class)
    void missingFileIsReported(ParserType type) {
        assertThrows(XmlProcessingException.class, () -> type.createParser()
                .parse(Path.of("absent.xml")));
    }

    @Test
    void allParsersReturnEqualResults() throws Exception {
        List<Drug> sax = ParserType.SAX.createParser().parse(TestData.XML);

        assertEquals(sax, ParserType.STAX.createParser().parse(TestData.XML));
        assertEquals(sax, ParserType.DOM.createParser().parse(TestData.XML));
    }

    @Test
    void parserTypeCreatesMatchingImplementation() {
        assertInstanceOf(SaxDrugParser.class,
                ParserType.SAX.createParser());
        assertInstanceOf(StaxDrugParser.class,
                ParserType.STAX.createParser());
        assertInstanceOf(DomDrugParser.class,
                ParserType.DOM.createParser());
    }
}
