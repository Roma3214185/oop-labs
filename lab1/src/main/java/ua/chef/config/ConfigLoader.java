package ua.chef.config;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

import ua.chef.factory.VegetableFactory;
import ua.chef.model.Recipe;
import ua.chef.sort.SortCriterion;

/**
 * Loads {@link AppConfig} from a properties file.
 *
 * <p>Ingredients are given as a comma separated list of
 * {@code type:weight} pairs, e.g. {@code tomato:150, onion:40}.
 */
public class ConfigLoader {

    /** Default configuration bundled with the application. */
    public static final String DEFAULT_RESOURCE = "/salad.properties";

    static final String CHEF_NAME = "chef.name";
    static final String SALAD_NAME = "salad.name";
    static final String INGREDIENTS = "salad.ingredients";
    static final String SORT_BY = "sort.by";
    static final String MIN_CALORIES = "calories.min";
    static final String MAX_CALORIES = "calories.max";

    private static final String ITEM_SEPARATOR = ",";
    private static final String WEIGHT_SEPARATOR = ":";

    /**
     * Loads configuration from a file.
     *
     * @param path path to the properties file
     * @return configuration
     */
    public AppConfig load(Path path) {
        try (InputStream in = Files.newInputStream(path)) {
            return load(in);
        } catch (IOException e) {
            throw new UncheckedIOException("Cannot read " + path, e);
        }
    }

    /**
     * Loads the default configuration from the classpath.
     *
     * @return configuration
     */
    public AppConfig loadDefault() {
        return loadResource(DEFAULT_RESOURCE);
    }

    /**
     * Loads configuration from a classpath resource.
     *
     * @param resource absolute resource name
     * @return configuration
     */
    AppConfig loadResource(String resource) {
        try (InputStream in =
                ConfigLoader.class.getResourceAsStream(resource)) {
            if (in == null) {
                throw new IllegalStateException(
                        "Resource not found: " + resource);
            }
            return load(in);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    /**
     * Loads configuration from a stream.
     *
     * @param in stream with properties in UTF-8
     * @return configuration
     * @throws IOException if the stream cannot be read
     */
    public AppConfig load(InputStream in) throws IOException {
        Properties props = new Properties();
        try (Reader reader =
                new InputStreamReader(in, StandardCharsets.UTF_8)) {
            props.load(reader);
        }
        return parse(props);
    }

    /**
     * Converts properties to configuration.
     *
     * @param props properties
     * @return configuration
     */
    AppConfig parse(Properties props) {
        Recipe recipe = new Recipe(require(props, SALAD_NAME),
                parseItems(require(props, INGREDIENTS)));
        SortCriterion criterion = SortCriterion.valueOf(
                props.getProperty(SORT_BY, SortCriterion.CALORIES.name())
                        .trim().toUpperCase(Locale.ROOT));
        return new AppConfig(
                props.getProperty(CHEF_NAME, "Chef").trim(),
                recipe,
                criterion,
                parseNumber(props.getProperty(MIN_CALORIES, "0")),
                parseNumber(props.getProperty(MAX_CALORIES,
                        String.valueOf(Double.MAX_VALUE))));
    }

    private static List<Recipe.Item> parseItems(String value) {
        List<Recipe.Item> items = new ArrayList<>();
        for (String token : value.split(ITEM_SEPARATOR)) {
            String[] parts = token.split(WEIGHT_SEPARATOR);
            if (parts.length != 2) {
                throw new IllegalArgumentException(
                        "Ingredient must look like type:weight, got '"
                        + token.trim() + "'");
            }
            items.add(new Recipe.Item(VegetableFactory.typeOf(parts[0]),
                    parseNumber(parts[1])));
        }
        return items;
    }

    private static String require(Properties props, String key) {
        String value = props.getProperty(key);
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(
                    "Missing property: " + key);
        }
        return value.trim();
    }

    private static double parseNumber(String value) {
        try {
            return Double.parseDouble(value.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    "Not a number: '" + value.trim() + "'", e);
        }
    }
}
