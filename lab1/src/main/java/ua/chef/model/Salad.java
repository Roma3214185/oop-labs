package ua.chef.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import ua.chef.model.vegetable.Vegetable;
import ua.chef.strategy.VegetableStrategy;

/**
 * Salad made of vegetables. Knows its total weight, energy value and
 * cost. Sorting and searching of the ingredients is delegated to a
 * {@link VegetableStrategy}.
 */
public class Salad {

    private final List<Vegetable> ingredients = new ArrayList<>();

    /**
     * Adds a vegetable to the salad.
     *
     * @param vegetable vegetable to add, not null
     */
    public void add(Vegetable vegetable) {
        ingredients.add(Objects.requireNonNull(vegetable, "vegetable"));
    }

    /**
     * Returns the ingredients in the order they were added.
     *
     * @return unmodifiable view of the ingredients
     */
    public List<Vegetable> getIngredients() {
        return Collections.unmodifiableList(ingredients);
    }

    public boolean isEmpty() {
        return ingredients.isEmpty();
    }

    /**
     * Calculates total weight of the salad.
     *
     * @return weight in grams
     */
    public double getTotalWeight() {
        return ingredients.stream()
                .mapToDouble(Vegetable::getWeight)
                .sum();
    }

    /**
     * Calculates total energy value of the salad.
     *
     * @return calories in kcal
     */
    public double getTotalCalories() {
        return ingredients.stream()
                .mapToDouble(Vegetable::getCalories)
                .sum();
    }

    /**
     * Calculates total cost of the salad.
     *
     * @return price in UAH
     */
    public double getTotalPrice() {
        return ingredients.stream()
                .mapToDouble(Vegetable::getPrice)
                .sum();
    }

    /**
     * Selects ingredients using the given strategy (sorting, filtering).
     * The salad itself is not changed.
     *
     * @param strategy how to select the ingredients, not null
     * @return selected ingredients
     */
    public List<Vegetable> select(VegetableStrategy strategy) {
        return Objects.requireNonNull(strategy, "strategy")
                .apply(getIngredients());
    }
}
