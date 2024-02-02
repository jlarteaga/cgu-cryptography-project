package cryptologyapp;

import java.io.File;
import java.nio.file.Path;

public class TestingConstants {
    public static final Path BASE_TEST_RESOURCES_PATH = Path.of("src" + File.separatorChar + "test" + File.separatorChar + "resources");
    public static final Path BASE_TEST_TMP_OUTPUT_PATH = BASE_TEST_RESOURCES_PATH.resolve("tmp");
    public static final char SEPARATOR = '_';
    public static final double SMOOTHING_CONSTANT = 0.000001;

    public static final Path LATIN_ALPHABET_FILE = TestingConstants.BASE_TEST_RESOURCES_PATH.resolve("latin.alphabet");
    public static final Path LATIN_TRAINING_FILE = TestingConstants.BASE_TEST_RESOURCES_PATH.resolve(
            "latin-training-set.txt");
}
