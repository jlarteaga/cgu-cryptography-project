package cryptologyapp.crypto;

import cryptologyapp.nlp.Alphabet;
import cryptologyapp.nlp.AlphabetManager;
import cryptologyapp.nlp.NGramLanguageModel;
import cryptologyapp.nlp.NGramLanguageModelManager;
import cryptologyapp.utils.LatinConstants;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CaesarCipherAttackerTest {

    private CaesarCipher utilCipher = new CaesarCipher(LatinConstants.LEXICOGRAPHICAL_ALPHABET, 0);
    private Alphabet alphabet = AlphabetManager.load(LatinConstants.ALPHABET_PATH);
    private NGramLanguageModel model = NGramLanguageModelManager.train(LatinConstants.LANGUAGE_TRAINING_SET_PATH,
            "latin",
            3,
            alphabet,
            LatinConstants.SMOOTHING_CONSTANT);
    private CaesarCipherAttacker attacker = new CaesarCipherAttacker(LatinConstants.LETTERS,
            LatinConstants.PUNCTUATION_MARKS);

    @Test
    void attackWithoutOffset() {
        String latinText = "lorem ipsum";

        List<CaesarCipherAttacker.Candidate> candidates = this.attacker.bruteForceAttack(this.model,
                latinText);
        CaesarCipherAttacker.Candidate mostLikelySolution = candidates.getFirst();

        utilCipher.setOffset(mostLikelySolution.getOffset());
        assertEquals(0, mostLikelySolution.getOffset());
        assertEquals(latinText, utilCipher.decrypt(latinText));
    }

    @Test
    void attackWithOffset() {
        String latinText = "lorem ipsum";
        int offset = 2;
        this.utilCipher.setOffset(offset);
        String offsetText = this.utilCipher.encrypt(latinText);

        List<CaesarCipherAttacker.Candidate> candidates = this.attacker.bruteForceAttack(this.model,
                offsetText);
        CaesarCipherAttacker.Candidate mostLikelySolution = candidates.getFirst();

        utilCipher.setOffset(mostLikelySolution.getOffset());
        assertEquals(offset, mostLikelySolution.getOffset());
        assertEquals(latinText, utilCipher.decrypt(offsetText));
    }

    @Test
    void attackWithOffsetAndNotPresentWord() {
        String latinText = "proeliaretur";
        int offset = 2;
        this.utilCipher.setOffset(offset);
        String offsetText = this.utilCipher.encrypt(latinText);

        List<CaesarCipherAttacker.Candidate> candidates = this.attacker.bruteForceAttack(this.model,
                offsetText);
        CaesarCipherAttacker.Candidate mostLikelySolution = candidates.getFirst();

        utilCipher.setOffset(mostLikelySolution.getOffset());
        String result = utilCipher.decrypt(offsetText);
        assertEquals(offset, mostLikelySolution.getOffset());
        assertEquals(latinText, result);
    }
}
