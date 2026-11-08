package ua.chef.strategy;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import ua.chef.model.vegetable.Vegetable;

/**
 * Sorts vegetables in ascending order of cost in the salad.
 */
public class SortByPriceStrategy implements VegetableStrategy {

    @Override
    public List<Vegetable> apply(List<Vegetable> vegetables) {
        List<Vegetable> sorted = new ArrayList<>(vegetables);
        sorted.sort(Comparator.comparingDouble(Vegetable::getPrice));
        return sorted;
    }

    @Override
    public String getTitle() {
        return "Сортування за вартістю в салаті";
    }
}
