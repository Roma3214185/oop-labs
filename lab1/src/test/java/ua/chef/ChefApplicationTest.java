package ua.chef;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ChefApplicationTest {

    private InputStream originalIn;
    private PrintStream originalOut;

    @BeforeEach
    void rememberStreams() {
        originalIn = System.in;
        originalOut = System.out;
    }

    @AfterEach
    void restoreStreams() {
        System.setIn(originalIn);
        System.setOut(originalOut);
    }

    @Test
    void mainRunsMenuOnConsole() {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        System.setIn(new ByteArrayInputStream(
                "2\n100\n0\n0\n".getBytes(StandardCharsets.UTF_8)));
        System.setOut(new PrintStream(buffer, true, StandardCharsets.UTF_8));

        ChefApplication.main(new String[0]);

        String output = buffer.toString(StandardCharsets.UTF_8);
        assertTrue(output.contains("Шеф: Огірок 100 г"));
        assertTrue(output.contains("Смачного!"));
    }
}
