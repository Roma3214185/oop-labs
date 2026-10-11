package ua.chef;

import java.io.PrintStream;
import java.nio.file.Path;
import java.util.List;
import java.util.Locale;

import ua.chef.config.AppConfig;
import ua.chef.config.ConfigLoader;
import ua.chef.factory.VegetableFactory;
import ua.chef.model.Salad;
import ua.chef.model.vegetable.Vegetable;
import ua.chef.service.Chef;
import ua.chef.view.SaladPrinter;

/**
 * Entry point: the chef cooks a salad described in the configuration
 * file, counts its calories, sorts the ingredients and finds the ones
 * in the configured calorie range.
 */
public class ChefApplication {

    private final AppConfig config;
    private final SaladPrinter printer;

    /**
     * Creates the application.
     *
     * @param config  initialization parameters
     * @param printer output
     */
    public ChefApplication(AppConfig config, SaladPrinter printer) {
        this.config = config;
        this.printer = printer;
    }

    /**
     * Runs the scenario.
     *
     * @param chef chef that cooks the salad
     * @return cooked salad with sorted ingredients
     */
    public Salad run(Chef chef) {
        Salad salad = chef.cook(config.recipe());
        printer.printVegetables(
                "Chef " + chef.getName() + " cooked:",
                salad.getIngredients());
        printer.printSummary(salad);

        salad.sortIngredients(config.sortCriterion().comparator());
        printer.printVegetables(
                "Sorted by " + config.sortCriterion() + ":",
                salad.getIngredients());

        List<Vegetable> found = salad.findByCalories(
                config.minCalories(), config.maxCalories());
        printer.printVegetables(
                String.format(Locale.ROOT, "Calories in [%.1f; %.1f]:",
                        config.minCalories(), config.maxCalories()),
                found);
        return salad;
    }

    /**
     * Starts the application.
     *
     * @param args optional path to a properties file
     */
    public static void main(String[] args) {
        run(args, new ConfigLoader(), System.out);
    }

    static Salad run(String[] args, ConfigLoader loader, PrintStream out) {
        AppConfig config = args.length > 0
                ? loader.load(Path.of(args[0]))
                : loader.loadDefault();
        Chef chef = new Chef(config.chefName(), new VegetableFactory());
        return new ChefApplication(config, new SaladPrinter(out)).run(chef);
    }
}
