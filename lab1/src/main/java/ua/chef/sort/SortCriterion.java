package ua.chef.sort;

import java.util.Comparator;

import ua.chef.model.vegetable.Vegetable;

/**
 * Parameters the ingredients of a salad can be sorted by.
 */
public enum SortCriterion {
    NAME(Comparator.comparing(Vegetable::getName)),
    WEIGHT(Comparator.comparingDouble(Vegetable::getWeight)),
    CALORIES(Comparator.comparingDouble(Vegetable::getCalories)),
    CALORIES_PER_100G(
            Comparator.comparingDouble(Vegetable::getCaloriesPer100g)),
    CATEGORY(Comparator.comparing(Vegetable::getCategory)
            .thenComparing(Vegetable::getName));

    private final Comparator<Vegetable> comparator;

    SortCriterion(Comparator<Vegetable> comparator) {
        this.comparator = comparator;
    }

    /**
     * Returns comparator for ascending order.
     *
     * @return comparator
     */
    public Comparator<Vegetable> comparator() {
        return comparator;
    }
}
