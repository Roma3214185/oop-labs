package ua.medicine;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class ApplicationPathsTest {

    @Test
    void defaultsAreUsedWithoutArguments() {
        ApplicationPaths paths = ApplicationPaths.fromArgs();

        assertEquals(TestData.XML, paths.xml());
        assertEquals(TestData.XSD, paths.xsd());
        assertEquals(TestData.XSL, paths.xsl());
        assertEquals(Path.of("output", "medicines-by-group.xml"),
                paths.output());
    }

    @Test
    void defaultsAreFoundFromRepositoryRoot(@TempDir Path root)
            throws IOException {
        Files.createDirectories(root.resolve("lab2").resolve("data"));

        ApplicationPaths paths = ApplicationPaths.defaults(root);

        assertEquals(root.resolve("lab2/data/medicines.xml"), paths.xml());
        assertEquals(root.resolve("lab2/output/medicines-by-group.xml"),
                paths.output());
    }

    @Test
    void pathsAreTakenFromArguments() {
        assertEquals(new ApplicationPaths(Path.of("a.xml"),
                        Path.of("b.xsd"), Path.of("c.xsl"), Path.of("d.xml")),
                ApplicationPaths.fromArgs("a.xml", "b.xsd", "c.xsl",
                        "d.xml"));
    }

    @Test
    void wrongNumberOfArgumentsIsRejected() {
        assertThrows(IllegalArgumentException.class,
                () -> ApplicationPaths.fromArgs("a.xml"));
    }

    @Test
    void nullPathsAreRejected() {
        Path p = Path.of("x");
        assertThrows(NullPointerException.class,
                () -> new ApplicationPaths(null, p, p, p));
        assertThrows(NullPointerException.class,
                () -> new ApplicationPaths(p, null, p, p));
        assertThrows(NullPointerException.class,
                () -> new ApplicationPaths(p, p, null, p));
        assertThrows(NullPointerException.class,
                () -> new ApplicationPaths(p, p, p, null));
    }
}
