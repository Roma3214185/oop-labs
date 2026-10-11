package ua.chef.model.vegetable.kind;

import ua.chef.model.vegetable.RootVegetable;

/**
 * Beet, a root vegetable used in vinaigrette.
 */
public class Beet extends RootVegetable {

    /** Energy value in kcal per 100 g. */
    public static final double CALORIES_PER_100G = 43.0;

    public Beet(double weight) {
        super("Beet", weight, CALORIES_PER_100G, true);
    }
}
