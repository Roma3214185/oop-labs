package ua.chef.model;

import java.util.List;
import java.util.Objects;

import ua.chef.factory.VegetableType;

/**
 * Recipe of a salad: its name and the list of ingredients with weights.
 *
 * @param name  salad name
 * @param items ingredients of the recipe
 */
public record Recipe(String name, List<Item> items) {

    /**
     * Creates a recipe with an immutable copy of the items.
     */
    public Recipe {
        Objects.requireNonNull(name, "name");
        items = List.copyOf(items);
    }

    /**
     * Single ingredient of the recipe.
     *
     * @param type   vegetable type
     * @param weight weight in grams
     */
    public record Item(VegetableType type, double weight) {

        /**
         * Validates the ingredient.
         */
        public Item {
            Objects.requireNonNull(type, "type");
            if (weight <= 0) {
                throw new IllegalArgumentException(
                        "Weight must be positive: " + weight);
            }
        }
    }
}
