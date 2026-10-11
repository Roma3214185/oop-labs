package ua.chef.factory;

import java.util.function.DoubleFunction;

import ua.chef.model.vegetable.Vegetable;
import ua.chef.model.vegetable.kind.BellPepper;
import ua.chef.model.vegetable.kind.Beet;
import ua.chef.model.vegetable.kind.Cabbage;
import ua.chef.model.vegetable.kind.Carrot;
import ua.chef.model.vegetable.kind.Cucumber;
import ua.chef.model.vegetable.kind.Garlic;
import ua.chef.model.vegetable.kind.Lettuce;
import ua.chef.model.vegetable.kind.Onion;
import ua.chef.model.vegetable.kind.Radish;
import ua.chef.model.vegetable.kind.Tomato;

/**
 * Supported vegetable types and their constructors.
 */
public enum VegetableType {
    CARROT(Carrot::new),
    BEET(Beet::new),
    RADISH(Radish::new),
    TOMATO(Tomato::new),
    CUCUMBER(Cucumber::new),
    BELL_PEPPER(BellPepper::new),
    LETTUCE(Lettuce::new),
    CABBAGE(Cabbage::new),
    ONION(Onion::new),
    GARLIC(Garlic::new);

    private final DoubleFunction<Vegetable> constructor;

    VegetableType(DoubleFunction<Vegetable> constructor) {
        this.constructor = constructor;
    }

    /**
     * Creates a vegetable of this type.
     *
     * @param weight weight in grams
     * @return new vegetable
     */
    Vegetable create(double weight) {
        return constructor.apply(weight);
    }
}
