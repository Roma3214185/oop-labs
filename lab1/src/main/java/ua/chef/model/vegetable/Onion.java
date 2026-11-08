package ua.chef.model.vegetable;

/**
 * Onion, a spicy bulb vegetable.
 */
public class Onion extends Vegetable {

    /** Name shown to the user. */
    public static final String NAME = "Цибуля";

    /** Energy value in kcal per 100 g. */
    public static final double CALORIES_PER_100G = 40.0;

    /** Price in UAH per kilogram. */
    public static final double PRICE_PER_KG = 20.0;

    /**
     * Creates an onion.
     *
     * @param weight weight in grams, positive
     */
    public Onion(double weight) {
        super(NAME, weight, CALORIES_PER_100G, PRICE_PER_KG);
    }

    @Override
    public String prepare() {
        return "почистити і нарізати півкільцями";
    }
}
