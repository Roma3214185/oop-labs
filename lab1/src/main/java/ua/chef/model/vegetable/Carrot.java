package ua.chef.model.vegetable;

/**
 * Carrot, a sweet root vegetable.
 */
public class Carrot extends Vegetable {

    /** Name shown to the user. */
    public static final String NAME = "Морква";

    /** Energy value in kcal per 100 g. */
    public static final double CALORIES_PER_100G = 41.0;

    /** Price in UAH per kilogram. */
    public static final double PRICE_PER_KG = 25.0;

    /**
     * Creates a carrot.
     *
     * @param weight weight in grams, positive
     */
    public Carrot(double weight) {
        super(NAME, weight, CALORIES_PER_100G, PRICE_PER_KG);
    }

    @Override
    public String prepare() {
        return "почистити і натерти на тертці";
    }
}
