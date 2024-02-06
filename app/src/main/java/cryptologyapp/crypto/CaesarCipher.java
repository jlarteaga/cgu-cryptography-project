package cryptologyapp.crypto;

public class CaesarCipher implements Cipher {

    private static final int MIN_ALPHABET_SIZE = 3;

    private SubstitutionCipher innerCipher;
    private int offset;

    public CaesarCipher(char[] alphabet, int offset) {
        CaesarCipher.validateAlphabet(alphabet);
        this.offset = CaesarCipher.normalizeOffset(offset, alphabet);
        this.innerCipher = new SubstitutionCipher(alphabet, CaesarCipher.generateCipherAlphabet(alphabet, this.offset));
    }

    private static void validateAlphabet(char[] alphabet) {
        if (alphabet == null) {
            throw new AlphabetNotProvidedException();
        }
        if (alphabet.length < CaesarCipher.MIN_ALPHABET_SIZE) {
            throw new InvalidAlphabetSizeException(CaesarCipher.MIN_ALPHABET_SIZE);
        }
    }

    private static int normalizeOffset(int offset, char[] alphabet) {
        int alphabetSize = alphabet.length;
        offset = offset % alphabetSize;
        if (offset < 0) {
            offset += alphabetSize;
        }
        return offset;
    }

    private static char[] generateCipherAlphabet(char[] baseAlphabet, int normalizedOffset) {
        CaesarCipher.validateAlphabet(baseAlphabet);
        if (normalizedOffset < 0 || normalizedOffset >= baseAlphabet.length) {
            normalizedOffset = CaesarCipher.normalizeOffset(normalizedOffset, baseAlphabet);
        }

        char[] cipherAlphabet = new char[baseAlphabet.length];
        for (int i = 0; i < cipherAlphabet.length; i++) {
            cipherAlphabet[i] = baseAlphabet[(i + normalizedOffset) % baseAlphabet.length];
        }
        return cipherAlphabet;
    }

    /**
     * Updates the offset by setting a normalized value between `0` and `alphabet.length - 1`
     *
     * @param offset
     */
    public void setOffset(int offset) {
        char[] alphabet = this.innerCipher.getBaseAlphabet();
        this.offset = CaesarCipher.normalizeOffset(offset, alphabet);
        this.innerCipher.setCipherAlphabet(CaesarCipher.generateCipherAlphabet(alphabet, this.offset));
    }

    @Override
    public String encrypt(String plainText) {
        return this.innerCipher.encrypt(plainText);
    }

    @Override
    public String decrypt(String encryptedText) {
        return this.innerCipher.decrypt(encryptedText);
    }

    public int getOffset() {
        return this.offset;
    }
}
