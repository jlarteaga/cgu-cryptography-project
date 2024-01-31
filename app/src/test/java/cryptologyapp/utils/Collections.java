package cryptologyapp.utils;

import org.junit.jupiter.api.Assertions;

import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class Collections {

    /**
     * Included to hide the implicit public constructor
     */
    private Collections() {
    }

    public static void assertEquals(Set<Character> expected, Set<Character> actual) {
        Assertions.assertEquals(expected.size(), actual.size());
        expected.forEach(expectedCharacter -> Assertions.assertTrue(actual.contains(expectedCharacter)));
    }

    public static <K, V> void assertEquals(Map<K, V> expected, Map<K, V> actual) {
        Assertions.assertEquals(expected.size(), actual.size());
        expected.forEach((k, v) -> {
            assertTrue(actual.containsKey(k));
            Assertions.assertEquals(k, actual.get(k));
        });
    }
}
