package ua.chef.model.vegetable;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

class VegetableTest {

    private static final double DELTA = 1e-9;

    /** Minimal vegetable used to test the base class. */
    private static class TestVegetable extends Vegetable {

        TestVegetable(String name, double weight, double calories,
                double price) {
            super(name, weight, calories, price);
        }

        @Override
        public String prepare() {
            return "test";
        }
    }

    @Test
    void storesCharacteristics() {
        Vegetable vegetable = new TestVegetable("Test", 250, 20, 40);

        assertEquals("Test", vegetable.getName());
        assertEquals(250, vegetable.getWeight(), DELTA);
        assertEquals(20, vegetable.getCaloriesPer100g(), DELTA);
        assertEquals(40, vegetable.getPricePerKg(), DELTA);
    }

    @Test
    void calculatesCaloriesForWeight() {
        Vegetable vegetable = new TestVegetable("Test", 250, 20, 40);

        assertEquals(50, vegetable.getCalories(), DELTA);
    }

    @Test
    void calculatesPriceForWeight() {
        Vegetable vegetable = new TestVegetable("Test", 250, 20, 40);

        assertEquals(10, vegetable.getPrice(), DELTA);
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"  "})
    void rejectsBlankName(String name) {
        assertThrows(IllegalArgumentException.class,
                () -> new TestVegetable(name, 100, 10, 10));
    }

    @ParameterizedTest
    @ValueSource(doubles = {0, -1, Double.NaN, Double.POSITIVE_INFINITY})
    void rejectsInvalidWeight(double weight) {
        assertThrows(IllegalArgumentException.class,
                () -> new TestVegetable("Test", weight, 10, 10));
    }

    @Test
    void rejectsNegativeCalories() {
        assertThrows(IllegalArgumentException.class,
                () -> new TestVegetable("Test", 100, -1, 10));
    }

    @Test
    void rejectsNegativePrice() {
        assertThrows(IllegalArgumentException.class,
                () -> new TestVegetable("Test", 100, 10, -1));
    }

    @Test
    void allowsZeroCaloriesAndPrice() {
        Vegetable vegetable = new TestVegetable("Test", 100, 0, 0);

        assertEquals(0, vegetable.getCalories(), DELTA);
        assertEquals(0, vegetable.getPrice(), DELTA);
    }

    @Test
    void equalVegetablesHaveSameHashCode() {
        Vegetable first = new Tomato(100);
        Vegetable second = new Tomato(100);

        assertEquals(first, first);
        assertEquals(first, second);
        assertEquals(first.hashCode(), second.hashCode());
    }

    @Test
    void differentVegetablesAreNotEqual() {
        Vegetable tomato = new Tomato(100);

        assertNotEquals(tomato, new Tomato(150));
        assertNotEquals(tomato, new Cucumber(100));
        assertNotEquals(tomato, null);
        assertNotEquals(tomato, "Tomato");
        assertNotEquals(new TestVegetable("A", 100, 10, 10),
                new TestVegetable("B", 100, 10, 10));
        assertNotEquals(new TestVegetable("A", 100, 10, 10),
                new TestVegetable("A", 100, 11, 10));
        assertNotEquals(new TestVegetable("A", 100, 10, 10),
                new TestVegetable("A", 100, 10, 11));
    }

    @Test
    void describesItself() {
        assertEquals("Помідор: 200 g, 36.0 kcal, 16.00 UAH",
                new Tomato(200).toString());
    }
}
