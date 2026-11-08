package ua.chef.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ua.chef.model.vegetable.Carrot;
import ua.chef.model.vegetable.Tomato;
import ua.chef.model.vegetable.Vegetable;
import ua.chef.strategy.VegetableStrategy;

@ExtendWith(MockitoExtension.class)
class SaladTest {

    private static final double DELTA = 1e-9;

    @Mock
    private VegetableStrategy strategy;

    private Salad salad;
    private Vegetable tomato;
    private Vegetable carrot;

    @BeforeEach
    void setUp() {
        salad = new Salad();
        tomato = new Tomato(200);
        carrot = new Carrot(100);
    }

    @Test
    void newSaladIsEmpty() {
        assertTrue(salad.isEmpty());
        assertTrue(salad.getIngredients().isEmpty());
        assertEquals(0, salad.getTotalWeight(), DELTA);
        assertEquals(0, salad.getTotalCalories(), DELTA);
        assertEquals(0, salad.getTotalPrice(), DELTA);
    }

    @Test
    void keepsIngredientsInAddingOrder() {
        salad.add(tomato);
        salad.add(carrot);

        assertFalse(salad.isEmpty());
        assertEquals(List.of(tomato, carrot), salad.getIngredients());
    }

    @Test
    void rejectsNullIngredient() {
        assertThrows(NullPointerException.class, () -> salad.add(null));
    }

    @Test
    void ingredientsCannotBeModifiedOutside() {
        salad.add(tomato);

        assertThrows(UnsupportedOperationException.class,
                () -> salad.getIngredients().add(carrot));
    }

    @Test
    void calculatesTotals() {
        salad.add(tomato);
        salad.add(carrot);

        assertEquals(300, salad.getTotalWeight(), DELTA);
        // 200 g * 18 / 100 + 100 g * 41 / 100
        assertEquals(36 + 41, salad.getTotalCalories(), DELTA);
        // 0.2 kg * 80 + 0.1 kg * 25
        assertEquals(16 + 2.5, salad.getTotalPrice(), DELTA);
    }

    @Test
    void delegatesSelectionToStrategy() {
        salad.add(tomato);
        salad.add(carrot);
        List<Vegetable> expected = List.of(carrot);
        when(strategy.apply(List.of(tomato, carrot))).thenReturn(expected);

        List<Vegetable> result = salad.select(strategy);

        assertSame(expected, result);
        verify(strategy).apply(List.of(tomato, carrot));
    }

    @Test
    void rejectsNullStrategy() {
        assertThrows(NullPointerException.class, () -> salad.select(null));
    }
}
