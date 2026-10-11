package ua.chef.model.vegetable.kind;

import ua.chef.model.vegetable.FruitVegetable;

/**
 * Bell pepper, a fruit vegetable with inedible seeds.
 */
public class BellPepper extends FruitVegetable {

    /** Energy value in kcal per 100 g. */
    public static final double CALORIES_PER_100G = 27.0;

    public BellPepper(double weight) {
        super("Bell Pepper", weight, CALORIES_PER_100G, true);
    }
}
