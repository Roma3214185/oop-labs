package ua.chef.strategy;

import java.util.List;
import java.util.Locale;

import ua.chef.model.vegetable.Vegetable;

/**
 * Finds vegetables whose energy value in the salad is within the given
 * range (both bounds inclusive).
 */
public class CaloriesRangeStrategy implements VegetableStrategy {

    private final double min;
    private final double max;

    /**
     * Creates the strategy.
     *
     * @param min lower bound in kcal, not negative
     * @param max upper bound in kcal, not less than min
     */
    public CaloriesRangeStrategy(double min, double max) {
        if (min < 0) {
            throw new IllegalArgumentException(
                    "Min must not be negative: " + min);
        }
        if (min > max) {
            throw new IllegalArgumentException(
                    "Min " + min + " is greater than max " + max);
        }
        this.min = min;
        this.max = max;
    }

    public double getMin() {
        return min;
    }

    public double getMax() {
        return max;
    }

    @Override
    public List<Vegetable> apply(List<Vegetable> vegetables) {
        return vegetables.stream()
                .filter(v -> v.getCalories() >= min && v.getCalories() <= max)
                .toList();
    }

    @Override
    public String getTitle() {
        return String.format(Locale.ROOT,
                "Овочі з калорійністю в салаті від %.1f до %.1f ккал",
                min, max);
    }
}
