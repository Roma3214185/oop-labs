package ua.medicine;

import java.nio.file.Path;
import java.util.Objects;

/**
 * Files the application works with.
 *
 * @param xml    medicines XML document
 * @param xsd    XSD schema of the document
 * @param xsl    XSL stylesheet that groups the drugs
 * @param output result of the transformation
 */
public record ApplicationPaths(Path xml, Path xsd, Path xsl, Path output) {

    /** Paths used when no command line arguments are given. */
    public static final ApplicationPaths DEFAULT = new ApplicationPaths(
            Path.of("data", "medicines.xml"),
            Path.of("data", "medicines.xsd"),
            Path.of("data", "medicines-by-group.xsl"),
            Path.of("output", "medicines-by-group.xml"));

    private static final int ARGUMENT_COUNT = 4;

    /**
     * Validates that every path is given.
     */
    public ApplicationPaths {
        Objects.requireNonNull(xml, "xml");
        Objects.requireNonNull(xsd, "xsd");
        Objects.requireNonNull(xsl, "xsl");
        Objects.requireNonNull(output, "output");
    }

    /**
     * Creates paths from command line arguments.
     *
     * @param args nothing or {@code xml xsd xsl output}
     * @return paths
     * @throws IllegalArgumentException if the number of arguments is
     *                                  wrong
     */
    public static ApplicationPaths fromArgs(String... args) {
        if (args.length == 0) {
            return DEFAULT;
        }
        if (args.length != ARGUMENT_COUNT) {
            throw new IllegalArgumentException(
                    "Usage: <xml> <xsd> <xsl> <output>");
        }
        return new ApplicationPaths(Path.of(args[0]), Path.of(args[1]),
                Path.of(args[2]), Path.of(args[3]));
    }
}
