package cryptologyapp.nlp;

import cryptologyapp.TestingConstants;
import cryptologyapp.files.Directories;
import cryptologyapp.utils.Collections;
import cryptologyapp.utils.Files;
import cryptologyapp.utils.SimpleTestConstants;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class NGramLanguageModelManagerTest {


    @BeforeEach
    void cleanTmpOutputDirectoryBeforeEach() {
        Directories.recursivelyDeleteDirectory(TestingConstants.BASE_TEST_TMP_OUTPUT_PATH);
    }

    @AfterAll
    static void cleanTmpOutputDirectoryAfterAll() {
        Directories.recursivelyDeleteDirectory(TestingConstants.BASE_TEST_TMP_OUTPUT_PATH);
    }

    @Test
    void train() {
        NGramLanguageModel model = NGramLanguageModelManager.train(TestingConstants.LATIN_TRAINING_FILE,
                "latin",
                2,
                TestingConstants.LATIN_ALPHABET,
                TestingConstants.SEPARATOR,
                TestingConstants.SMOOTHING_CONSTANT);
        double latinTextProbability = model.calculateProbability("lorem ipsum", false);
        double randomTextProbability = model.calculateProbability("abcdef", false);
        double latinTextPerplexity = model.calculatePerplexity("lorem ipsum", false);
        double randomTextPerplexity = model.calculatePerplexity("abcdef", false);
        System.out.format("latin text probability: %f | random text probability: %f%n",
                latinTextProbability,
                randomTextProbability);
        System.out.format("latin text perplexity: %f | random text perplexity: %f%n",
                latinTextPerplexity,
                randomTextPerplexity);
        assertTrue(latinTextProbability > randomTextProbability,
                "Latin text should be more probable than random text for the Latin training set");
        assertTrue(latinTextPerplexity < randomTextPerplexity,
                "Latin text should be less perplex than random text for the Latin training set");
    }

    @Test
    void save() {
        Path outputPath = TestingConstants.BASE_TEST_TMP_OUTPUT_PATH.resolve("test.lmodel");
        Alphabet alphabet = AlphabetManager.load(SimpleTestConstants.ALPHABET_PATH);
        List<String> expectedFileOutputLines = new ArrayList<>() {{
            add(SimpleTestConstants.LANGUAGE_NAME);
            add("2");
            add(Double.toString(TestingConstants.SMOOTHING_CONSTANT));
            add("ab");
            add("1");
        }};

        NGramLanguageModel model = new NGramLanguageModel("test",
                2,
                alphabet,
                Map.of("ab", 1),
                TestingConstants.SMOOTHING_CONSTANT);
        NGramLanguageModelManager.save(model, outputPath);

        Files.assertFileContent(expectedFileOutputLines, outputPath);
    }

    @Test
    void load() {
        Path inputPath = SimpleTestConstants.LANGUAGE_MODEL_PATH;
        Alphabet alphabet = AlphabetManager.load(SimpleTestConstants.ALPHABET_PATH);
        NGramLanguageModel model = NGramLanguageModelManager.load(alphabet, inputPath);

        assertEquals(SimpleTestConstants.LANGUAGE_NAME, model.getLanguage());
        assertEquals(SimpleTestConstants.N_GRAM_SIZE, model.getNGramSize());
        assertEquals(SimpleTestConstants.SMOOTHING_CONSTANT, model.getSmoothingConstant());
        Collections.assertEquals(SimpleTestConstants.FREQUENCY_MAP, model.getFrequencyMap());
    }
}
