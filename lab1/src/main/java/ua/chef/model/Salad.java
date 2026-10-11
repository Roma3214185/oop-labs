package ua.chef.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import ua.chef.model.vegetable.Vegetable;

/**
 * Salad made of vegetables. Knows its total energy value, can sort its
 * ingredients and find ingredients within a calorie range.
 */
public class Salad {

    private final String name;
    private final List<Vegetable> ingredients = new ArrayList<>();

    /**
     * Creates an empty salad.
     *
     * @param name salad name, not blank
     */
    public Salad(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name must not be blank");
        }
        this.name = name;
    }

    public String getName() {
        return name;
    }

    /**
     * Adds a vegetable to the salad.
     *
     * @param vegetable vegetable to add, not null
     */
    public void addIngredient(Vegetable vegetable) {
        ingredients.add(Objects.requireNonNull(vegetable, "vegetable"));
    }

    /**
     * Returns the ingredients in the current order.
     *
     * @return unmodifiable view of the ingredients
     */
    public List<Vegetable> getIngredients() {
        return Collections.unmodifiableList(ingredients);
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
     * Sorts ingredients of the salad in place.
     *
     * @param comparator order of the ingredients, not null
     */
    public void sortIngredients(Comparator<? super Vegetable> comparator) {
        ingredients.sort(Objects.requireNonNull(comparator, "comparator"));
    }

    /**
     * Finds ingredients whose calories are within the given range.
     *
     * @param min lower bound in kcal, inclusive
     * @param max upper bound in kcal, inclusive
     * @return matching ingredients in the current order
     */
    public List<Vegetable> findByCalories(double min, double max) {
        if (min > max) {
            throw new IllegalArgumentException(
                    "Min " + min + " is greater than max " + max);
        }
        return ingredients.stream()
                .filter(v -> v.getCalories() >= min
                        && v.getCalories() <= max)
                .toList();
    }

    @Override
    public String toString() {
        return String.format(Locale.ROOT, "%s: %d ingredients, %.1f kcal",
                name, ingredients.size(), getTotalCalories());
    }
}
