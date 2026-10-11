package ua.chef.model.vegetable;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

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

class VegetableCategoryTest {

    @Test
    void rootVegetablesArePeeledWhenNeeded() {
        RootVegetable carrot = new Carrot(10);
        RootVegetable radish = new Radish(10);

        assertTrue(carrot.isNeedsPeeling());
        assertFalse(radish.isNeedsPeeling());
        assertEquals("peel and grate", carrot.prepare());
        assertEquals("peel and grate", new Beet(10).prepare());
        assertEquals("wash and grate", radish.prepare());
        assertEquals("root", carrot.getCategory());
    }

    @Test
    void fruitVegetablesHaveSeedsRemovedWhenInedible() {
        FruitVegetable pepper = new BellPepper(10);
        FruitVegetable tomato = new Tomato(10);

        assertTrue(pepper.isHasInedibleSeeds());
        assertFalse(tomato.isHasInedibleSeeds());
        assertEquals("remove seeds and slice", pepper.prepare());
        assertEquals("slice", tomato.prepare());
        assertEquals("slice", new Cucumber(10).prepare());
        assertEquals("fruit", tomato.getCategory());
        assertEquals("Bell Pepper", pepper.getName());
    }

    @Test
    void leafyVegetablesAreShreddedOrTorn() {
        LeafyVegetable cabbage = new Cabbage(10);
        LeafyVegetable lettuce = new Lettuce(10);

        assertTrue(cabbage.isShredded());
        assertFalse(lettuce.isShredded());
        assertEquals("wash and shred", cabbage.prepare());
        assertEquals("wash and tear by hand", lettuce.prepare());
        assertEquals("leafy", lettuce.getCategory());
    }

    @Test
    void pungentBulbsAreSoaked() {
        BulbVegetable onion = new Onion(10);

        assertEquals(4, onion.getPungency());
        assertEquals(5, new Garlic(10).getPungency());
        assertEquals("finely chop and soak in cold water", onion.prepare());
        assertEquals("finely chop", new MildBulb(1).prepare());
        assertEquals("bulb", onion.getCategory());
    }

    @Test
    void pungencyOutOfRangeIsRejected() {
        assertThrows(IllegalArgumentException.class, () -> new MildBulb(0));
        assertThrows(IllegalArgumentException.class, () -> new MildBulb(6));
    }

    @Test
    void polymorphicCallUsesSubclassBehaviour() {
        Vegetable vegetable = new Onion(10);

        assertInstanceOf(BulbVegetable.class, vegetable);
        assertEquals("bulb", vegetable.getCategory());
    }

    /** Bulb with configurable pungency. */
    private static final class MildBulb extends BulbVegetable {

        MildBulb(int pungency) {
            super("Shallot", 10, 72, pungency);
        }
    }
}
