package ua.chef.view;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ua.chef.model.Salad;
import ua.chef.model.vegetable.Cucumber;
import ua.chef.model.vegetable.Tomato;

class SaladPrinterTest {

    private ByteArrayOutputStream buffer;
    private SaladPrinter printer;

    @BeforeEach
    void setUp() {
        buffer = new ByteArrayOutputStream();
        printer = new SaladPrinter(
                new PrintStream(buffer, true, StandardCharsets.UTF_8));
    }

    private String output() {
        return buffer.toString(StandardCharsets.UTF_8);
    }

    @Test
    void printsTableOfVegetables() {
        printer.printVegetables("Title",
                List.of(new Tomato(200), new Cucumber(150)));

        String text = output();
        assertTrue(text.contains("Title:"));
        assertTrue(text.contains("Овоч"));
        assertTrue(text.matches(
                "(?s).*Помідор\\s+200\\s+18\\.0\\s+36\\.0\\s+16\\.00.*"));
        assertTrue(text.matches(
                "(?s).*Огірок\\s+150\\s+15\\.0\\s+22\\.5\\s+9\\.00.*"));
    }

    @Test
    void printsMessageForEmptyList() {
        printer.printVegetables("Title", List.of());

        assertTrue(output().contains("(нічого не знайдено)"));
        assertFalse(output().contains("Овоч"));
    }

    @Test
    void printsSummaryOfSalad() {
        Salad salad = mock(Salad.class);
        when(salad.getIngredients()).thenReturn(List.of(new Tomato(200)));
        when(salad.getTotalWeight()).thenReturn(430.0);
        when(salad.getTotalCalories()).thenReturn(91.3);
        when(salad.getTotalPrice()).thenReturn(27.0);

        printer.printSummary(salad);

        assertEquals("Інгредієнтів: 1 | маса: 430 г | калорійність: 91.3 ккал"
                + " | вартість: 27.00 грн" + System.lineSeparator(), output());
    }

    @Test
    void rejectsNullStream() {
        assertThrows(NullPointerException.class,
                () -> new SaladPrinter(null));
    }
}
