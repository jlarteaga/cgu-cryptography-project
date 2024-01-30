package cryptologyapp.nlp;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Normalizer {
    private Set<Character> alphabet;
    private char separator;

    public Normalizer(Character[] alphabet, char separator) {
        this.alphabet = new HashSet<>(Arrays.asList(alphabet));

        Normalizer.validateSeparator(this.alphabet, separator);
        this.separator = separator;
    }

    private static void validateSeparator(Set<Character> alphabet, char separator) {
        if (alphabet.contains(separator)) {
            throw new IllegalArgumentException("The separator must not be part of the alphabet");
        }
    }

    public String normalize(String input) {
        StringBuilder sb = new StringBuilder();
        boolean isLastCharacterASeparator = false;

        // Appends a separator to denote the beginning of the first word
        sb.append(this.separator);
        isLastCharacterASeparator = true;

        for (int charIdx = 0; charIdx < input.length(); charIdx++) {
            char currentChar = input.charAt(charIdx);

            if (this.alphabet.contains(currentChar)) {
                sb.append(currentChar);
                isLastCharacterASeparator = false;
                continue;
            }

            if (!isLastCharacterASeparator) {
                sb.append(this.separator);
                isLastCharacterASeparator = true;
            }
        }

        // Appends a separator to mark the end of the last word
        if (!isLastCharacterASeparator) {
            sb.append(this.separator);
        }
        return sb.toString();
    }
}
