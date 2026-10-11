package ua.medicine.comparator;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import ua.medicine.TestData;
import ua.medicine.model.Drug;
import ua.medicine.model.DrugGroup;

class DrugComparatorTest {

    private final Drug zinc =
            TestData.drug("d-3", "zinc", DrugGroup.VITAMINS, "40.00");
    private final Drug aspirin =
            TestData.drug("d-1", "Aspirin", DrugGroup.ANALGESICS, "15.50");
    private final Drug biseptol =
            TestData.drug("d-4", "Biseptol", DrugGroup.ANTIBIOTICS, null);
    private final Drug amoxil =
            TestData.drug("d-2", "Amoxil", DrugGroup.ANTIBIOTICS, "15.50");

    private List<Drug> sort(DrugComparator comparator) {
        List<Drug> drugs = new ArrayList<>(
                List.of(zinc, aspirin, biseptol, amoxil));
        drugs.sort(comparator);
        return drugs;
    }

    @Test
    void sortsById() {
        assertEquals(List.of(aspirin, amoxil, zinc, biseptol),
                sort(DrugComparator.BY_ID));
    }

    @Test
    void sortsByNameIgnoringCase() {
        assertEquals(List.of(amoxil, aspirin, biseptol, zinc),
                sort(DrugComparator.BY_NAME));
    }

    @Test
    void sortsByGroupThenName() {
        assertEquals(List.of(amoxil, biseptol, aspirin, zinc),
                sort(DrugComparator.BY_GROUP));
    }

    @Test
    void sortsByMinPriceThenNameWithMissingPricesLast() {
        assertEquals(List.of(amoxil, aspirin, zinc, biseptol),
                sort(DrugComparator.BY_MIN_PRICE));
    }
}
