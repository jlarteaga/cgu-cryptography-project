package cryptologyapp.nlp;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class NGramLanguageModelTest {

    static final char[] ALPHABET = {'a', 'b', 'c'};
    static final char SEPARATOR = '_';

    /**
     * +++++++++++++++++++++++++++++
     * |   | _ | a | b | c | total |
     * +++++++++++++++++++++++++++++
     * | _ | 0 | 3 | 2 | 1 |     6 |
     * +++++++++++++++++++++++++++++
     * | a | 1 | 1 | 2 | 1 |     5 |
     * +++++++++++++++++++++++++++++
     * | b | 0 | 3 | 0 | 1 |     4 |
     * +++++++++++++++++++++++++++++
     * | c | 2 | 0 | 1 | 2 |     5 |
     * +++++++++++++++++++++++++++++
     */
    static final Map<String, Integer> FREQUENCY_MAP = new HashMap<String, Integer>() {{
        put("__", 0);
        put("_a", 3);
        put("_b", 2);
        put("_c", 1);
        put("a_", 1);
        put("aa", 1);
        put("ab", 2);
        put("ac", 1);
        put("b_", 0);
        put("ba", 3);
        put("bb", 0);
        put("bc", 1);
        put("c_", 2);
        put("ca", 0);
        put("cb", 1);
        put("cc", 2);
    }};
    static final Map<String, Double> PROBABILITY_MAP = new HashMap<>() {{
        put("__", 0.0);
        put("_a", 0.5);
        put("_b", 1.0 / 3.0);
        put("_c", 1.0 / 6.0);
        put("a_", 0.2);
        put("aa", 0.2);
        put("ab", 0.4);
        put("ac", 0.2);
        put("b_", 0.0);
        put("ba", 0.75);
        put("bb", 0.0);
        put("bc", 0.25);
        put("c_", 0.4);
        put("ca", 0.0);
        put("cb", 0.2);
        put("cc", 0.4);
    }};
    static final double SMOOTHING_CONSTANT = 0.0001;

    final static String NOT_NORMALIZED_INPUT = "abc";
    final static String NORMALIZED_INPUT = "_abc_";
    final static Double EXPECTED_INPUT_PROBABILITY = PROBABILITY_MAP.get("_a") *
            PROBABILITY_MAP.get("ab") *
            PROBABILITY_MAP.get("bc") *
            PROBABILITY_MAP.get("c_");
    final static Double EXPECTED_INPUT_PERPLEXITY = Math.pow(EXPECTED_INPUT_PROBABILITY,
            -1.0 / NORMALIZED_INPUT.length());

    @Test
    void testConstructor() {
        Alphabet alphabet = new Alphabet(NGramLanguageModelTest.ALPHABET, NGramLanguageModelTest.SEPARATOR);
        NGramLanguageModel model = new NGramLanguageModel("test language",
                2,
                alphabet,
                NGramLanguageModelTest.FREQUENCY_MAP,
                NGramLanguageModelTest.SMOOTHING_CONSTANT
        );
        Map<String, Double> probabilityMap = model.getProbabilityMap();

        assertEquals(probabilityMap.size(), NGramLanguageModelTest.PROBABILITY_MAP.size());
        for (String key : probabilityMap.keySet()) {
            assertEquals(probabilityMap.get(key), NGramLanguageModelTest.PROBABILITY_MAP.get(key));
        }
    }

    @Test
    void testProbability() {
        Alphabet alphabet = new Alphabet(NGramLanguageModelTest.ALPHABET, NGramLanguageModelTest.SEPARATOR);
        NGramLanguageModel model = new NGramLanguageModel("test language",
                2,
                alphabet,
                NGramLanguageModelTest.FREQUENCY_MAP,
                NGramLanguageModelTest.SMOOTHING_CONSTANT
        );
        assertEquals(EXPECTED_INPUT_PROBABILITY, model.calculateProbability(NOT_NORMALIZED_INPUT, false));
        assertEquals(EXPECTED_INPUT_PROBABILITY, model.calculateProbability(NORMALIZED_INPUT, true));
    }

    @Test
    void testPerplexity() {
        Alphabet alphabet = new Alphabet(NGramLanguageModelTest.ALPHABET, NGramLanguageModelTest.SEPARATOR);
        NGramLanguageModel model = new NGramLanguageModel("test language",
                2,
                alphabet,
                NGramLanguageModelTest.FREQUENCY_MAP,
                NGramLanguageModelTest.SMOOTHING_CONSTANT
        );
        assertEquals(EXPECTED_INPUT_PERPLEXITY, model.calculatePerplexity(NOT_NORMALIZED_INPUT, false));
        assertEquals(EXPECTED_INPUT_PERPLEXITY, model.calculatePerplexity(NORMALIZED_INPUT, true));
    }


}
