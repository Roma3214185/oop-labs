package ua.chef.view;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ua.chef.model.Salad;
import ua.chef.model.vegetable.kind.Cucumber;
import ua.chef.model.vegetable.kind.Tomato;

class SaladPrinterTest {

    private ByteArrayOutputStream buffer;
    private SaladPrinter printer;

    @BeforeEach
    void setUp() {
        buffer = new ByteArrayOutputStream();
        printer = new SaladPrinter(
                new PrintStream(buffer, true, StandardCharsets.UTF_8));
    }

    @Test
    void printsVegetablesWithPreparation() {
        printer.printVegetables("Title", List.of(new Tomato(100)));

        assertEquals(lines("Title",
                "  - Tomato (fruit): 100 g, 18.0 kcal -> slice"), output());
    }

    @Test
    void printsPlaceholderForEmptyList() {
        printer.printVegetables("Title", List.of());

        assertEquals(lines("Title", "  (none)"), output());
    }

    @Test
    void printsSummary() {
        Salad salad = new Salad("Summer");
        salad.addIngredient(new Tomato(100));
        salad.addIngredient(new Cucumber(100));

        printer.printSummary(salad);

        assertEquals(lines("Salad \"Summer\": 200 g, 33.0 kcal"), output());
    }

    @Test
    void nullStreamIsRejected() {
        assertThrows(NullPointerException.class,
                () -> new SaladPrinter(null));
    }

    private String output() {
        return buffer.toString(StandardCharsets.UTF_8);
    }

    private static String lines(String... lines) {
        return String.join(System.lineSeparator(), lines)
                + System.lineSeparator();
    }
}
