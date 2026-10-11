package ua.chef.view;

import java.io.PrintStream;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import ua.chef.model.Salad;
import ua.chef.model.vegetable.Vegetable;

/**
 * Prints salad information to a text stream.
 */
public class SaladPrinter {

    private final PrintStream out;

    /**
     * Creates a printer.
     *
     * @param out destination stream
     */
    public SaladPrinter(PrintStream out) {
        this.out = Objects.requireNonNull(out, "out");
    }

    /**
     * Prints a titled list of vegetables with preparation hints.
     *
     * @param title      section title
     * @param vegetables vegetables to print
     */
    public void printVegetables(String title, List<Vegetable> vegetables) {
        out.println(title);
        if (vegetables.isEmpty()) {
            out.println("  (none)");
        }
        for (Vegetable v : vegetables) {
            out.printf(Locale.ROOT, "  - %s -> %s%n", v, v.prepare());
        }
    }

    /**
     * Prints salad summary: total weight and calories.
     *
     * @param salad salad
     */
    public void printSummary(Salad salad) {
        out.printf(Locale.ROOT, "Salad \"%s\": %.0f g, %.1f kcal%n",
                salad.getName(), salad.getTotalWeight(),
                salad.getTotalCalories());
    }
}
