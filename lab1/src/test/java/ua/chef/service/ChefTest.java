package ua.chef.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ua.chef.factory.VegetableFactory;
import ua.chef.factory.VegetableType;
import ua.chef.model.Recipe;
import ua.chef.model.Salad;
import ua.chef.model.vegetable.Vegetable;

@ExtendWith(MockitoExtension.class)
class ChefTest {

    @Mock
    private VegetableFactory factory;

    @Mock
    private Vegetable tomato;

    @Mock
    private Vegetable onion;

    @Test
    void cooksSaladFromRecipeUsingFactory() {
        when(factory.create(VegetableType.TOMATO, 100)).thenReturn(tomato);
        when(factory.create(VegetableType.ONION, 20)).thenReturn(onion);
        Recipe recipe = new Recipe("Simple", List.of(
                new Recipe.Item(VegetableType.TOMATO, 100),
                new Recipe.Item(VegetableType.ONION, 20)));

        Salad salad = new Chef("Jamie", factory).cook(recipe);

        assertEquals("Simple", salad.getName());
        assertEquals(List.of(tomato, onion), salad.getIngredients());
        verify(factory).create(VegetableType.TOMATO, 100);
        verify(factory).create(VegetableType.ONION, 20);
        verifyNoMoreInteractions(factory);
    }

    @Test
    void hasName() {
        assertEquals("Jamie", new Chef("Jamie", factory).getName());
    }

    @Test
    void nullArgumentsAreRejected() {
        Chef chef = new Chef("Jamie", factory);

        assertThrows(NullPointerException.class, () -> chef.cook(null));
        assertThrows(NullPointerException.class,
                () -> new Chef(null, factory));
        assertThrows(NullPointerException.class,
                () -> new Chef("Jamie", null));
    }
}
