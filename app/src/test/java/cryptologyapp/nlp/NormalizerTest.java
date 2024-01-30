package cryptologyapp.nlp;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NormalizerTest {

    static final Character[] ALPHABET = {'a', 'b', 'c', 'd', 'e', 'f'};
    static final char SEPARATOR = '_';

    @Test
    void testBasicNormalization() {
        Normalizer normalizer = new Normalizer(NormalizerTest.ALPHABET, NormalizerTest.SEPARATOR);

        assertEquals("_a_",
                normalizer.normalize("a"),
                "normalize() should append the separator at the beginning and the end of the string");
        assertEquals("_a_b_",
                normalizer.normalize("a,b"),
                "normalize() should replace characters outside the alphabet with the separator");
        assertEquals("_ab_",
                normalizer.normalize(",.!@#$^ab"),
                "normalize() should merge separators at the beginning of the string");
        assertEquals("_ab_",
                normalizer.normalize("ab,.!@#$^"),
                "normalize() should merge separators at the end of the string");
        assertEquals("_a_b_",
                normalizer.normalize("a,.!@#$^b"),
                "normalize() should merge separators in the middle of the string");
    }
}
