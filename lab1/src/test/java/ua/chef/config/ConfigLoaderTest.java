package ua.chef.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Properties;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import ua.chef.factory.VegetableType;
import ua.chef.model.Recipe;
import ua.chef.sort.SortCriterion;

class ConfigLoaderTest {

    private static final double DELTA = 1e-9;

    private final ConfigLoader loader = new ConfigLoader();

    @Test
    void loadsDefaultConfiguration() {
        AppConfig config = loader.loadDefault();

        assertEquals("Gordon", config.chefName());
        assertEquals("Spring salad", config.recipe().name());
        assertEquals(7, config.recipe().items().size());
        assertEquals(SortCriterion.CALORIES, config.sortCriterion());
        assertEquals(10, config.minCalories(), DELTA);
        assertEquals(30, config.maxCalories(), DELTA);
    }

    @Test
    void loadsFile(@TempDir Path dir) throws IOException {
        Path file = dir.resolve("salad.properties");
        Files.writeString(file, """
                chef.name=Олена
                salad.name=Vinaigrette
                salad.ingredients=beet:150, carrot:100, bell pepper:50
                sort.by=weight
                calories.min=5
                calories.max=60.5
                """, StandardCharsets.UTF_8);

        AppConfig config = loader.load(file);

        assertEquals("Олена", config.chefName());
        assertEquals(new Recipe("Vinaigrette", List.of(
                new Recipe.Item(VegetableType.BEET, 150),
                new Recipe.Item(VegetableType.CARROT, 100),
                new Recipe.Item(VegetableType.BELL_PEPPER, 50))),
                config.recipe());
        assertEquals(SortCriterion.WEIGHT, config.sortCriterion());
        assertEquals(60.5, config.maxCalories(), DELTA);
    }

    @Test
    void optionalPropertiesHaveDefaults() throws IOException {
        AppConfig config = loader.load(stream(
                "salad.name=S\nsalad.ingredients=onion:10"));

        assertEquals("Chef", config.chefName());
        assertEquals(SortCriterion.CALORIES, config.sortCriterion());
        assertEquals(0, config.minCalories(), DELTA);
        assertEquals(Double.MAX_VALUE, config.maxCalories(), DELTA);
    }

    @Test
    void missingRequiredPropertyIsReported() {
        Properties props = new Properties();
        props.setProperty(ConfigLoader.SALAD_NAME, "S");
        props.setProperty(ConfigLoader.INGREDIENTS, " ");

        IllegalArgumentException e = assertThrows(
                IllegalArgumentException.class, () -> loader.parse(props));
        assertEquals("Missing property: salad.ingredients", e.getMessage());
    }

    @Test
    void malformedIngredientIsReported() {
        assertThrows(IllegalArgumentException.class, () -> loader.load(
                stream("salad.name=S\nsalad.ingredients=onion")));
        assertThrows(IllegalArgumentException.class, () -> loader.load(
                stream("salad.name=S\nsalad.ingredients=onion:abc")));
    }

    @Test
    void missingFileIsReported(@TempDir Path dir) {
        assertThrows(UncheckedIOException.class,
                () -> loader.load(dir.resolve("absent.properties")));
    }

    @Test
    void missingResourceIsReported() {
        assertThrows(IllegalStateException.class,
                () -> loader.loadResource("/absent.properties"));
    }

    private static ByteArrayInputStream stream(String text) {
        return new ByteArrayInputStream(
                text.getBytes(StandardCharsets.UTF_8));
    }
}
