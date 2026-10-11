package ua.chef.sort;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import ua.chef.model.vegetable.Vegetable;
import ua.chef.model.vegetable.kind.Carrot;
import ua.chef.model.vegetable.kind.Garlic;
import ua.chef.model.vegetable.kind.Lettuce;
import ua.chef.model.vegetable.kind.Tomato;

class SortCriterionTest {

    private final Vegetable tomato = new Tomato(300);   // 54 kcal
    private final Vegetable garlic = new Garlic(10);    // 14.9 kcal
    private final Vegetable carrot = new Carrot(100);   // 41 kcal
    private final Vegetable lettuce = new Lettuce(50);  // 7.5 kcal

    private List<Vegetable> sort(SortCriterion criterion) {
        List<Vegetable> list = new ArrayList<>(
                List.of(tomato, garlic, carrot, lettuce));
        list.sort(criterion.comparator());
        return list;
    }

    @Test
    void sortsByName() {
        assertEquals(List.of(carrot, garlic, lettuce, tomato),
                sort(SortCriterion.NAME));
    }

    @Test
    void sortsByWeight() {
        assertEquals(List.of(garlic, lettuce, carrot, tomato),
                sort(SortCriterion.WEIGHT));
    }

    @Test
    void sortsByCalories() {
        assertEquals(List.of(lettuce, garlic, carrot, tomato),
                sort(SortCriterion.CALORIES));
    }

    @Test
    void sortsByCaloriesPer100g() {
        assertEquals(List.of(lettuce, tomato, carrot, garlic),
                sort(SortCriterion.CALORIES_PER_100G));
    }

    @Test
    void sortsByCategoryThenName() {
        assertEquals(List.of(garlic, tomato, lettuce, carrot),
                sort(SortCriterion.CATEGORY));
    }
}
