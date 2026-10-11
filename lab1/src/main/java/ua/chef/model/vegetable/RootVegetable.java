package ua.chef.model.vegetable;

/**
 * Vegetable whose edible part is the root (carrot, beet, potato...).
 */
public abstract class RootVegetable extends Vegetable {

    private final boolean needsPeeling;

    protected RootVegetable(String name, double weight,
            double caloriesPer100g, boolean needsPeeling) {
        super(name, weight, caloriesPer100g);
        this.needsPeeling = needsPeeling;
    }

    public boolean isNeedsPeeling() {
        return needsPeeling;
    }

    @Override
    public String prepare() {
        return needsPeeling ? "peel and grate" : "wash and grate";
    }

    @Override
    public String getCategory() {
        return "root";
    }
}
