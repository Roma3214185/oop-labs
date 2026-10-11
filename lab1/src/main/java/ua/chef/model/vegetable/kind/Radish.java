package ua.chef.model.vegetable.kind;

import ua.chef.model.vegetable.RootVegetable;

/**
 * Radish, a root vegetable eaten with the skin.
 */
public class Radish extends RootVegetable {

    /** Energy value in kcal per 100 g. */
    public static final double CALORIES_PER_100G = 16.0;

    public Radish(double weight) {
        super("Radish", weight, CALORIES_PER_100G, false);
    }
}
