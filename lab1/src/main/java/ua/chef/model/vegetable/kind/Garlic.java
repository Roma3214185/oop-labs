package ua.chef.model.vegetable.kind;

import ua.chef.model.vegetable.BulbVegetable;

/**
 * Garlic, a very pungent bulb vegetable.
 */
public class Garlic extends BulbVegetable {

    /** Energy value in kcal per 100 g. */
    public static final double CALORIES_PER_100G = 149.0;

    public Garlic(double weight) {
        super("Garlic", weight, CALORIES_PER_100G, 5);
    }
}
