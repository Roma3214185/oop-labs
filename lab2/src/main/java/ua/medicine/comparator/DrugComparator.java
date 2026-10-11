package ua.medicine.comparator;

import java.math.BigDecimal;
import java.util.Comparator;

import ua.medicine.model.Drug;

/**
 * Orders in which drugs can be sorted.
 */
public enum DrugComparator implements Comparator<Drug> {

    /** By identifier. */
    BY_ID {
        @Override
        public int compare(Drug first, Drug second) {
            return first.getId().compareTo(second.getId());
        }
    },

    /** By name ignoring case. */
    BY_NAME {
        @Override
        public int compare(Drug first, Drug second) {
            return String.CASE_INSENSITIVE_ORDER.compare(first.getName(),
                    second.getName());
        }
    },

    /** By group, then by name. */
    BY_GROUP {
        @Override
        public int compare(Drug first, Drug second) {
            int result = first.getGroup().compareTo(second.getGroup());
            return result != 0 ? result : BY_NAME.compare(first, second);
        }
    },

    /** By the cheapest package, drugs without prices go last. */
    BY_MIN_PRICE {
        @Override
        public int compare(Drug first, Drug second) {
            BigDecimal noPrice = null;
            int result = Comparator.nullsLast(BigDecimal::compareTo)
                    .compare(first.getMinPrice().orElse(noPrice),
                            second.getMinPrice().orElse(noPrice));
            return result != 0 ? result : BY_NAME.compare(first, second);
        }
    }
}
