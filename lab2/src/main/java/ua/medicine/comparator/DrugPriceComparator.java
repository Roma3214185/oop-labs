package ua.medicine.comparator;

import java.util.Comparator;

import ua.medicine.model.Drug;

/**
 * Сортує препарати за ціною найдешевшої упаковки, при однаковій ціні —
 * за назвою.
 */
public class DrugPriceComparator implements Comparator<Drug> {

    private final Comparator<Drug> byName = new DrugNameComparator();

    @Override
    public int compare(Drug first, Drug second) {
        int result = Double.compare(first.getMinPrice(), second.getMinPrice());
        return result != 0 ? result : byName.compare(first, second);
    }
}
