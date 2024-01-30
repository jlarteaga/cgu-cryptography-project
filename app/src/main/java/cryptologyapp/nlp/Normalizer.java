package cryptologyapp.nlp;

public class Normalizer {

    private Alphabet alphabet;

    public Normalizer(Alphabet alphabet) {
        this.alphabet = alphabet;
    }

    public String normalize(String input) {
        StringBuilder sb = new StringBuilder();
        char separator = this.alphabet.getSeparator();
        boolean isLastCharacterASeparator = false;

        // Appends a separator to denote the beginning of the first word
        sb.append(separator);
        isLastCharacterASeparator = true;

        for (int charIdx = 0; charIdx < input.length(); charIdx++) {
            char currentChar = input.charAt(charIdx);

            if (this.alphabet.contains(currentChar)) {
                sb.append(currentChar);
                isLastCharacterASeparator = false;
                continue;
            }

            if (!isLastCharacterASeparator) {
                sb.append(separator);
                isLastCharacterASeparator = true;
            }
        }

        // Appends a separator to mark the end of the last word
        if (!isLastCharacterASeparator) {
            sb.append(separator);
        }
        return sb.toString();
    }
}
