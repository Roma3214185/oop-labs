package ua.chef.model.vegetable;

import java.util.Locale;
import java.util.Objects;

/**
 * Base class of the vegetable hierarchy. Every vegetable has a name,
 * a weight in grams and an energy value per 100 grams.
 */
public abstract class Vegetable {

    private static final double GRAMS_IN_PORTION = 100.0;

    private final String name;
    private final double weight;
    private final double caloriesPer100g;

    /**
     * Creates a vegetable.
     *
     * @param name            vegetable name, not blank
     * @param weight          weight in grams, positive
     * @param caloriesPer100g energy value in kcal per 100 g, not negative
     */
    protected Vegetable(String name, double weight, double caloriesPer100g) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name must not be blank");
        }
        if (weight <= 0) {
            throw new IllegalArgumentException(
                    "Weight must be positive: " + weight);
        }
        if (caloriesPer100g < 0) {
            throw new IllegalArgumentException(
                    "Calories must not be negative: " + caloriesPer100g);
        }
        this.name = name;
        this.weight = weight;
        this.caloriesPer100g = caloriesPer100g;
    }

    public String getName() {
        return name;
    }

    public double getWeight() {
        return weight;
    }

    public double getCaloriesPer100g() {
        return caloriesPer100g;
    }

    /**
     * Calculates the energy value of this piece of vegetable.
     *
     * @return calories in kcal for the actual weight
     */
    public double getCalories() {
        return weight * caloriesPer100g / GRAMS_IN_PORTION;
    }

    /**
     * Describes how the chef prepares this vegetable for a salad.
     *
     * @return preparation description
     */
    public abstract String prepare();

    /**
     * Returns the human readable category of the vegetable.
     *
     * @return category name
     */
    public abstract String getCategory();

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Vegetable other = (Vegetable) o;
        return Double.compare(weight, other.weight) == 0
                && Double.compare(caloriesPer100g, other.caloriesPer100g) == 0
                && name.equals(other.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getClass(), name, weight, caloriesPer100g);
    }

    @Override
    public String toString() {
        return String.format(Locale.ROOT, "%s (%s): %.0f g, %.1f kcal",
                name, getCategory(), weight, getCalories());
    }
}
