package ua.chef;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.InOrder;

import ua.chef.config.AppConfig;
import ua.chef.config.ConfigLoader;
import ua.chef.factory.VegetableType;
import ua.chef.model.Recipe;
import ua.chef.model.Salad;
import ua.chef.model.vegetable.kind.Cucumber;
import ua.chef.model.vegetable.kind.Onion;
import ua.chef.model.vegetable.kind.Tomato;
import ua.chef.service.Chef;
import ua.chef.sort.SortCriterion;
import ua.chef.view.SaladPrinter;

class ChefApplicationTest {

    private final Recipe recipe = new Recipe("Mock salad", List.of(
            new Recipe.Item(VegetableType.TOMATO, 1)));

    @Test
    void runCooksSortsAndSearches() {
        Salad salad = new Salad("Mock salad");
        Tomato tomato = new Tomato(200);       // 36 kcal
        Cucumber cucumber = new Cucumber(100); // 15 kcal
        Onion onion = new Onion(10);           // 4 kcal
        salad.addIngredient(tomato);
        salad.addIngredient(cucumber);
        salad.addIngredient(onion);
        Chef chef = mock(Chef.class);
        when(chef.getName()).thenReturn("Mock");
        when(chef.cook(recipe)).thenReturn(salad);
        SaladPrinter printer = mock(SaladPrinter.class);
        AppConfig config = new AppConfig("Mock", recipe,
                SortCriterion.CALORIES, 10, 20);

        Salad result = new ChefApplication(config, printer).run(chef);

        assertEquals(List.of(onion, cucumber, tomato),
                result.getIngredients());
        InOrder order = inOrder(printer);
        order.verify(printer).printVegetables(eq("Chef Mock cooked:"),
                anyList());
        order.verify(printer).printSummary(salad);
        order.verify(printer).printVegetables("Sorted by CALORIES:",
                List.of(onion, cucumber, tomato));
        order.verify(printer).printVegetables("Calories in [10.0; 20.0]:",
                List.of(cucumber));
        verify(chef).cook(recipe);
    }

    @Test
    void staticRunUsesDefaultConfigWithoutArguments() {
        ConfigLoader loader = mock(ConfigLoader.class);
        when(loader.loadDefault()).thenReturn(new AppConfig("Mock", recipe,
                SortCriterion.NAME, 0, 100));
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();

        Salad salad = ChefApplication.run(new String[0], loader,
                new PrintStream(buffer, true, StandardCharsets.UTF_8));

        assertEquals("Mock salad", salad.getName());
        verify(loader).loadDefault();
        assertTrue(buffer.toString(StandardCharsets.UTF_8)
                .startsWith("Chef Mock cooked:"));
    }

    @Test
    void staticRunUsesFileFromArguments() {
        ConfigLoader loader = mock(ConfigLoader.class);
        Path path = Path.of("custom.properties");
        when(loader.load(path)).thenReturn(new AppConfig("Mock", recipe,
                SortCriterion.NAME, 0, 100));

        ChefApplication.run(new String[] {"custom.properties"}, loader,
                new PrintStream(new ByteArrayOutputStream(), true,
                        StandardCharsets.UTF_8));

        verify(loader).load(path);
    }

    @Test
    void mainPrintsReportForConfigFile(@TempDir Path dir)
            throws IOException {
        Path file = dir.resolve("app.properties");
        Files.writeString(file,
                "salad.name=Mini\nsalad.ingredients=tomato:100\n");
        PrintStream original = System.out;
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        System.setOut(new PrintStream(buffer, true, StandardCharsets.UTF_8));
        try {
            ChefApplication.main(new String[] {file.toString()});
        } finally {
            System.setOut(original);
        }

        assertTrue(buffer.toString(StandardCharsets.UTF_8)
                .contains("Salad \"Mini\": 100 g, 18.0 kcal"));
    }
}
