package ua.chef.model.vegetable;

/**
 * Vegetable whose edible part is the leaves (lettuce, cabbage...).
 */
public abstract class LeafyVegetable extends Vegetable {

    private final boolean shredded;

    protected LeafyVegetable(String name, double weight,
            double caloriesPer100g, boolean shredded) {
        super(name, weight, caloriesPer100g);
        this.shredded = shredded;
    }

    public boolean isShredded() {
        return shredded;
    }

    @Override
    public String prepare() {
        return shredded ? "wash and shred" : "wash and tear by hand";
    }

    @Override
    public String getCategory() {
        return "leafy";
    }
}
