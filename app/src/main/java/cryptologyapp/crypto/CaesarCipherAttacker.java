package cryptologyapp.crypto;

import cryptologyapp.nlp.NGramLanguageModel;

import java.util.*;

public class CaesarCipherAttacker {

    private CaesarCipher cipher;
    private char[] substitutionAlphabet;

    public CaesarCipherAttacker(char[] substitutionAlphabet) {
        CaesarCipherAttacker.validateSubstitutionAlphabet(substitutionAlphabet);

        this.substitutionAlphabet = substitutionAlphabet;
        this.cipher = new CaesarCipher(this.substitutionAlphabet, 0);
    }

    public List<Candidate> bruteForceAttack(NGramLanguageModel model, String input) {
        int possibilities = this.substitutionAlphabet.length;
        List<Candidate> candidates = new ArrayList<>();

        for (int offset = 0; offset < possibilities; offset++) {
            cipher.setOffset(offset);
            String decipheredOption = cipher.decrypt(input);
            double perplexity = model.calculatePerplexity(decipheredOption, false);
            candidates.add(new Candidate(offset, perplexity));
        }

        Collections.sort(candidates, (ps1, ps2) -> {
            if (ps1.getPerplexity() < ps2.getPerplexity()) {
                return -1;
            } else if (ps1.getPerplexity() > ps2.getPerplexity()) {
                return 1;
            }
            return 0;
        });

        return candidates;
    }

    private static char[] generateSubstitutionAlphabet(char[] letters, char[] punctuationMarks) {
        char[] lexicalAlphabet = new char[letters.length + punctuationMarks.length];
        for (int i = 0; i < letters.length; i++) {
            lexicalAlphabet[i] = letters[i];
        }
        for (int i = 0; i < punctuationMarks.length; i++) {
            lexicalAlphabet[letters.length + i] = punctuationMarks[i];
        }
        return lexicalAlphabet;
    }

    private static void validateSubstitutionAlphabet(char[] substitutionAlphabet) {
        if (substitutionAlphabet == null) {
            throw new IllegalArgumentException("A substitution alphabet must not be provided");
        }
        if (substitutionAlphabet.length < 2) {
            throw new IllegalArgumentException("A substitution alphabet must have at least two characters");
        }
    }

    public static class Candidate {
        private int offset;
        private double perplexity;

        public Candidate(int offset, double perplexity) {
            this.offset = offset;
            this.perplexity = perplexity;
        }

        public int getOffset() {
            return this.offset;
        }

        public double getPerplexity() {
            return this.perplexity;
        }
    }
}
