package ua.chef.service;

import java.util.Objects;

import ua.chef.factory.VegetableFactory;
import ua.chef.model.Recipe;
import ua.chef.model.Salad;

/**
 * Chef cooks salads according to recipes.
 */
public class Chef {

    private final String name;
    private final VegetableFactory factory;

    /**
     * Creates a chef.
     *
     * @param name    chef name
     * @param factory factory used to get vegetables
     */
    public Chef(String name, VegetableFactory factory) {
        this.name = Objects.requireNonNull(name, "name");
        this.factory = Objects.requireNonNull(factory, "factory");
    }

    public String getName() {
        return name;
    }

    /**
     * Cooks a salad by the recipe.
     *
     * @param recipe recipe, not null
     * @return cooked salad
     */
    public Salad cook(Recipe recipe) {
        Objects.requireNonNull(recipe, "recipe");
        Salad salad = new Salad(recipe.name());
        for (Recipe.Item item : recipe.items()) {
            salad.addIngredient(factory.create(item.type(), item.weight()));
        }
        return salad;
    }
}
