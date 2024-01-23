package main.crypto;

public interface Cipher {

    String encrypt(String plaintext);

    String decrypt(String encryptedText);
}
