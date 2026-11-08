package ua.chef.view;

import java.io.PrintStream;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import ua.chef.model.Salad;
import ua.chef.model.vegetable.Vegetable;

/**
 * Prints vegetables and salad information as text tables.
 */
public class SaladPrinter {

    private static final String HEADER_FORMAT = "%-20s %8s %10s %9s %10s%n";
    private static final String ROW_FORMAT = "%-20s %8.0f %10.1f %9.1f %10.2f%n";

    private final PrintStream out;

    /**
     * Creates the printer.
     *
     * @param out stream to print to, not null
     */
    public SaladPrinter(PrintStream out) {
        this.out = Objects.requireNonNull(out, "out");
    }

    /**
     * Prints a titled table of vegetables.
     *
     * @param title      table title
     * @param vegetables vegetables to print
     */
    public void printVegetables(String title, List<Vegetable> vegetables) {
        out.println();
        out.println(title + ":");
        if (vegetables.isEmpty()) {
            out.println("  (нічого не знайдено)");
            return;
        }
        out.printf(Locale.ROOT, HEADER_FORMAT,
                "Овоч", "Маса, г", "ккал/100г", "ккал", "Ціна, грн");
        for (Vegetable vegetable : vegetables) {
            out.printf(Locale.ROOT, ROW_FORMAT,
                    vegetable.getName(),
                    vegetable.getWeight(),
                    vegetable.getCaloriesPer100g(),
                    vegetable.getCalories(),
                    vegetable.getPrice());
        }
    }

    /**
     * Prints total weight, energy value and cost of the salad.
     *
     * @param salad salad to describe
     */
    public void printSummary(Salad salad) {
        out.printf(Locale.ROOT,
                "Інгредієнтів: %d | маса: %.0f г | калорійність: %.1f ккал"
                        + " | вартість: %.2f грн%n",
                salad.getIngredients().size(),
                salad.getTotalWeight(),
                salad.getTotalCalories(),
                salad.getTotalPrice());
    }
}
