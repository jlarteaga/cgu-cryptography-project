package test;

import main.crypto.CaesarCipher;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;

public class CaesarCipherTester {

    private static final char[] alphabet = {
            'a',
            'b',
            'c',
            'd',
            'e',
            'f'
    };
    private static final String PLAIN_TEXT_TEST = "abcdef";
    private static final String PLAIN_TEXT_TEST_SHIFTED_LEFT = "bcdefa";
    private static final String PLAIN_TEXT_TEST_SHIFTED_RIGHT = "fabcde";

    public static void main(String[] args) {
        Instant startedAt = BaseTester.startTest(CaesarCipherTester.class.getName());

        CaesarCipher cipher = new CaesarCipher(CaesarCipherTester.alphabet, 0);

        testCipherWithExtraneousCharacter(cipher);
        testBasicTransformations(cipher);
        testBorderCasesForOffsets(cipher);
        testFileTransformations(cipher);

        BaseTester.finishTest(startedAt);
    }

    private static void testCipherWithExtraneousCharacter(CaesarCipher cipher) {
        cipher.setOffset(0);
        BaseTester.test("[encryptString] should ignore characters outside the alphabet",
                cipher.encryptString("aqbwcrdteyf,!@%"),
                PLAIN_TEXT_TEST);
        BaseTester.test("[decryptString] should ignore characters outside the alphabet",
                cipher.decryptString("aqbwcrdteyf,!@%"),
                PLAIN_TEXT_TEST);
    }

    private static void testBasicTransformations(CaesarCipher cipher) {
        cipher.setOffset(1);
        BaseTester.test("[encryptString] should move all characters one position forward",
                cipher.encryptString(PLAIN_TEXT_TEST),
                PLAIN_TEXT_TEST_SHIFTED_LEFT);
        BaseTester.test("[decryptString] should move all characters one position backward",
                cipher.decryptString(PLAIN_TEXT_TEST),
                PLAIN_TEXT_TEST_SHIFTED_RIGHT);
    }

    private static void testBorderCasesForOffsets(CaesarCipher cipher) {
        cipher.setOffset(CaesarCipherTester.alphabet.length);
        BaseTester.test(
                "[encryptString] should return the same string when the offset is equals to the size of the alphabet",
                cipher.decryptString(PLAIN_TEXT_TEST),
                PLAIN_TEXT_TEST);
        BaseTester.test(
                "[decryptString] should return the same string when the offset is equals to the size of the alphabet",
                cipher.decryptString(PLAIN_TEXT_TEST),
                PLAIN_TEXT_TEST);

        cipher.setOffset(-1);
        BaseTester.test(
                "[encryptString] should gracefully handle negative offsets (1/2)",
                cipher.encryptString(PLAIN_TEXT_TEST),
                PLAIN_TEXT_TEST_SHIFTED_RIGHT);
        cipher.setOffset(-CaesarCipherTester.alphabet.length - 1);
        BaseTester.test(
                "[encryptString] should gracefully handle negative offsets (2/2)",
                cipher.encryptString(PLAIN_TEXT_TEST),
                PLAIN_TEXT_TEST_SHIFTED_RIGHT);
        cipher.setOffset(CaesarCipherTester.alphabet.length + 1);
        BaseTester.test(
                "[encryptString] should gracefully handle positive offsets greater than the size of the alphabet",
                cipher.encryptString(PLAIN_TEXT_TEST),
                PLAIN_TEXT_TEST_SHIFTED_LEFT);

        cipher.setOffset(-1);
        BaseTester.test(
                "[decryptString] should gracefully handle negative offsets (1/2)",
                cipher.decryptString(PLAIN_TEXT_TEST),
                PLAIN_TEXT_TEST_SHIFTED_LEFT);
        cipher.setOffset(-CaesarCipherTester.alphabet.length - 1);
        BaseTester.test(
                "[decryptString] should gracefully handle negative offsets (2/2)",
                cipher.decryptString(PLAIN_TEXT_TEST),
                PLAIN_TEXT_TEST_SHIFTED_LEFT);
        cipher.setOffset(CaesarCipherTester.alphabet.length + 1);
        BaseTester.test(
                "[decryptString] should gracefully handle positive offsets greater than the size of the alphabet",
                cipher.decryptString(PLAIN_TEXT_TEST),
                PLAIN_TEXT_TEST_SHIFTED_RIGHT);
    }

    private static void testFileTransformations(CaesarCipher cipher) {
        Path test1InputFile = Path.of("test1-input.txt");
        Path test1OutputFile = Path.of("test1-output.txt");
        cipher.setOffset(1);

        try {
            cipher.encryptFile(test1InputFile, test1OutputFile);
            String result = Files.readString(test1OutputFile);
            BaseTester.test("[encodeFile] should shift test1-input.txt characters one position forward",
                    result,
                    PLAIN_TEXT_TEST_SHIFTED_LEFT);
        } catch (Exception e) {
            BaseTester.test("[encodeFile] should shift test1-input.txt characters one position forward",
                    e.toString(),
                    PLAIN_TEXT_TEST_SHIFTED_LEFT);
            e.printStackTrace();
        }

        try {
            Files.deleteIfExists(test1OutputFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Path test2InputFile = Path.of("test2-input.txt");
        Path test2OutputFile = Path.of("test2-output.txt");
        try {
            cipher.setOffset(1);
            cipher.decryptFile(test2InputFile, test2OutputFile);
            String result = Files.readString(test2OutputFile);
            BaseTester.test("[encodeFile] should shift test2-input.txt characters one position backward",
                    result,
                    PLAIN_TEXT_TEST);
        } catch (Exception e) {
            BaseTester.test("[encodeFile] should shift test1-input.txt characters one position backward",
                    e.toString(),
                    PLAIN_TEXT_TEST);
            e.printStackTrace();
        }

        try {
            Files.deleteIfExists(test2OutputFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
