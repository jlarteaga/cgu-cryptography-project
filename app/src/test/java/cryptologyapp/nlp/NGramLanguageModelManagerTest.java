package cryptologyapp.nlp;

import cryptologyapp.TestingConstants;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

class NGramLanguageModelManagerTest {

    static final char[] LATIN_ALPHABET = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
    static final char SEPARATOR = '_';
    static final double SMOOTHING_CONSTANT = 0.000001;
    static final Path LATIN_TRAINING_FILE = TestingConstants.BASE_TEST_RESOURCES_PATH.resolve("latin-training-set.txt");

    @Test
    void train() {
        NGramLanguageModel model = NGramLanguageModelManager.train(LATIN_TRAINING_FILE,
                "latin",
                2,
                LATIN_ALPHABET,
                SEPARATOR,
                SMOOTHING_CONSTANT);
        double latinTextProbability = model.calculateProbability("lorem", false);
        double randomTextProbability = model.calculateProbability("abc", false);
        double latinTextPerplexity = model.calculatePerplexity("lorem", false);
        double randomTextPerplexity = model.calculatePerplexity("abc", false);
        System.out.format("latin text probability: %f | random text probability: %f",
                latinTextProbability,
                randomTextProbability);
        System.out.format("latin text perplexity: %f | random text perplexity: %f",
                latinTextPerplexity,
                randomTextPerplexity);
        assertTrue(latinTextProbability > randomTextProbability,
                "Latin text should be more probable than random text for the Latin training set");
        assertTrue(latinTextPerplexity < randomTextPerplexity,
                "Latin text should be less perplex than random text for the Latin training set");
    }
}
