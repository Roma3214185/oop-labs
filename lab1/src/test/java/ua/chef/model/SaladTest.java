package ua.chef.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Comparator;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ua.chef.model.vegetable.Vegetable;
import ua.chef.model.vegetable.kind.Cucumber;
import ua.chef.model.vegetable.kind.Garlic;
import ua.chef.model.vegetable.kind.Onion;
import ua.chef.model.vegetable.kind.Tomato;

class SaladTest {

    private static final double DELTA = 1e-9;

    private Salad salad;
    private Vegetable tomato;
    private Vegetable cucumber;
    private Vegetable onion;
    private Vegetable garlic;

    @BeforeEach
    void setUp() {
        salad = new Salad("Test");
        tomato = new Tomato(200);     // 36 kcal
        cucumber = new Cucumber(100); // 15 kcal
        onion = new Onion(50);        // 20 kcal
        garlic = new Garlic(2);       // 2.98 kcal
        salad.addIngredient(tomato);
        salad.addIngredient(cucumber);
        salad.addIngredient(onion);
        salad.addIngredient(garlic);
    }

    @Test
    void totalCaloriesIsSumOfIngredients() {
        assertEquals(36 + 15 + 20 + 2.98, salad.getTotalCalories(), DELTA);
    }

    @Test
    void totalWeightIsSumOfIngredients() {
        assertEquals(352, salad.getTotalWeight(), DELTA);
    }

    @Test
    void emptySaladHasNoCalories() {
        Salad empty = new Salad("Empty");

        assertEquals(0, empty.getTotalCalories(), DELTA);
        assertTrue(empty.getIngredients().isEmpty());
    }

    @Test
    void ingredientsAreSortedByComparator() {
        salad.sortIngredients(
                Comparator.comparingDouble(Vegetable::getCalories));

        assertEquals(List.of(garlic, cucumber, onion, tomato),
                salad.getIngredients());
    }

    @Test
    void findsIngredientsInCaloriesRangeInclusive() {
        assertEquals(List.of(cucumber, onion),
                salad.findByCalories(15, 20));
        assertEquals(List.of(), salad.findByCalories(100, 200));
    }

    @Test
    void invalidRangeIsRejected() {
        assertThrows(IllegalArgumentException.class,
                () -> salad.findByCalories(30, 10));
    }

    @Test
    void ingredientsCannotBeModifiedFromOutside() {
        assertThrows(UnsupportedOperationException.class,
                () -> salad.getIngredients().clear());
    }

    @Test
    void nullArgumentsAreRejected() {
        assertThrows(NullPointerException.class,
                () -> salad.addIngredient(null));
        assertThrows(NullPointerException.class,
                () -> salad.sortIngredients(null));
        assertThrows(IllegalArgumentException.class, () -> new Salad(""));
        assertThrows(IllegalArgumentException.class, () -> new Salad(null));
    }

    @Test
    void toStringShowsSummary() {
        assertEquals("Test", salad.getName());
        assertEquals("Test: 4 ingredients, 74.0 kcal", salad.toString());
    }
}
