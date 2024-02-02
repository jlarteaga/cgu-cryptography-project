package cryptologyapp.nlp;

import cryptologyapp.TestingConstants;
import cryptologyapp.files.Directories;
import cryptologyapp.util.Strings;
import cryptologyapp.utils.Collections;
import cryptologyapp.utils.Files;
import cryptologyapp.utils.LatinConstants;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
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
        Alphabet expectedAlphabet = new Alphabet(LatinConstants.LETTERS, TestingConstants.SEPARATOR);
        Alphabet loadedAlphabet = AlphabetManager.load(TestingConstants.LATIN_ALPHABET_FILE);

        Set<Character> expectedAlphabetSet = expectedAlphabet.getAlphabetSet();
        Set<Character> loadedAlphabetSet = loadedAlphabet.getAlphabetSet();
        Collections.assertEquals(expectedAlphabetSet, loadedAlphabetSet);
        assertEquals(expectedAlphabet.getSeparator(), loadedAlphabet.getSeparator());
    }

    @Test
    void save() {
        Path outputPath = TestingConstants.BASE_TEST_TMP_OUTPUT_PATH.resolve("latin.alphabet");
        Alphabet alphabet = new Alphabet(LatinConstants.LETTERS, TestingConstants.SEPARATOR);

        List<String> expectedFileOutputLines = List.of(
                String.join("", Strings.join("", alphabet.getAlphabetSet())),
                Character.toString(alphabet.getSeparator())
        );

        AlphabetManager.save(alphabet, outputPath);
        Files.assertFileContent(expectedFileOutputLines, outputPath);
    }
}
