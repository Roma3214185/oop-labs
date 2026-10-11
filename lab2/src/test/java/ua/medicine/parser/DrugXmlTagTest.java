package ua.medicine.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

class DrugXmlTagTest {

    @ParameterizedTest
    @EnumSource(DrugXmlTag.class)
    void tagIsFoundByName(DrugXmlTag tag) {
        assertEquals(tag, DrugXmlTag.of(tag.getTagName()));
    }

    @Test
    void camelCaseNamesAreUsed() {
        assertEquals("issueDate", DrugXmlTag.ISSUE_DATE.getTagName());
        assertEquals("Medicine", DrugXmlTag.MEDICINE.getTagName());
    }

    @Test
    void unknownNameIsRejected() {
        assertThrows(IllegalArgumentException.class,
                () -> DrugXmlTag.of("drugs"));
    }
}
