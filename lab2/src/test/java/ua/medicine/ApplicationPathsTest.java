package ua.medicine;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.nio.file.Path;

import org.junit.jupiter.api.Test;

class ApplicationPathsTest {

    @Test
    void defaultsAreUsedWithoutArguments() {
        assertSame(ApplicationPaths.DEFAULT, ApplicationPaths.fromArgs());
        assertEquals(TestData.XML, ApplicationPaths.DEFAULT.xml());
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
