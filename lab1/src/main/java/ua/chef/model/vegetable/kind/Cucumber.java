package ua.chef.model.vegetable.kind;

import ua.chef.model.vegetable.FruitVegetable;

/**
 * Cucumber, a watery fruit vegetable.
 */
public class Cucumber extends FruitVegetable {

    /** Energy value in kcal per 100 g. */
    public static final double CALORIES_PER_100G = 15.0;

    public Cucumber(double weight) {
        super("Cucumber", weight, CALORIES_PER_100G, false);
    }
}
