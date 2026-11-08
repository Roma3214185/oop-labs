package ua.medicine.comparator;

import java.util.Comparator;

import ua.medicine.model.Drug;

/**
 * Сортує препарати за групою, а в межах групи — за назвою.
 */
public class DrugGroupComparator implements Comparator<Drug> {

    private final Comparator<Drug> byName = new DrugNameComparator();

    @Override
    public int compare(Drug first, Drug second) {
        int result = first.getGroup().compareTo(second.getGroup());
        return result != 0 ? result : byName.compare(first, second);
    }
}
