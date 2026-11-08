package ua.chef.model.vegetable;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;

class VegetableTypeTest {

    private static final double DELTA = 1e-9;

    static Stream<Arguments> types() {
        return Stream.of(
                Arguments.of(VegetableType.TOMATO, Tomato.class,
                        Tomato.CALORIES_PER_100G, Tomato.PRICE_PER_KG),
                Arguments.of(VegetableType.CUCUMBER, Cucumber.class,
                        Cucumber.CALORIES_PER_100G, Cucumber.PRICE_PER_KG),
                Arguments.of(VegetableType.CARROT, Carrot.class,
                        Carrot.CALORIES_PER_100G, Carrot.PRICE_PER_KG),
                Arguments.of(VegetableType.BELL_PEPPER, BellPepper.class,
                        BellPepper.CALORIES_PER_100G,
                        BellPepper.PRICE_PER_KG),
                Arguments.of(VegetableType.ONION, Onion.class,
                        Onion.CALORIES_PER_100G, Onion.PRICE_PER_KG),
                Arguments.of(VegetableType.CABBAGE, Cabbage.class,
                        Cabbage.CALORIES_PER_100G, Cabbage.PRICE_PER_KG),
                Arguments.of(VegetableType.LETTUCE, Lettuce.class,
                        Lettuce.CALORIES_PER_100G, Lettuce.PRICE_PER_KG),
                Arguments.of(VegetableType.RADISH, Radish.class,
                        Radish.CALORIES_PER_100G, Radish.PRICE_PER_KG));
    }

    @ParameterizedTest
    @MethodSource("types")
    void createsVegetableOfMatchingClass(VegetableType type,
            Class<? extends Vegetable> expectedClass, double calories,
            double price) {
        Vegetable vegetable = type.create(150);

        assertInstanceOf(expectedClass, vegetable);
        assertEquals(type.getTitle(), vegetable.getName());
        assertEquals(150, vegetable.getWeight(), DELTA);
        assertEquals(calories, vegetable.getCaloriesPer100g(), DELTA);
        assertEquals(price, vegetable.getPricePerKg(), DELTA);
    }

    @ParameterizedTest
    @EnumSource(VegetableType.class)
    void everyVegetableKnowsHowToBePrepared(VegetableType type) {
        assertFalse(type.create(100).prepare().isBlank());
    }

    @ParameterizedTest
    @EnumSource(VegetableType.class)
    void everyTypeHasTitle(VegetableType type) {
        assertFalse(type.getTitle().isBlank());
    }
}
