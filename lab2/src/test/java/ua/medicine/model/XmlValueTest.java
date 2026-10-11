package ua.medicine.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.api.Test;

class XmlValueTest {

    @ParameterizedTest
    @EnumSource(DrugGroup.class)
    void groupIsFoundByValue(DrugGroup group) {
        assertEquals(group, XmlValue.fromValue(DrugGroup.class,
                group.value()));
    }

    @Test
    void valuesOfOtherEnumsAreResolved() {
        assertEquals(DrugForm.SYRUP,
                XmlValue.fromValue(DrugForm.class, "syrup"));
        assertEquals(PackageType.AMPOULE,
                XmlValue.fromValue(PackageType.class, "ampoule"));
        assertEquals(Currency.EUR, XmlValue.fromValue(Currency.class, "EUR"));
        assertEquals(DosageUnit.MCG,
                XmlValue.fromValue(DosageUnit.class, "mcg"));
    }

    @Test
    void unknownValueIsRejected() {
        IllegalArgumentException e = assertThrows(
                IllegalArgumentException.class,
                () -> XmlValue.fromValue(DrugGroup.class, "sweets"));
        assertEquals("Unknown DrugGroup value: sweets", e.getMessage());
    }
}
