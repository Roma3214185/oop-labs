package ua.chef.config;

import java.util.Objects;

import ua.chef.model.Recipe;
import ua.chef.sort.SortCriterion;

/**
 * Initialization parameters of the application.
 *
 * @param chefName      name of the chef
 * @param recipe        salad recipe
 * @param sortCriterion parameter to sort the ingredients by
 * @param minCalories   lower bound of the calorie search range
 * @param maxCalories   upper bound of the calorie search range
 */
public record AppConfig(String chefName, Recipe recipe,
        SortCriterion sortCriterion, double minCalories,
        double maxCalories) {

    /**
     * Validates the configuration.
     */
    public AppConfig {
        Objects.requireNonNull(chefName, "chefName");
        Objects.requireNonNull(recipe, "recipe");
        Objects.requireNonNull(sortCriterion, "sortCriterion");
        if (minCalories > maxCalories) {
            throw new IllegalArgumentException(
                    "Min calories are greater than max calories");
        }
    }
}
