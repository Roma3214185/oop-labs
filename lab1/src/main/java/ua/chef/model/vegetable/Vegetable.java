package ua.chef.model.vegetable;

import java.util.Locale;
import java.util.Objects;

/**
 * Base class of the vegetable hierarchy. Every vegetable in the salad
 * has a name, a weight in grams, an energy value per 100 grams and a
 * price per kilogram. Concrete vegetables differ in these values and
 * in the way the chef prepares them.
 */
public abstract class Vegetable {

    private static final double GRAMS_IN_PORTION = 100.0;
    private static final double GRAMS_IN_KILOGRAM = 1000.0;

    private final String name;
    private final double weight;
    private final double caloriesPer100g;
    private final double pricePerKg;

    /**
     * Creates a vegetable.
     *
     * @param name            vegetable name, not blank
     * @param weight          weight in grams, positive finite number
     * @param caloriesPer100g energy value in kcal per 100 g, not negative
     * @param pricePerKg      price in UAH per kilogram, not negative
     */
    protected Vegetable(String name, double weight, double caloriesPer100g,
            double pricePerKg) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name must not be blank");
        }
        if (!Double.isFinite(weight) || weight <= 0) {
            throw new IllegalArgumentException(
                    "Weight must be positive: " + weight);
        }
        if (caloriesPer100g < 0) {
            throw new IllegalArgumentException(
                    "Calories must not be negative: " + caloriesPer100g);
        }
        if (pricePerKg < 0) {
            throw new IllegalArgumentException(
                    "Price must not be negative: " + pricePerKg);
        }
        this.name = name;
        this.weight = weight;
        this.caloriesPer100g = caloriesPer100g;
        this.pricePerKg = pricePerKg;
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

    public double getPricePerKg() {
        return pricePerKg;
    }

    /**
     * Calculates the energy value of this vegetable in the salad.
     *
     * @return calories in kcal for the actual weight
     */
    public double getCalories() {
        return weight * caloriesPer100g / GRAMS_IN_PORTION;
    }

    /**
     * Calculates the cost of this vegetable in the salad.
     *
     * @return price in UAH for the actual weight
     */
    public double getPrice() {
        return weight * pricePerKg / GRAMS_IN_KILOGRAM;
    }

    /**
     * Describes how the chef prepares this vegetable for the salad.
     *
     * @return preparation description
     */
    public abstract String prepare();

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
                && Double.compare(pricePerKg, other.pricePerKg) == 0
                && name.equals(other.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getClass(), name, weight, caloriesPer100g,
                pricePerKg);
    }

    @Override
    public String toString() {
        return String.format(Locale.ROOT, "%s: %.0f g, %.1f kcal, %.2f UAH",
                name, weight, getCalories(), getPrice());
    }
}
