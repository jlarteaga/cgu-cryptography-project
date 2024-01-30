package cryptologyapp.nlp;

import cryptologyapp.TestingConstants;
import cryptologyapp.files.Directories;
import cryptologyapp.util.Strings;
import cryptologyapp.utils.Sets;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AlphabetManagerTest {

    @AfterAll
    static void cleanTmpOutputDirectoryAfterAll() {
        Directories.recursivelyDeleteDirectory(TestingConstants.BASE_TEST_TMP_OUTPUT_PATH);
    }

    @BeforeEach
    void cleanTmpOutputDirectoryBeforeEach() {
        Directories.recursivelyDeleteDirectory(TestingConstants.BASE_TEST_TMP_OUTPUT_PATH);
    }

    @Test
    void load() {
        Alphabet expectedAlphabet = new Alphabet(TestingConstants.LATIN_ALPHABET, TestingConstants.SEPARATOR);
        Alphabet loadedAlphabet = AlphabetManager.load(TestingConstants.LATIN_ALPHABET_FILE);

        Set<Character> expectedAlphabetSet = expectedAlphabet.getAlphabetSet();
        Set<Character> loadedAlphabetSet = loadedAlphabet.getAlphabetSet();
        Sets.assertEquals(expectedAlphabetSet, loadedAlphabetSet);
        assertEquals(expectedAlphabet.getSeparator(), loadedAlphabet.getSeparator());
    }

    @Test
    void save() {
        Path outputPath = TestingConstants.BASE_TEST_TMP_OUTPUT_PATH.resolve("latin.alphabet");
        Alphabet alphabet = new Alphabet(TestingConstants.LATIN_ALPHABET, TestingConstants.SEPARATOR);
        List<String> expectedFileOutputLines = new ArrayList<>() {{
            add(String.join("", Strings.join("", alphabet.getAlphabetSet())));
            add(Character.toString(alphabet.getSeparator()));
        }};

        AlphabetManager.save(alphabet, outputPath);

        try {
            List<String> actualLines = Files.readAllLines(outputPath);
            assertEquals(expectedFileOutputLines.size(), actualLines.size());
            for (int i = 0; i < expectedFileOutputLines.size(); i++) {
                String expectedLine = expectedFileOutputLines.get(i);
                assertEquals(expectedLine, actualLines.get(i));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
