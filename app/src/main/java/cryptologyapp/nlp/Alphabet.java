package cryptologyapp.nlp;

import java.util.HashSet;
import java.util.Set;

public class Alphabet {

    private Set<Character> alphabetSet;
    private char separator;

    public Alphabet(char[] alphabet, char separator) {
        this.alphabetSet = Alphabet.generateAlphabetSet(alphabet);
        validateSeparator(this.alphabetSet, separator);
        this.separator = separator;
    }

    public static Set<Character> generateAlphabetSet(char[] alphabet) {
        Set<Character> alphabetSet = new HashSet<>();
        for (int i = 0; i < alphabet.length; i++) {
            alphabetSet.add(alphabet[i]);
        }
        return alphabetSet;
    }

    private static void validateSeparator(Set<Character> alphabet, char separator) {
        if (alphabet.contains(separator)) {
            throw new IllegalArgumentException("The separator must not be part of the alphabet");
        }
    }

    public boolean contains(char character) {
        return this.alphabetSet.contains(character);
    }

    public Set<Character> getAlphabetSet() {
        return alphabetSet;
    }

    public char getSeparator() {
        return separator;
    }
}
