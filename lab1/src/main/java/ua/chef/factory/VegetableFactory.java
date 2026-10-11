package ua.chef.factory;

import java.util.Locale;
import java.util.Objects;

import ua.chef.model.vegetable.Vegetable;

/**
 * Creates vegetables by their type.
 */
public class VegetableFactory {

    /**
     * Creates a vegetable of the given type.
     *
     * @param type   vegetable type, not null
     * @param weight weight in grams
     * @return new vegetable
     */
    public Vegetable create(VegetableType type, double weight) {
        return Objects.requireNonNull(type, "type").create(weight);
    }

    /**
     * Resolves a vegetable type by its name, ignoring case and treating
     * spaces and dashes as underscores.
     *
     * @param name type name, e.g. "bell pepper"
     * @return vegetable type
     * @throws IllegalArgumentException if the type is unknown
     */
    public static VegetableType typeOf(String name) {
        if (name == null) {
            throw new IllegalArgumentException("Vegetable type is null");
        }
        String key = name.trim()
                .replaceAll("[\\s-]+", "_")
                .toUpperCase(Locale.ROOT);
        try {
            return VegetableType.valueOf(key);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(
                    "Unknown vegetable type: " + name, e);
        }
    }
}
