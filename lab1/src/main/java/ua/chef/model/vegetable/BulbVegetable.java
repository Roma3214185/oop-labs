package ua.chef.model.vegetable;

/**
 * Vegetable whose edible part is the bulb (onion, garlic...).
 */
public abstract class BulbVegetable extends Vegetable {

    /** Pungency level from which the bulb is soaked before serving. */
    private static final int SOAK_PUNGENCY = 3;
    private static final int MIN_PUNGENCY = 1;
    private static final int MAX_PUNGENCY = 5;

    private final int pungency;

    protected BulbVegetable(String name, double weight,
            double caloriesPer100g, int pungency) {
        super(name, weight, caloriesPer100g);
        if (pungency < MIN_PUNGENCY || pungency > MAX_PUNGENCY) {
            throw new IllegalArgumentException(
                    "Pungency must be in [1, 5]: " + pungency);
        }
        this.pungency = pungency;
    }

    public int getPungency() {
        return pungency;
    }

    @Override
    public String prepare() {
        return pungency >= SOAK_PUNGENCY
                ? "finely chop and soak in cold water"
                : "finely chop";
    }

    @Override
    public String getCategory() {
        return "bulb";
    }
}
