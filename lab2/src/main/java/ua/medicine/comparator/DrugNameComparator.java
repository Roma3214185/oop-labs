package ua.medicine.comparator;

import java.text.Collator;
import java.util.Comparator;
import java.util.Locale;

import ua.medicine.model.Drug;

/**
 * Сортує препарати за назвою за правилами української абетки
 * (і, ї, є, ґ стоять на своїх місцях).
 */
public class DrugNameComparator implements Comparator<Drug> {

    private final Collator collator =
            Collator.getInstance(Locale.forLanguageTag("uk"));

    @Override
    public int compare(Drug first, Drug second) {
        return collator.compare(first.getName(), second.getName());
    }
}
