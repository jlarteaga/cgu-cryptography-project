package cryptologyapp.utils;

import cryptologyapp.TestingConstants;

import java.nio.file.Path;
import java.util.Map;

public class SimpleTestConstants {

    public static final int N_GRAM_SIZE = 2;
    public static final String LANGUAGE_NAME = "simple";
    public static final Map<String, Integer> FREQUENCY_MAP = Map.of(
            "_a", 1,
            "a_", 1
    );
    public static final double SMOOTHING_CONSTANT = 0.1;
    public static final Path ALPHABET_PATH = TestingConstants.BASE_TEST_RESOURCES_PATH.resolve("simple-test.alphabet");
    public static final Path LANGUAGE_MODEL_PATH = TestingConstants.BASE_TEST_RESOURCES_PATH.resolve(
            "simple-test.lmodel");

    /**
     * Included to hide the implicit public constructor
     */
    private SimpleTestConstants() {
    }
}
