package ua.chef.model.vegetable;

/**
 * Vegetable whose edible part is the fruit of the plant
 * (tomato, cucumber, pepper...).
 */
public abstract class FruitVegetable extends Vegetable {

    private final boolean hasInedibleSeeds;

    protected FruitVegetable(String name, double weight,
            double caloriesPer100g, boolean hasInedibleSeeds) {
        super(name, weight, caloriesPer100g);
        this.hasInedibleSeeds = hasInedibleSeeds;
    }

    public boolean isHasInedibleSeeds() {
        return hasInedibleSeeds;
    }

    @Override
    public String prepare() {
        return hasInedibleSeeds ? "remove seeds and slice" : "slice";
    }

    @Override
    public String getCategory() {
        return "fruit";
    }
}
