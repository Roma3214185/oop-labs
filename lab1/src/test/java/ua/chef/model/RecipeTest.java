package ua.chef.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import ua.chef.factory.VegetableType;

class RecipeTest {

    @Test
    void itemsAreCopied() {
        List<Recipe.Item> items = new ArrayList<>();
        items.add(new Recipe.Item(VegetableType.TOMATO, 100));
        Recipe recipe = new Recipe("Salad", items);

        items.clear();

        assertEquals(1, recipe.items().size());
        assertThrows(UnsupportedOperationException.class,
                () -> recipe.items().clear());
    }

    @Test
    void invalidValuesAreRejected() {
        assertThrows(NullPointerException.class,
                () -> new Recipe(null, List.of()));
        assertThrows(NullPointerException.class,
                () -> new Recipe.Item(null, 10));
        assertThrows(IllegalArgumentException.class,
                () -> new Recipe.Item(VegetableType.ONION, 0));
    }
}
