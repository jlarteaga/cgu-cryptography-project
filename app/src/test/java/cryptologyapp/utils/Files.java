package cryptologyapp.utils;

import cryptologyapp.files.FileValidator;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Files {

    /**
     * Included to hide the implicit public constructor
     */
    private Files() {
    }

    public static void assertFileContent(List<String> expectedFileLines, Path filePath) {
        FileValidator.validateThatExists(filePath);
        try {
            List<String> actualLines = java.nio.file.Files.readAllLines(filePath);
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
