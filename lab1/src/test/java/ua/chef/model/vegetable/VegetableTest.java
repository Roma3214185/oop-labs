package ua.chef.model.vegetable;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import ua.chef.model.vegetable.kind.Carrot;
import ua.chef.model.vegetable.kind.Tomato;

class VegetableTest {

    private static final double DELTA = 1e-9;

    @Test
    void caloriesAreCalculatedFromWeight() {
        Vegetable carrot = new Carrot(250);

        assertEquals(250 * Carrot.CALORIES_PER_100G / 100,
                carrot.getCalories(), DELTA);
    }

    @Test
    void gettersReturnConstructorValues() {
        Vegetable tomato = new Tomato(120);

        assertEquals("Tomato", tomato.getName());
        assertEquals(120, tomato.getWeight(), DELTA);
        assertEquals(Tomato.CALORIES_PER_100G,
                tomato.getCaloriesPer100g(), DELTA);
    }

    @Test
    void blankNameIsRejected() {
        assertThrows(IllegalArgumentException.class,
                () -> new TestVegetable(" ", 10, 10));
        assertThrows(IllegalArgumentException.class,
                () -> new TestVegetable(null, 10, 10));
    }

    @Test
    void nonPositiveWeightIsRejected() {
        assertThrows(IllegalArgumentException.class, () -> new Carrot(0));
        assertThrows(IllegalArgumentException.class, () -> new Carrot(-5));
    }

    @Test
    void negativeCaloriesAreRejected() {
        assertThrows(IllegalArgumentException.class,
                () -> new TestVegetable("x", 10, -1));
    }

    @Test
    void equalsAndHashCodeDependOnTypeAndValues() {
        Vegetable carrot = new Carrot(100);

        assertEquals(carrot, carrot);
        assertEquals(carrot, new Carrot(100));
        assertEquals(carrot.hashCode(), new Carrot(100).hashCode());
        assertNotEquals(carrot, new Carrot(101));
        assertNotEquals(carrot, new TestVegetable("Carrot", 100,
                Carrot.CALORIES_PER_100G));
        assertNotEquals(carrot, new TestVegetable("Other", 100,
                Carrot.CALORIES_PER_100G));
        assertNotEquals(new TestVegetable("a", 1, 1),
                new TestVegetable("a", 1, 2));
        assertNotEquals(new TestVegetable("a", 1, 1),
                new TestVegetable("b", 1, 1));
        assertNotEquals(carrot, null);
        assertNotEquals(carrot, "Carrot");
    }

    @Test
    void toStringContainsNameCategoryWeightAndCalories() {
        assertEquals("Tomato (fruit): 200 g, 36.0 kcal",
                new Tomato(200).toString());
    }

    /** Minimal vegetable used to test the base class. */
    private static final class TestVegetable extends Vegetable {

        TestVegetable(String name, double weight, double calories) {
            super(name, weight, calories);
        }

        @Override
        public String prepare() {
            return "test";
        }

        @Override
        public String getCategory() {
            return "test";
        }
    }
}
