package ua.chef.strategy;

import java.util.List;

import ua.chef.model.vegetable.Vegetable;

/**
 * Strategy of selecting vegetables of a salad: sorting them by some
 * parameter or filtering them by some condition.
 */
public interface VegetableStrategy {

    /**
     * Applies the strategy. The given list is not changed.
     *
     * @param vegetables vegetables to process
     * @return new list with the selected vegetables
     */
    List<Vegetable> apply(List<Vegetable> vegetables);

    /**
     * Returns the description of the strategy shown to the user.
     *
     * @return title of the result
     */
    String getTitle();
}
