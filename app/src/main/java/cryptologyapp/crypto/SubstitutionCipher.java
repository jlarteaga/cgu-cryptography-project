package cryptologyapp.crypto;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class SubstitutionCipher implements Cipher {
    private char[] baseAlphabet = null;
    private char[] cipherAlphabet = null;
    private Map<Character, Character> encryptionAlphabetMapping = new HashMap<>();
    private Map<Character, Character> decryptionAlphabetMapping = new HashMap<>();

    public SubstitutionCipher(char[] baseAlphabet, char[] cipherAlphabet) {
        this.setAlphabets(baseAlphabet, cipherAlphabet);
    }

    private void buildAlphabetMappings() {
        this.encryptionAlphabetMapping.clear();
        this.decryptionAlphabetMapping.clear();

        int alphabetLength = baseAlphabet.length;
        for (int i = 0; i < alphabetLength; i++) {
            this.encryptionAlphabetMapping.put(baseAlphabet[i], cipherAlphabet[i]);
            this.decryptionAlphabetMapping.put(cipherAlphabet[i], baseAlphabet[i]);
        }
    }

    @Override
    public String encrypt(String plainText) {
        return SubstitutionCipher.substituteString(plainText, encryptionAlphabetMapping);
    }

    @Override
    public String decrypt(String encryptedText) {
        return SubstitutionCipher.substituteString(encryptedText, decryptionAlphabetMapping);
    }

    private static String substituteString(String input, Map<Character, Character> alphabetMapping) {
        StringBuilder sb = new StringBuilder();

        Character encryptedCharacter;
        for (int index = 0; index < input.length(); index++) {
            encryptedCharacter = alphabetMapping.get(input.charAt(index));
            if (encryptedCharacter != null) {
                sb.append(encryptedCharacter);
            }
        }

        return sb.toString();
    }

    public void setAlphabets(char[] baseAlphabet, char[] cipherAlphabet) {
        if (baseAlphabet == null || cipherAlphabet == null) {
            throw new IllegalArgumentException("baseAlphabet and cipherAlphabet must not be null");
        }
        if (baseAlphabet.length != cipherAlphabet.length) {
            throw new IllegalArgumentException(
                    "sizes of baseAlphabet and cipherAlphabet must be the same");
        }

        this.baseAlphabet = Arrays.copyOf(baseAlphabet, baseAlphabet.length);
        this.cipherAlphabet = Arrays.copyOf(cipherAlphabet, cipherAlphabet.length);

        this.buildAlphabetMappings();
    }

    public void setBaseAlphabet(char[] baseAlphabet) {
        if (baseAlphabet == null) {
            throw new IllegalArgumentException("plainAlphabet must not be null");
        }
        if (baseAlphabet.length != cipherAlphabet.length) {
            throw new IllegalArgumentException("sizes of plain and cipher must be the same");
        }

        this.baseAlphabet = Arrays.copyOf(baseAlphabet, baseAlphabet.length);

        this.buildAlphabetMappings();
    }

    public void setCipherAlphabet(char[] cipherAlphabet) {
        if (cipherAlphabet == null) {
            throw new IllegalArgumentException("cipherAlphabet must not be null");
        }
        if (baseAlphabet.length != cipherAlphabet.length) {
            throw new IllegalArgumentException("sizes of plain and cipher must be the same");
        }

        this.cipherAlphabet = Arrays.copyOf(cipherAlphabet, cipherAlphabet.length);

        this.buildAlphabetMappings();
    }

    public int getAlphabetSize() {
        return this.baseAlphabet.length;
    }

    public char[] getBaseAlphabet() {
        return Arrays.copyOf(this.baseAlphabet, this.baseAlphabet.length);
    }

    public char[] getCipherAlphabet() {
        return Arrays.copyOf(this.cipherAlphabet, this.cipherAlphabet.length);
    }
}
