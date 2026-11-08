package ua.chef;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import ua.chef.menu.SaladMenu;

/**
 * Entry point of the "Chef" application: the user cooks a salad from
 * vegetables, learns its calories and cost, sorts the ingredients and
 * finds the ones in a calorie range.
 */
public final class ChefApplication {

    private ChefApplication() {
    }

    /**
     * Starts the console menu.
     *
     * @param args not used
     */
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in, StandardCharsets.UTF_8);
        PrintStream out = new PrintStream(System.out, true,
                StandardCharsets.UTF_8);
        new SaladMenu(in, out).run();
    }
}
