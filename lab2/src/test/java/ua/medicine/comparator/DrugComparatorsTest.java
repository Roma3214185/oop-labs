package ua.medicine.comparator;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.junit.jupiter.api.Test;

import ua.medicine.TestData;
import ua.medicine.model.Drug;
import ua.medicine.model.DrugGroup;

class DrugComparatorsTest {

    private static List<String> sortedNames(List<Drug> drugs,
            Comparator<Drug> comparator) {
        List<Drug> sorted = new ArrayList<>(drugs);
        sorted.sort(comparator);
        return sorted.stream().map(Drug::getName).toList();
    }

    /** Звичайний compareTo поставив би «І» і «Є» перед «А». */
    @Test
    void sortsByUkrainianAlphabet() {
        List<Drug> drugs = List.of(
                TestData.drugWithPrice("Ібупрофен", DrugGroup.PAINKILLER, 1),
                TestData.drugWithPrice("Яблуко", DrugGroup.VITAMIN, 1),
                TestData.drugWithPrice("Гліцин", DrugGroup.VITAMIN, 1),
                TestData.drugWithPrice("Єгер", DrugGroup.VITAMIN, 1),
                TestData.drugWithPrice("аспірин", DrugGroup.PAINKILLER, 1));

        assertEquals(List.of("аспірин", "Гліцин", "Єгер", "Ібупрофен",
                "Яблуко"), sortedNames(drugs, new DrugNameComparator()));
    }

    @Test
    void sortsByGroupThenName() {
        List<Drug> drugs = List.of(
                TestData.drugWithPrice("Б", DrugGroup.VITAMIN, 1),
                TestData.drugWithPrice("В", DrugGroup.ANTIBIOTIC, 1),
                TestData.drugWithPrice("А", DrugGroup.VITAMIN, 1));

        assertEquals(List.of("В", "А", "Б"),
                sortedNames(drugs, new DrugGroupComparator()));
    }

    @Test
    void sortsByMinPriceThenName() {
        List<Drug> drugs = List.of(
                TestData.drugWithPrice("Б", DrugGroup.VITAMIN, 50),
                TestData.drugWithPrice("В", DrugGroup.VITAMIN, 10),
                TestData.drugWithPrice("А", DrugGroup.VITAMIN, 50));

        assertEquals(List.of("В", "А", "Б"),
                sortedNames(drugs, new DrugPriceComparator()));
    }

    @Test
    void priceComparatorUsesCheapestPackage() {
        // Амоксил: найдешевша упаковка 142.10; Аскорбінка: 25.00
        assertEquals(List.of("Аскорбінова кислота", "Амоксил"),
                sortedNames(TestData.expectedDrugs(),
                        new DrugPriceComparator()));
    }
}
