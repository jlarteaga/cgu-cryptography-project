package cryptologyapp.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FileTester {

    /**
     * Included to hide the implicit public constructor
     */
    private FileTester() {
    }

    public static void assertFileContent(List<String> expectedFileLines, Path filePath) {
        try {
            List<String> actualLines = Files.readAllLines(filePath);
            assertEquals(expectedFileLines.size(), actualLines.size());
            for (int i = 0; i < expectedFileLines.size(); i++) {
                String expectedLine = expectedFileLines.get(i);
                assertEquals(expectedLine, actualLines.get(i));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
