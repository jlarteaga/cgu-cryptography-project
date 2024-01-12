package test;

import main.crypto.CaesarCipher;

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
    private static final String PLAIN_TEXT_TEST = new String(alphabet);
    private static final String PLAIN_TEXT_TEST_SHIFTED_RIGHT = PLAIN_TEXT_TEST.substring(1,
            PLAIN_TEXT_TEST.length()) + PLAIN_TEXT_TEST.charAt(0);
    private static final String PLAIN_TEXT_TEST_SHIFTED_LEFT = PLAIN_TEXT_TEST.charAt(PLAIN_TEXT_TEST.length() - 1) + PLAIN_TEXT_TEST.substring(
            0,
            PLAIN_TEXT_TEST.length() - 1);

    public static void main(String[] args) {
        Instant startedAt = BaseTester.startTest(CaesarCipherTester.class.getName());

        CaesarCipher cipher = new CaesarCipher(CaesarCipherTester.alphabet, 0);

        BaseTester.test("[encryptString] should ignore characters outside the alphabet",
                cipher.encryptString("aqbwcrdteyf,!@%"),
                PLAIN_TEXT_TEST);
        BaseTester.test("[decryptString] should ignore characters outside the alphabet",
                cipher.decryptString("aqbwcrdteyf,!@%"),
                PLAIN_TEXT_TEST);

        cipher.setOffset(1);
        BaseTester.test("[encryptString] should move all characters one position forward",
                cipher.encryptString(PLAIN_TEXT_TEST),
                PLAIN_TEXT_TEST_SHIFTED_RIGHT);
        BaseTester.test("[decryptString] should move all characters one position backward",
                cipher.decryptString(PLAIN_TEXT_TEST),
                PLAIN_TEXT_TEST_SHIFTED_LEFT);

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
                PLAIN_TEXT_TEST_SHIFTED_LEFT);
        cipher.setOffset(-CaesarCipherTester.alphabet.length - 1);
        BaseTester.test(
                "[encryptString] should gracefully handle negative offsets (2/2)",
                cipher.encryptString(PLAIN_TEXT_TEST),
                PLAIN_TEXT_TEST_SHIFTED_LEFT);
        cipher.setOffset(CaesarCipherTester.alphabet.length + 1);
        BaseTester.test(
                "[encryptString] should gracefully handle positive offsets greater than the size of the alphabet",
                cipher.encryptString(PLAIN_TEXT_TEST),
                PLAIN_TEXT_TEST_SHIFTED_RIGHT);

        cipher.setOffset(-1);
        BaseTester.test(
                "[decryptString] should gracefully handle negative offsets (1/2)",
                cipher.decryptString(PLAIN_TEXT_TEST),
                PLAIN_TEXT_TEST_SHIFTED_RIGHT);
        cipher.setOffset(-CaesarCipherTester.alphabet.length - 1);
        BaseTester.test(
                "[decryptString] should gracefully handle negative offsets (2/2)",
                cipher.decryptString(PLAIN_TEXT_TEST),
                PLAIN_TEXT_TEST_SHIFTED_RIGHT);
        cipher.setOffset(CaesarCipherTester.alphabet.length + 1);
        BaseTester.test(
                "[decryptString] should gracefully handle positive offsets greater than the size of the alphabet",
                cipher.decryptString(PLAIN_TEXT_TEST),
                PLAIN_TEXT_TEST_SHIFTED_LEFT);


        BaseTester.finishTest(CaesarCipherTester.class.getName(), startedAt);
    }

}
