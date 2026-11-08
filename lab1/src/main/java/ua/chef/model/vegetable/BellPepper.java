package ua.chef.model.vegetable;

/**
 * Bell pepper, a sweet fruit vegetable with inedible seeds.
 */
public class BellPepper extends Vegetable {

    /** Name shown to the user. */
    public static final String NAME = "Болгарський перець";

    /** Energy value in kcal per 100 g. */
    public static final double CALORIES_PER_100G = 27.0;

    /** Price in UAH per kilogram. */
    public static final double PRICE_PER_KG = 120.0;

    /**
     * Creates a bell pepper.
     *
     * @param weight weight in grams, positive
     */
    public BellPepper(double weight) {
        super(NAME, weight, CALORIES_PER_100G, PRICE_PER_KG);
    }

    @Override
    public String prepare() {
        return "видалити насіння і нарізати соломкою";
    }
}
