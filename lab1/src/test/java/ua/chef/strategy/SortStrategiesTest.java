package ua.chef.strategy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ua.chef.model.vegetable.Vegetable;

class SortStrategiesTest {

    private Vegetable first;
    private Vegetable second;
    private Vegetable third;
    private List<Vegetable> vegetables;

    /**
     * Creates a mocked vegetable. Every parameter has a different order
     * among the three vegetables, so each strategy gives its own result.
     */
    private static Vegetable vegetable(double weight, double caloriesPer100g,
            double calories, double price) {
        Vegetable vegetable = mock(Vegetable.class);
        lenient().when(vegetable.getWeight()).thenReturn(weight);
        lenient().when(vegetable.getCaloriesPer100g())
                .thenReturn(caloriesPer100g);
        lenient().when(vegetable.getCalories()).thenReturn(calories);
        lenient().when(vegetable.getPrice()).thenReturn(price);
        return vegetable;
    }

    @BeforeEach
    void setUp() {
        first = vegetable(300, 20, 60, 5);
        second = vegetable(100, 40, 40, 15);
        third = vegetable(200, 10, 20, 10);
        vegetables = new ArrayList<>(List.of(first, second, third));
    }

    @Test
    void sortsByWeight() {
        assertEquals(List.of(second, third, first),
                new SortByWeightStrategy().apply(vegetables));
    }

    @Test
    void sortsByCaloriesPer100g() {
        assertEquals(List.of(third, first, second),
                new SortByCaloriesPer100gStrategy().apply(vegetables));
    }

    @Test
    void sortsByCaloriesInSalad() {
        assertEquals(List.of(third, second, first),
                new SortByCaloriesStrategy().apply(vegetables));
    }

    @Test
    void sortsByPrice() {
        assertEquals(List.of(first, third, second),
                new SortByPriceStrategy().apply(vegetables));
    }

    @Test
    void doesNotChangeSourceList() {
        List<Vegetable> sorted = new SortByWeightStrategy().apply(vegetables);

        assertNotSame(vegetables, sorted);
        assertEquals(List.of(first, second, third), vegetables);
    }

    @Test
    void sortsEmptyList() {
        assertTrue(new SortByPriceStrategy().apply(List.of()).isEmpty());
    }

    @Test
    void everyStrategyHasTitle() {
        List<VegetableStrategy> strategies = List.of(
                new SortByWeightStrategy(),
                new SortByCaloriesPer100gStrategy(),
                new SortByCaloriesStrategy(),
                new SortByPriceStrategy());

        for (VegetableStrategy strategy : strategies) {
            assertFalse(strategy.getTitle().isBlank());
        }
    }
}
