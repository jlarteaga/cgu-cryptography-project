package main.crypto;

import main.NotImplementedYetException;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class CaesarCipher implements Cipher {
    private final Map<Character, Integer> alphabetIndex;
    private final char[] alphabet;
    private int offset;

    private CaesarCipher(char[] alphabet) {
        this.alphabet = Arrays.copyOf(alphabet, alphabet.length);
        this.alphabetIndex = new HashMap<>();
        for (int letterIndex = 0; letterIndex < alphabet.length; letterIndex++) {
            this.alphabetIndex.put(alphabet[letterIndex], letterIndex);
        }
    }

    public CaesarCipher(char[] alphabet, int offset) {
        this(alphabet);
        this.setOffset(offset);
    }

    /**
     * Updates the offset by setting a normalized value between `0` and `alphabet.length - 1`
     *
     * @param offset
     */
    public void setOffset(int offset) {
        offset = offset % alphabet.length;
        if (offset < 0) {
            offset += this.alphabet.length;
        }
        this.offset = offset;
    }

    @Override
    public String encryptString(String plainText) {
        StringBuilder sb = new StringBuilder();

        char charToEncrypt;
        Integer charToEncryptIndex;
        int encryptedCharIndex;
        for (int index = 0; index < plainText.length(); index++) {
            charToEncrypt = plainText.charAt(index);
            charToEncryptIndex = this.alphabetIndex.get(charToEncrypt);

            if (charToEncryptIndex != null) {
                encryptedCharIndex = (charToEncryptIndex + this.offset) % this.alphabet.length;
                sb.append(this.alphabet[encryptedCharIndex]);
            }
        }

        return sb.toString();
    }

    @Override
    public String decryptString(String encryptedText) {
        StringBuilder sb = new StringBuilder();

        char characterToDecrypt;
        Integer characterToDecryptIndex;
        int decryptedCharIndex;
        for (int index = 0; index < encryptedText.length(); index++) {
            characterToDecrypt = encryptedText.charAt(index);
            characterToDecryptIndex = this.alphabetIndex.get(characterToDecrypt);
            if (characterToDecryptIndex != null) {
                decryptedCharIndex = (characterToDecryptIndex - this.offset + this.alphabet.length) % this.alphabet.length;
                sb.append(this.alphabet[decryptedCharIndex]);
            }
        }

        return sb.toString();
    }

    @Override
    public void encryptFile(Path input, Path output) {
        throw new NotImplementedYetException();
    }

    @Override
    public void decryptFile(Path input, Path output) {
        throw new NotImplementedYetException();
    }
}
