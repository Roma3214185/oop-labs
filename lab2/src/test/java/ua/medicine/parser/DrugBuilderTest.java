package ua.medicine.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import ua.medicine.TestData;
import ua.medicine.model.Drug;
import ua.medicine.model.DrugGroup;

class DrugBuilderTest {

    private final DrugBuilder builder = new DrugBuilder();

    @Test
    void buildsDrugFromEvents() {
        builder.startElement("Medicine", Map.of());
        builder.startElement("drug", Map.of("id", "d7"));
        builder.endElement("name", "  Цетрин \n");
        builder.endElement("pharm", "Dr. Reddy's");
        builder.endElement("group", "ANTIHISTAMINE");
        builder.startElement("analogs", Map.of());
        builder.endElement("analog", "Зодак");
        builder.endElement("analogs", "");
        builder.endElement("drug", "");

        Drug expected = TestData.drug("d7", "Цетрин", "Dr. Reddy's",
                DrugGroup.ANTIHISTAMINE);
        expected.addAnalog("Зодак");
        assertEquals(List.of(expected), builder.getDrugs());
    }

    @Test
    void hasNoDrugsBeforeEvents() {
        assertTrue(builder.getDrugs().isEmpty());
    }

    @Test
    void returnsCopyOfDrugs() {
        builder.startElement("drug", Map.of("id", "d1"));

        assertThrows(UnsupportedOperationException.class,
                () -> builder.getDrugs().clear());
    }

    @Test
    void rejectsUnknownForm() {
        builder.startElement("drug", Map.of("id", "d1"));

        assertThrows(IllegalArgumentException.class,
                () -> builder.startElement("version", Map.of("form", "PILL")));
    }
}
