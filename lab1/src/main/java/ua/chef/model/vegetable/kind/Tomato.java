package ua.chef.model.vegetable.kind;

import ua.chef.model.vegetable.FruitVegetable;

/**
 * Tomato, a juicy fruit vegetable.
 */
public class Tomato extends FruitVegetable {

    /** Energy value in kcal per 100 g. */
    public static final double CALORIES_PER_100G = 18.0;

    public Tomato(double weight) {
        super("Tomato", weight, CALORIES_PER_100G, false);
    }
}
