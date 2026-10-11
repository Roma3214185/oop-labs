package ua.chef.config;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.api.Test;

import ua.chef.model.Recipe;
import ua.chef.sort.SortCriterion;

class AppConfigTest {

    private final Recipe recipe = new Recipe("S", List.of());

    @Test
    void invalidRangeIsRejected() {
        assertThrows(IllegalArgumentException.class, () -> new AppConfig(
                "Chef", recipe, SortCriterion.NAME, 10, 5));
    }

    @Test
    void nullValuesAreRejected() {
        assertThrows(NullPointerException.class, () -> new AppConfig(
                null, recipe, SortCriterion.NAME, 0, 1));
        assertThrows(NullPointerException.class, () -> new AppConfig(
                "Chef", null, SortCriterion.NAME, 0, 1));
        assertThrows(NullPointerException.class, () -> new AppConfig(
                "Chef", recipe, null, 0, 1));
    }
}
