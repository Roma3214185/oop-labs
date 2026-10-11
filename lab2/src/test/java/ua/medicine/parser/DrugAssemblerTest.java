package ua.medicine.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import ua.medicine.model.Drug;

class DrugAssemblerTest {

    @Test
    void buildsDrugFromEvents() {
        DrugAssembler assembler = new DrugAssembler();

        assembler.startElement("Medicine", Map.of());
        assembler.startElement("drug", Map.of("id", "drug-001",
                "prescription", "true"));
        assembler.startElement("name", Map.of());
        assembler.endElement("name", "  Aspirin \n");
        assembler.startElement("analogs", Map.of());
        assembler.startElement("analog", Map.of());
        assembler.endElement("analog", "Upsarin");
        assembler.endElement("analogs", null);
        assembler.endElement("drug", "");
        assembler.endElement("Medicine", "");

        Drug drug = assembler.getDrugs().get(0);
        assertEquals("drug-001", drug.getId());
        assertEquals("Aspirin", drug.getName());
        assertEquals(List.of("Upsarin"), drug.getAnalogs());
        assertEquals(true, drug.isPrescription());
    }

    @Test
    void returnedListIsCopy() {
        DrugAssembler assembler = new DrugAssembler();
        assembler.startElement("drug", Map.of("id", "drug-001"));
        assembler.endElement("drug", "");

        assembler.getDrugs().clear();

        assertEquals(1, assembler.getDrugs().size());
    }

    @Test
    void invalidEnumValueIsRejected() {
        DrugAssembler assembler = new DrugAssembler();
        assembler.startElement("drug", Map.of("id", "drug-001"));

        assertThrows(IllegalArgumentException.class,
                () -> assembler.startElement("version",
                        Map.of("form", "gel")));
        assertThrows(IllegalArgumentException.class,
                () -> assembler.endElement("group", "sweets"));
    }
}
