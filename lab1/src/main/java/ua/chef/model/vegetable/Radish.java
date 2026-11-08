package ua.chef.model.vegetable;

/**
 * Radish, a crisp root vegetable.
 */
public class Radish extends Vegetable {

    /** Name shown to the user. */
    public static final String NAME = "Редиска";

    /** Energy value in kcal per 100 g. */
    public static final double CALORIES_PER_100G = 16.0;

    /** Price in UAH per kilogram. */
    public static final double PRICE_PER_KG = 90.0;

    /**
     * Creates a radish.
     *
     * @param weight weight in grams, positive
     */
    public Radish(double weight) {
        super(NAME, weight, CALORIES_PER_100G, PRICE_PER_KG);
    }

    @Override
    public String prepare() {
        return "нарізати тонкими кружальцями";
    }
}
