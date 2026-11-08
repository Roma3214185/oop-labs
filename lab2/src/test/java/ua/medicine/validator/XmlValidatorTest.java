package ua.medicine.validator;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.io.InputStream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import ua.medicine.TestData;
import ua.medicine.XmlException;

class XmlValidatorTest {

    private XmlValidator validator;

    @BeforeEach
    void setUp() throws XmlException {
        validator = new XmlValidator(
                getClass().getResourceAsStream("/xml/medicines.xsd"));
    }

    private boolean isValid(String xml) {
        return validator.isValid(TestData.stream(xml));
    }

    @Test
    void acceptsValidDocument() {
        assertTrue(isValid(TestData.VALID_XML));
    }

    @Test
    void acceptsRealDataFile() {
        assertTrue(validator.isValid(
                getClass().getResourceAsStream("/xml/medicines.xml")));
    }

    @Test
    void acceptsDrugsInAnyOrder() {
        assertTrue(isValid(TestData.medicine(TestData.ASCORBIC,
                TestData.AMOXIL)));
    }

    @Test
    void acceptsElementsOfDrugInAnyOrder() {
        String xml = TestData.VALID_XML.replace(
                "<name>Амоксил</name>\n    <pharm>GlaxoSmithKline</pharm>",
                "<pharm>GlaxoSmithKline</pharm>\n    <name>Амоксил</name>");

        assertTrue(xml.indexOf("<pharm>GlaxoSmithKline")
                < xml.indexOf("<name>Амоксил"));
        assertTrue(isValid(xml));
    }

    @Test
    void acceptsDrugWithoutAnalogs() {
        assertTrue(isValid(TestData.medicine(TestData.ASCORBIC)));
    }

    @ParameterizedTest(name = "{0}")
    @CsvSource(delimiter = '|', value = {
        "невідома одиниця дозування | unit=\"MG\"         | unit=\"KG\"",
        "немає одиниці дозування    | unit=\"MG\"         | ''",
        "дубль id                   | id=\"d2\"           | id=\"d1\"",
        "id починається з цифри     | id=\"d1\"           | id=\"1\"",
        "немає id                   | id=\"d1\"           | ''",
        "дубль назви препарату      | Аскорбінова кислота | Амоксил",
        "невідома група             | ANTIBIOTIC          | CANDY",
        "невідома форма             | TABLETS             | PILLS",
        "невідомий тип упаковки     | BLISTER             | BAG",
        "нульова ціна               | 185.40              | 0",
        "три знаки після коми       | 185.40              | 185.401",
        "кількість більше 1000      | quantity=\"20\"     | quantity=\"1001\"",
        "прийомів на день більше 12 | perDay=\"3\"        | perDay=\"13\"",
        "номер свідоцтва            | UA/1001/01          | 1001-01",
        "неправильна дата           | 2021-03-15          | 15.03.2021",
        "коротка назва              | <name>Амоксил       | <name>А",
        "немає фірми                | <pharm>GlaxoSmithKline</pharm> | ''",
        "невідомий елемент          | <group>VITAMIN</group> | <group>VITAMIN</group><price>1</price>",
    })
    void rejectsInvalidDocument(String description, String from, String to) {
        String xml = TestData.VALID_XML.replace(from, to);

        assertNotEquals(TestData.VALID_XML, xml, "заміна не спрацювала");
        assertFalse(isValid(xml));
    }

    @Test
    void rejectsMalformedXml() {
        assertFalse(isValid("<Medicine><drug>"));
    }

    @Test
    void rejectsUnreadableStream() {
        InputStream broken = new InputStream() {
            @Override
            public int read() throws IOException {
                throw new IOException("диск недоступний");
            }
        };

        assertFalse(validator.isValid(broken));
    }

    @Test
    void failsOnBrokenSchema() {
        assertThrows(XmlException.class,
                () -> new XmlValidator(TestData.stream("<xs:schema>")));
    }
}
