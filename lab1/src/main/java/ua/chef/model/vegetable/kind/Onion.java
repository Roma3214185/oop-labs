package ua.chef.model.vegetable.kind;

import ua.chef.model.vegetable.BulbVegetable;

/**
 * Onion, a pungent bulb vegetable.
 */
public class Onion extends BulbVegetable {

    /** Energy value in kcal per 100 g. */
    public static final double CALORIES_PER_100G = 40.0;

    public Onion(double weight) {
        super("Onion", weight, CALORIES_PER_100G, 4);
    }
}
