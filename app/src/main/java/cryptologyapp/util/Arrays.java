package cryptologyapp.util;

import java.util.Set;

public class Arrays {

    /**
     * Included to hide the implicit public constructor
     */
    private Arrays() {
    }

    public static <T> T[] join(T[] array1, T[] array2) {
        T[] result = java.util.Arrays.copyOf(array1, array1.length + array2.length);
        for (int i = 0; i < array2.length; i++) {
            result[i + array1.length] = array2[i];
        }
        return result;
    }
}
