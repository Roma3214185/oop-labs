package ua.chef.strategy;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import ua.chef.model.vegetable.Vegetable;

/**
 * Sorts vegetables in ascending order of energy value per 100 grams.
 */
public class SortByCaloriesPer100gStrategy implements VegetableStrategy {

    @Override
    public List<Vegetable> apply(List<Vegetable> vegetables) {
        List<Vegetable> sorted = new ArrayList<>(vegetables);
        sorted.sort(Comparator.comparingDouble(Vegetable::getCaloriesPer100g));
        return sorted;
    }

    @Override
    public String getTitle() {
        return "Сортування за калорійністю на 100 г";
    }
}
