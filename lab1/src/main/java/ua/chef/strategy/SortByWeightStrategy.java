package ua.chef.strategy;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import ua.chef.model.vegetable.Vegetable;

/**
 * Sorts vegetables in ascending order of weight in the salad.
 */
public class SortByWeightStrategy implements VegetableStrategy {

    @Override
    public List<Vegetable> apply(List<Vegetable> vegetables) {
        List<Vegetable> sorted = new ArrayList<>(vegetables);
        sorted.sort(Comparator.comparingDouble(Vegetable::getWeight));
        return sorted;
    }

    @Override
    public String getTitle() {
        return "Сортування за масою в салаті";
    }
}
