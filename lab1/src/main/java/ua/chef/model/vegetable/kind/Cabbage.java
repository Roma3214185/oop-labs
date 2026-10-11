package ua.chef.model.vegetable.kind;

import ua.chef.model.vegetable.LeafyVegetable;

/**
 * Cabbage, leaves are shredded.
 */
public class Cabbage extends LeafyVegetable {

    /** Energy value in kcal per 100 g. */
    public static final double CALORIES_PER_100G = 25.0;

    public Cabbage(double weight) {
        super("Cabbage", weight, CALORIES_PER_100G, true);
    }
}
