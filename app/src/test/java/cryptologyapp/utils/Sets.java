package cryptologyapp.utils;

import org.junit.jupiter.api.Assertions;

import java.util.Set;

public class Sets {
    
    /**
     * Included to hide the implicit public constructor
     */
    private Sets() {
    }

    public static void assertEquals(Set<Character> expected, Set<Character> actual) {
        Assertions.assertEquals(expected.size(), actual.size());
        expected.forEach(expectedCharacter -> Assertions.assertTrue(actual.contains(expectedCharacter)));
    }
}
