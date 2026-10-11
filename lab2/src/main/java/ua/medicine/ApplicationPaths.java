package ua.medicine;

import java.nio.file.Files;
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

    /** Folder with the XML, XSD and XSL files. */
    static final String DATA_DIR = "data";
    /** Folder of this project inside the repository. */
    static final String PROJECT_DIR = "lab2";

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
            return defaults(Path.of(""));
        }
        if (args.length != ARGUMENT_COUNT) {
            throw new IllegalArgumentException(
                    "Usage: <xml> <xsd> <xsl> <output>");
        }
        return new ApplicationPaths(Path.of(args[0]), Path.of(args[1]),
                Path.of(args[2]), Path.of(args[3]));
    }

    /**
     * Creates default paths. The files are looked up in the
     * {@code data} folder of the working directory or, when the
     * application is started from the repository root, of the
     * {@code lab2} folder.
     *
     * @param workingDir directory the application is started from
     * @return default paths
     */
    static ApplicationPaths defaults(Path workingDir) {
        Path base = Files.isDirectory(workingDir.resolve(DATA_DIR))
                ? workingDir
                : workingDir.resolve(PROJECT_DIR);
        Path data = base.resolve(DATA_DIR);
        return new ApplicationPaths(
                data.resolve("medicines.xml"),
                data.resolve("medicines.xsd"),
                data.resolve("medicines-by-group.xsl"),
                base.resolve("output").resolve("medicines-by-group.xml"));
    }
}
