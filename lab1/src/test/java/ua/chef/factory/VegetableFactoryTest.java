package ua.chef.factory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import ua.chef.model.vegetable.Vegetable;
import ua.chef.model.vegetable.kind.BellPepper;

class VegetableFactoryTest {

    private final VegetableFactory factory = new VegetableFactory();

    @ParameterizedTest
    @EnumSource(VegetableType.class)
    void createsEveryType(VegetableType type) {
        Vegetable vegetable = factory.create(type, 50);

        assertEquals(50, vegetable.getWeight());
        assertEquals(type, VegetableFactory.typeOf(vegetable.getName()));
    }

    @Test
    void createsConcreteClass() {
        assertInstanceOf(BellPepper.class,
                factory.create(VegetableType.BELL_PEPPER, 10));
    }

    @Test
    void typeNameIsNormalized() {
        assertEquals(VegetableType.BELL_PEPPER,
                VegetableFactory.typeOf("  bell pepper "));
        assertEquals(VegetableType.BELL_PEPPER,
                VegetableFactory.typeOf("Bell-Pepper"));
        assertEquals(VegetableType.ONION, VegetableFactory.typeOf("onion"));
    }

    @Test
    void unknownOrNullTypeIsRejected() {
        assertThrows(IllegalArgumentException.class,
                () -> VegetableFactory.typeOf("banana"));
        assertThrows(IllegalArgumentException.class,
                () -> VegetableFactory.typeOf(null));
        assertThrows(NullPointerException.class,
                () -> factory.create(null, 10));
    }
}
