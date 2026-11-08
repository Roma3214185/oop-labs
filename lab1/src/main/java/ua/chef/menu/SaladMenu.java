package ua.chef.menu;

import java.io.PrintStream;
import java.util.Locale;
import java.util.Objects;
import java.util.Scanner;

import ua.chef.model.Salad;
import ua.chef.model.vegetable.Vegetable;
import ua.chef.model.vegetable.VegetableType;
import ua.chef.strategy.CaloriesRangeStrategy;
import ua.chef.strategy.SortByCaloriesPer100gStrategy;
import ua.chef.strategy.SortByCaloriesStrategy;
import ua.chef.strategy.SortByPriceStrategy;
import ua.chef.strategy.SortByWeightStrategy;
import ua.chef.strategy.VegetableStrategy;
import ua.chef.view.SaladPrinter;

/**
 * Console menu. First the user chooses vegetables for the salad, then
 * explores the cooked salad: sorts the ingredients and searches them by
 * calories. The menu only reads input and prints results; all the logic
 * lives in the model and the strategies.
 */
public class SaladMenu {

    private static final int FINISH = 0;
    private static final int EXIT = 0;
    private static final int SORT_BY_WEIGHT = 1;
    private static final int SORT_BY_CALORIES_PER_100G = 2;
    private static final int SORT_BY_CALORIES = 3;
    private static final int SORT_BY_PRICE = 4;
    private static final int FIND_BY_CALORIES = 5;

    private final Scanner in;
    private final PrintStream out;
    private final SaladPrinter printer;

    /**
     * Creates the menu.
     *
     * @param in  source of the user input, not null
     * @param out stream to print to, not null
     */
    public SaladMenu(Scanner in, PrintStream out) {
        this.in = Objects.requireNonNull(in, "in");
        this.out = Objects.requireNonNull(out, "out");
        this.printer = new SaladPrinter(out);
    }

    /**
     * Runs the whole scenario: cooking and exploring the salad.
     */
    public void run() {
        Salad salad = cookSalad();
        if (salad.isEmpty()) {
            out.println("Салат так і не було приготовано.");
            return;
        }
        explore(salad);
    }

    /**
     * Asks the user which vegetables to add until "finish" is chosen
     * or the input ends.
     *
     * @return cooked salad, may be empty only if the input ended
     */
    public Salad cookSalad() {
        Salad salad = new Salad();
        VegetableType[] types = VegetableType.values();
        out.println("=== Шеф-кухар: готуємо салат ===");
        while (true) {
            printVegetableMenu(types);
            Double choice = readNumber("Ваш вибір: ");
            if (choice == null) {
                return salad;
            }
            int item = choice.intValue();
            if (item == FINISH) {
                if (!salad.isEmpty()) {
                    return salad;
                }
                out.println("Салат порожній, додайте хоча б один овоч.");
            } else if (item < 1 || item > types.length) {
                out.println("Немає такого пункту.");
            } else {
                Double weight = readNumber("Маса, г: ");
                if (weight == null) {
                    return salad;
                }
                addVegetable(salad, types[item - 1], weight);
            }
        }
    }

    /**
     * Prints the salad and lets the user sort and search its ingredients
     * until "exit" is chosen or the input ends.
     *
     * @param salad cooked salad
     */
    public void explore(Salad salad) {
        printer.printVegetables("Ваш салат", salad.getIngredients());
        printer.printSummary(salad);
        while (true) {
            printAnalysisMenu();
            Double choice = readNumber("Ваш вибір: ");
            if (choice == null || choice.intValue() == EXIT) {
                out.println("Смачного!");
                return;
            }
            switch (choice.intValue()) {
                case SORT_BY_WEIGHT -> show(salad, new SortByWeightStrategy());
                case SORT_BY_CALORIES_PER_100G ->
                        show(salad, new SortByCaloriesPer100gStrategy());
                case SORT_BY_CALORIES ->
                        show(salad, new SortByCaloriesStrategy());
                case SORT_BY_PRICE -> show(salad, new SortByPriceStrategy());
                case FIND_BY_CALORIES -> findByCalories(salad);
                default -> out.println("Немає такого пункту.");
            }
        }
    }

    private void addVegetable(Salad salad, VegetableType type, double weight) {
        Vegetable vegetable;
        try {
            vegetable = type.create(weight);
        } catch (IllegalArgumentException e) {
            out.println("Маса має бути додатним числом.");
            return;
        }
        salad.add(vegetable);
        out.printf(Locale.ROOT, "Шеф: %s %.0f г — %s, додано в салат.%n",
                vegetable.getName(), vegetable.getWeight(),
                vegetable.prepare());
    }

    private void findByCalories(Salad salad) {
        Double min = readNumber("Мінімум, ккал: ");
        if (min == null) {
            return;
        }
        Double max = readNumber("Максимум, ккал: ");
        if (max == null) {
            return;
        }
        try {
            show(salad, new CaloriesRangeStrategy(min, max));
        } catch (IllegalArgumentException e) {
            out.println("Невірний діапазон: мінімум має бути не менше 0"
                    + " і не більше максимуму.");
        }
    }

    private void show(Salad salad, VegetableStrategy strategy) {
        printer.printVegetables(strategy.getTitle(), salad.select(strategy));
    }

    private void printVegetableMenu(VegetableType[] types) {
        out.println();
        out.println("Що додати в салат?");
        for (int i = 0; i < types.length; i++) {
            out.println("  " + (i + 1) + ". " + types[i].getTitle());
        }
        out.println("  " + FINISH + ". Finish — салат готовий");
    }

    private void printAnalysisMenu() {
        out.println();
        out.println("Що показати?");
        out.println("  " + SORT_BY_WEIGHT + ". Сортувати за масою в салаті");
        out.println("  " + SORT_BY_CALORIES_PER_100G
                + ". Сортувати за калорійністю на 100 г");
        out.println("  " + SORT_BY_CALORIES
                + ". Сортувати за калорійністю в салаті");
        out.println("  " + SORT_BY_PRICE + ". Сортувати за вартістю");
        out.println("  " + FIND_BY_CALORIES
                + ". Знайти овочі в діапазоні калорійності");
        out.println("  " + EXIT + ". Вихід");
    }

    /**
     * Reads a number, asking again on invalid input. Comma is accepted
     * as the decimal separator.
     *
     * @param prompt text shown before the input
     * @return the number or {@code null} if the input ended
     */
    private Double readNumber(String prompt) {
        while (true) {
            out.print(prompt);
            if (!in.hasNextLine()) {
                out.println();
                return null;
            }
            String line = in.nextLine().trim().replace(',', '.');
            try {
                return Double.parseDouble(line);
            } catch (NumberFormatException e) {
                out.println("Введіть число.");
            }
        }
    }
}
