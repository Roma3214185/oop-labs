package ua.medicine.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import ua.medicine.TestData;
import ua.medicine.XmlException;
import ua.medicine.model.Drug;

class DrugParserTest {

    static Stream<DrugParser> parsers() {
        return Stream.of(new DomDrugParser(), new SaxDrugParser(),
                new StaxDrugParser());
    }

    @ParameterizedTest
    @MethodSource("parsers")
    void readsAllDrugs(DrugParser parser) throws XmlException {
        List<Drug> drugs = parser.parse(TestData.stream(TestData.VALID_XML));

        assertEquals(TestData.expectedDrugs(), drugs);
    }

    @ParameterizedTest
    @MethodSource("parsers")
    void keepsOrderOfDrugsFromDocument(DrugParser parser)
            throws XmlException {
        String xml = TestData.medicine(TestData.ASCORBIC, TestData.AMOXIL);

        List<Drug> drugs = parser.parse(TestData.stream(xml));

        assertEquals(List.of(TestData.ascorbic(), TestData.amoxil()), drugs);
    }

    @ParameterizedTest
    @MethodSource("parsers")
    void readsDocumentWithCommentsAndEmptyRoot(DrugParser parser)
            throws XmlException {
        String xml = "<!-- порожній каталог --><Medicine><!-- нічого --></Medicine>";

        assertEquals(List.of(), parser.parse(TestData.stream(xml)));
    }

    @ParameterizedTest
    @MethodSource("parsers")
    void failsOnMalformedXml(DrugParser parser) {
        XmlException e = assertThrows(XmlException.class,
                () -> parser.parse(TestData.stream("<Medicine><drug>")));

        assertTrue(e.getMessage().startsWith(parser.getName()));
    }

    @ParameterizedTest
    @MethodSource("parsers")
    void failsOnUnknownEnumValue(DrugParser parser) {
        String xml = TestData.VALID_XML.replace("ANTIBIOTIC", "CANDY");

        assertThrows(XmlException.class,
                () -> parser.parse(TestData.stream(xml)));
    }

    @ParameterizedTest
    @MethodSource("parsers")
    void nameIsClassName(DrugParser parser) {
        assertEquals(parser.getClass().getSimpleName(), parser.getName());
    }
}
