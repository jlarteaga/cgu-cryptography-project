package cryptologyapp.util;

import java.util.Set;

public class Strings {

    /**
     * Included to hide the implicit public constructor
     */
    private Strings() {
    }

    public static String join(String delimiter, Set<Character> characterSet) {
        return String.join(delimiter, characterSet.stream().map(Object::toString).toList());
    }
}
