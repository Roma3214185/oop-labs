package ua.chef.menu;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

import ua.chef.model.Salad;
import ua.chef.model.vegetable.Carrot;
import ua.chef.model.vegetable.Cucumber;
import ua.chef.model.vegetable.Tomato;

class SaladMenuTest {

    private final ByteArrayOutputStream buffer = new ByteArrayOutputStream();

    private SaladMenu menu(String input) {
        return new SaladMenu(new Scanner(input),
                new PrintStream(buffer, true, StandardCharsets.UTF_8));
    }

    private String output() {
        return buffer.toString(StandardCharsets.UTF_8);
    }

    /** Salad of 200 g tomato, 150 g cucumber and 80 g carrot. */
    private static Salad sampleSalad() {
        Salad salad = new Salad();
        salad.add(new Tomato(200));
        salad.add(new Cucumber(150));
        salad.add(new Carrot(80));
        return salad;
    }

    /** Returns the vegetable names printed after the given title. */
    private List<String> namesAfter(String title) {
        String text = output();
        String rest = text.substring(text.indexOf(title));
        String table = rest.substring(0, rest.indexOf("Що показати?"));
        return Stream.of("Помідор", "Огірок", "Морква")
                .filter(table::contains)
                .sorted(Comparator.comparingInt(table::indexOf))
                .toList();
    }

    @Test
    void cooksSaladFromChosenVegetables() {
        Salad salad = menu("1\n200\n2\n150\n0\n").cookSalad();

        assertEquals(List.of(new Tomato(200), new Cucumber(150)),
                salad.getIngredients());
        assertTrue(output().contains(
                "Шеф: Помідор 200 г — нарізати часточками, додано в салат."));
        assertTrue(output().contains("1. Помідор"));
        assertTrue(output().contains("0. Finish"));
    }

    @Test
    void acceptsCommaAsDecimalSeparator() {
        Salad salad = menu("3\n50,5\n0\n").cookSalad();

        assertEquals(List.of(new Carrot(50.5)), salad.getIngredients());
    }

    @Test
    void asksAgainOnInvalidInput() {
        Salad salad = menu("abc\n9\n-1\n1\n-5\n1\n0\n1\n100\n0\n")
                .cookSalad();

        assertEquals(List.of(new Tomato(100)), salad.getIngredients());
        assertTrue(output().contains("Введіть число."));
        assertTrue(output().contains("Немає такого пункту."));
        assertTrue(output().contains("Маса має бути додатним числом."));
    }

    @Test
    void doesNotFinishEmptySalad() {
        Salad salad = menu("0\n2\n100\n0\n").cookSalad();

        assertTrue(output().contains(
                "Салат порожній, додайте хоча б один овоч."));
        assertEquals(List.of(new Cucumber(100)), salad.getIngredients());
    }

    @Test
    void stopsCookingWhenInputEndsOnChoice() {
        Salad salad = menu("1\n100\n").cookSalad();

        assertEquals(List.of(new Tomato(100)), salad.getIngredients());
    }

    @Test
    void stopsCookingWhenInputEndsOnWeight() {
        Salad salad = menu("1\n").cookSalad();

        assertTrue(salad.isEmpty());
    }

    @Test
    void printsSaladBeforeExploring() {
        menu("0\n").explore(sampleSalad());

        assertTrue(output().contains("Ваш салат:"));
        assertTrue(output().contains("Інгредієнтів: 3 | маса: 430 г"));
        assertTrue(output().contains("Смачного!"));
    }

    @Test
    void sortsByWeight() {
        menu("1\n0\n").explore(sampleSalad());

        assertEquals(List.of("Морква", "Огірок", "Помідор"),
                namesAfter("Сортування за масою в салаті:"));
    }

    @Test
    void sortsByCaloriesPer100g() {
        menu("2\n0\n").explore(sampleSalad());

        assertEquals(List.of("Огірок", "Помідор", "Морква"),
                namesAfter("Сортування за калорійністю на 100 г:"));
    }

    @Test
    void sortsByCaloriesInSalad() {
        menu("3\n0\n").explore(sampleSalad());

        assertEquals(List.of("Огірок", "Морква", "Помідор"),
                namesAfter("Сортування за калорійністю в салаті:"));
    }

    @Test
    void sortsByPrice() {
        menu("4\n0\n").explore(sampleSalad());

        assertEquals(List.of("Морква", "Огірок", "Помідор"),
                namesAfter("Сортування за вартістю в салаті:"));
    }

    @Test
    void findsVegetablesByCaloriesRange() {
        menu("5\n20\n33\n0\n").explore(sampleSalad());

        assertEquals(List.of("Огірок", "Морква"), namesAfter(
                "Овочі з калорійністю в салаті від 20.0 до 33.0 ккал:"));
    }

    @Test
    void reportsInvalidRange() {
        menu("5\n50\n10\n0\n").explore(sampleSalad());

        assertTrue(output().contains("Невірний діапазон"));
    }

    @Test
    void reportsUnknownAnalysisItem() {
        menu("7\n0\n").explore(sampleSalad());

        assertTrue(output().contains("Немає такого пункту."));
    }

    @Test
    void exitsWhenInputEndsDuringRange() {
        menu("5\n").explore(sampleSalad());
        menu("5\n10\n").explore(sampleSalad());

        assertFalse(output().contains("Овочі з калорійністю"));
        assertTrue(output().contains("Смачного!"));
    }

    @Test
    void exitsWhenInputEnds() {
        menu("").explore(sampleSalad());

        assertTrue(output().contains("Смачного!"));
    }

    @Test
    void runsWholeScenario() {
        menu("1\n200\n0\n3\n0\n").run();

        assertTrue(output().contains("Шеф: Помідор 200 г"));
        assertTrue(output().contains("Ваш салат:"));
        assertTrue(output().contains("Сортування за калорійністю в салаті:"));
        assertTrue(output().contains("Смачного!"));
    }

    @Test
    void runEndsWhenNothingWasCooked() {
        menu("").run();

        assertTrue(output().contains("Салат так і не було приготовано."));
        assertFalse(output().contains("Ваш салат:"));
    }

    @Test
    void rejectsNullArguments() {
        PrintStream out = new PrintStream(buffer, true,
                StandardCharsets.UTF_8);
        Scanner in = new Scanner("");

        assertThrows(NullPointerException.class,
                () -> new SaladMenu(null, out));
        assertThrows(NullPointerException.class,
                () -> new SaladMenu(in, null));
    }
}
