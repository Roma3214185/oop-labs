package ua.chef.model.vegetable;

import java.util.function.DoubleFunction;

/**
 * Catalog of vegetables the chef can put into a salad. Each type knows
 * its display name and how to create a vegetable of the given weight.
 */
public enum VegetableType {
    TOMATO(Tomato.NAME, Tomato::new),
    CUCUMBER(Cucumber.NAME, Cucumber::new),
    CARROT(Carrot.NAME, Carrot::new),
    BELL_PEPPER(BellPepper.NAME, BellPepper::new),
    ONION(Onion.NAME, Onion::new),
    CABBAGE(Cabbage.NAME, Cabbage::new),
    LETTUCE(Lettuce.NAME, Lettuce::new),
    RADISH(Radish.NAME, Radish::new);

    private final String title;
    private final DoubleFunction<Vegetable> constructor;

    VegetableType(String title, DoubleFunction<Vegetable> constructor) {
        this.title = title;
        this.constructor = constructor;
    }

    public String getTitle() {
        return title;
    }

    /**
     * Creates a vegetable of this type.
     *
     * @param weight weight in grams, positive
     * @return new vegetable
     */
    public Vegetable create(double weight) {
        return constructor.apply(weight);
    }
}
