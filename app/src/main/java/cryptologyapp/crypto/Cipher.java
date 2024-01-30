package cryptologyapp.crypto;

public interface Cipher {

    String encrypt(String plaintext);

    String decrypt(String encryptedText);
}
