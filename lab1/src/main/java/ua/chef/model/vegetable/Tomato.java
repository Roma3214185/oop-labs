package ua.chef.model.vegetable;

/**
 * Tomato, a juicy fruit vegetable.
 */
public class Tomato extends Vegetable {

    /** Name shown to the user. */
    public static final String NAME = "Помідор";

    /** Energy value in kcal per 100 g. */
    public static final double CALORIES_PER_100G = 18.0;

    /** Price in UAH per kilogram. */
    public static final double PRICE_PER_KG = 80.0;

    /**
     * Creates a tomato.
     *
     * @param weight weight in grams, positive
     */
    public Tomato(double weight) {
        super(NAME, weight, CALORIES_PER_100G, PRICE_PER_KG);
    }

    @Override
    public String prepare() {
        return "нарізати часточками";
    }
}
