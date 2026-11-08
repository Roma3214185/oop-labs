package ua.chef.model.vegetable;

/**
 * Lettuce, a tender leafy vegetable.
 */
public class Lettuce extends Vegetable {

    /** Name shown to the user. */
    public static final String NAME = "Салат латук";

    /** Energy value in kcal per 100 g. */
    public static final double CALORIES_PER_100G = 15.0;

    /** Price in UAH per kilogram. */
    public static final double PRICE_PER_KG = 200.0;

    /**
     * Creates a lettuce.
     *
     * @param weight weight in grams, positive
     */
    public Lettuce(double weight) {
        super(NAME, weight, CALORIES_PER_100G, PRICE_PER_KG);
    }

    @Override
    public String prepare() {
        return "помити і порвати руками";
    }
}
