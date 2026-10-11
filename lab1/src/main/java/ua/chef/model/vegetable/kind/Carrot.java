package ua.chef.model.vegetable.kind;

import ua.chef.model.vegetable.RootVegetable;

/**
 * Carrot, a sweet root vegetable.
 */
public class Carrot extends RootVegetable {

    /** Energy value in kcal per 100 g. */
    public static final double CALORIES_PER_100G = 41.0;

    public Carrot(double weight) {
        super("Carrot", weight, CALORIES_PER_100G, true);
    }
}
