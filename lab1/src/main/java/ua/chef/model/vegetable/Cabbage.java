package ua.chef.model.vegetable;

/**
 * Cabbage, a crunchy leafy vegetable.
 */
public class Cabbage extends Vegetable {

    /** Name shown to the user. */
    public static final String NAME = "Капуста";

    /** Energy value in kcal per 100 g. */
    public static final double CALORIES_PER_100G = 25.0;

    /** Price in UAH per kilogram. */
    public static final double PRICE_PER_KG = 15.0;

    /**
     * Creates a cabbage.
     *
     * @param weight weight in grams, positive
     */
    public Cabbage(double weight) {
        super(NAME, weight, CALORIES_PER_100G, PRICE_PER_KG);
    }

    @Override
    public String prepare() {
        return "дрібно нашаткувати";
    }
}
