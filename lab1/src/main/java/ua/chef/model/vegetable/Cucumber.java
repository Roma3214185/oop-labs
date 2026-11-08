package ua.chef.model.vegetable;

/**
 * Cucumber, a watery fruit vegetable.
 */
public class Cucumber extends Vegetable {

    /** Name shown to the user. */
    public static final String NAME = "Огірок";

    /** Energy value in kcal per 100 g. */
    public static final double CALORIES_PER_100G = 15.0;

    /** Price in UAH per kilogram. */
    public static final double PRICE_PER_KG = 60.0;

    /**
     * Creates a cucumber.
     *
     * @param weight weight in grams, positive
     */
    public Cucumber(double weight) {
        super(NAME, weight, CALORIES_PER_100G, PRICE_PER_KG);
    }

    @Override
    public String prepare() {
        return "нарізати кружальцями";
    }
}
