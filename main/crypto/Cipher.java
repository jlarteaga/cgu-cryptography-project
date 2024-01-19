package main.crypto;

import java.nio.file.Path;

public interface Cipher {
    void encryptFile(Path input, Path output);

    void decryptFile(Path input, Path output);

    String encryptString(String plainText);

    String decryptString(String encryptedText);
}
