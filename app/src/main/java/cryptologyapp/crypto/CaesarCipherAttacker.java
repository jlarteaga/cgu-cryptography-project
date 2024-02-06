package cryptologyapp.crypto;

import cryptologyapp.nlp.NGramLanguageModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CaesarCipherAttacker {

    private CaesarCipher cipher;
    private char[] substitutionAlphabet;

    public CaesarCipherAttacker(char[] substitutionAlphabet) {
        CaesarCipherAttacker.validateSubstitutionAlphabet(substitutionAlphabet);

        this.substitutionAlphabet = substitutionAlphabet;
        this.cipher = new CaesarCipher(this.substitutionAlphabet, 0);
    }

    public List<SolutionCandidate> bruteForceAttack(NGramLanguageModel model, String input) {
        int possibilities = this.substitutionAlphabet.length;
        List<SolutionCandidate> solutionCandidates = new ArrayList<>();

        for (int offset = 0; offset < possibilities; offset++) {
            cipher.setOffset(offset);
            String decipheredOption = cipher.decrypt(input);
            double perplexity = model.calculatePerplexity(decipheredOption, false);
            solutionCandidates.add(new SolutionCandidate(offset, perplexity));
        }

        Collections.sort(solutionCandidates, (ps1, ps2) -> {
            if (ps1.getPerplexity() < ps2.getPerplexity()) {
                return -1;
            } else if (ps1.getPerplexity() > ps2.getPerplexity()) {
                return 1;
            }
            return 0;
        });

        return solutionCandidates;
    }

    private static void validateSubstitutionAlphabet(char[] substitutionAlphabet) {
        if (substitutionAlphabet == null) {
            throw new IllegalArgumentException("A substitution alphabet must not be provided");
        }
        if (substitutionAlphabet.length < 2) {
            throw new IllegalArgumentException("A substitution alphabet must have at least two characters");
        }
    }

    public static class SolutionCandidate {
        private int offset;
        private double perplexity;

        public SolutionCandidate(int offset, double perplexity) {
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
