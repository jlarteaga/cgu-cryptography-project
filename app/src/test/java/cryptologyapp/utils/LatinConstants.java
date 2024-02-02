package cryptologyapp.utils;

import cryptologyapp.TestingConstants;

import java.nio.file.Path;
import java.util.Map;

public class LatinConstants {
    public static final String LANGUAGE_NAME = "latin";
    public static final double SMOOTHING_CONSTANT = 1e-10;
    public static final Path ALPHABET_PATH = TestingConstants.BASE_TEST_RESOURCES_PATH.resolve("latin.alphabet");
    public static final Path LANGUAGE_TRAINING_SET_PATH = TestingConstants.BASE_TEST_RESOURCES_PATH.resolve(
            "latin-training-set.txt");

    public static final char[] LETTERS = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
    public static final char[] PUNCTUATION_MARKS = {',', '.', ' '};
    public static final char[] LEXICOGRAPHICAL_ALPHABET = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', ',', '.', ' '};
}
