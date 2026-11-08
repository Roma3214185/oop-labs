package ua.chef.strategy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;

import ua.chef.model.vegetable.Vegetable;

class CaloriesRangeStrategyTest {

    private static Vegetable withCalories(double calories) {
        Vegetable vegetable = mock(Vegetable.class);
        when(vegetable.getCalories()).thenReturn(calories);
        return vegetable;
    }

    @Test
    void findsVegetablesInRangeIncludingBounds() {
        Vegetable below = withCalories(9.9);
        Vegetable atMin = withCalories(10);
        Vegetable inside = withCalories(25);
        Vegetable atMax = withCalories(40);
        Vegetable above = withCalories(40.1);

        List<Vegetable> found = new CaloriesRangeStrategy(10, 40)
                .apply(List.of(below, atMin, inside, atMax, above));

        assertEquals(List.of(atMin, inside, atMax), found);
    }

    @Test
    void returnsEmptyListWhenNothingMatches() {
        List<Vegetable> found = new CaloriesRangeStrategy(100, 200)
                .apply(List.of(withCalories(5)));

        assertTrue(found.isEmpty());
    }

    @Test
    void allowsSinglePointRange() {
        Vegetable vegetable = withCalories(15);

        assertEquals(List.of(vegetable),
                new CaloriesRangeStrategy(15, 15).apply(List.of(vegetable)));
    }

    @Test
    void storesBounds() {
        CaloriesRangeStrategy strategy = new CaloriesRangeStrategy(1.5, 30);

        assertEquals(1.5, strategy.getMin());
        assertEquals(30, strategy.getMax());
    }

    @Test
    void rejectsMinGreaterThanMax() {
        assertThrows(IllegalArgumentException.class,
                () -> new CaloriesRangeStrategy(50, 10));
    }

    @Test
    void rejectsNegativeMin() {
        assertThrows(IllegalArgumentException.class,
                () -> new CaloriesRangeStrategy(-1, 10));
    }

    @Test
    void titleContainsBounds() {
        assertEquals("Овочі з калорійністю в салаті від 10.0 до 40.5 ккал",
                new CaloriesRangeStrategy(10, 40.5).getTitle());
    }
}
