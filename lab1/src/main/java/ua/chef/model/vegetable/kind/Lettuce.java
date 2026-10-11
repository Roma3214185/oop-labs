package ua.chef.model.vegetable.kind;

import ua.chef.model.vegetable.LeafyVegetable;

/**
 * Lettuce, leaves are torn by hand.
 */
public class Lettuce extends LeafyVegetable {

    /** Energy value in kcal per 100 g. */
    public static final double CALORIES_PER_100G = 15.0;

    public Lettuce(double weight) {
        super("Lettuce", weight, CALORIES_PER_100G, false);
    }
}
