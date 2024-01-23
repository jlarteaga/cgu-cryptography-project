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
    private static final String PLAIN_TEXT_TEST = "abcdef";
    private static final String PLAIN_TEXT_TEST_SHIFTED_LEFT = "bcdefa";
    private static final String PLAIN_TEXT_TEST_SHIFTED_RIGHT = "fabcde";

    public static void main(String[] args) {
        Instant startedAt = BaseTester.startTest(CaesarCipherTester.class.getName());

        CaesarCipher cipher = new CaesarCipher(CaesarCipherTester.alphabet, 0);

        testCipherWithExtraneousCharacter(cipher);
        testBasicTransformations(cipher);
        testBorderCasesForOffsets(cipher);

        BaseTester.finishTest(startedAt);
    }

    private static void testCipherWithExtraneousCharacter(CaesarCipher cipher) {
        cipher.setOffset(0);
        BaseTester.test("[encrypt] should ignore characters outside the alphabet",
                cipher.encrypt("aqbwcrdteyf,!@%"),
                PLAIN_TEXT_TEST);
        BaseTester.test("[decrypt] should ignore characters outside the alphabet",
                cipher.decrypt("aqbwcrdteyf,!@%"),
                PLAIN_TEXT_TEST);
    }

    private static void testBasicTransformations(CaesarCipher cipher) {
        cipher.setOffset(1);
        BaseTester.test("[encrypt] should move all characters one position forward",
                cipher.encrypt(PLAIN_TEXT_TEST),
                PLAIN_TEXT_TEST_SHIFTED_LEFT);
        BaseTester.test("[decrypt] should move all characters one position backward",
                cipher.decrypt(PLAIN_TEXT_TEST),
                PLAIN_TEXT_TEST_SHIFTED_RIGHT);
    }

    private static void testBorderCasesForOffsets(CaesarCipher cipher) {
        cipher.setOffset(CaesarCipherTester.alphabet.length);
        BaseTester.test(
                "[encrypt] should return the same string when the offset is equals to the size of the alphabet",
                cipher.decrypt(PLAIN_TEXT_TEST),
                PLAIN_TEXT_TEST);
        BaseTester.test(
                "[decrypt] should return the same string when the offset is equals to the size of the alphabet",
                cipher.decrypt(PLAIN_TEXT_TEST),
                PLAIN_TEXT_TEST);

        cipher.setOffset(-1);
        BaseTester.test(
                "[encrypt] should gracefully handle negative offsets (1/2)",
                cipher.encrypt(PLAIN_TEXT_TEST),
                PLAIN_TEXT_TEST_SHIFTED_RIGHT);
        cipher.setOffset(-CaesarCipherTester.alphabet.length - 1);
        BaseTester.test(
                "[encrypt] should gracefully handle negative offsets (2/2)",
                cipher.encrypt(PLAIN_TEXT_TEST),
                PLAIN_TEXT_TEST_SHIFTED_RIGHT);
        cipher.setOffset(CaesarCipherTester.alphabet.length + 1);
        BaseTester.test(
                "[encrypt] should gracefully handle positive offsets greater than the size of the alphabet",
                cipher.encrypt(PLAIN_TEXT_TEST),
                PLAIN_TEXT_TEST_SHIFTED_LEFT);

        cipher.setOffset(-1);
        BaseTester.test(
                "[decrypt] should gracefully handle negative offsets (1/2)",
                cipher.decrypt(PLAIN_TEXT_TEST),
                PLAIN_TEXT_TEST_SHIFTED_LEFT);
        cipher.setOffset(-CaesarCipherTester.alphabet.length - 1);
        BaseTester.test(
                "[decrypt] should gracefully handle negative offsets (2/2)",
                cipher.decrypt(PLAIN_TEXT_TEST),
                PLAIN_TEXT_TEST_SHIFTED_LEFT);
        cipher.setOffset(CaesarCipherTester.alphabet.length + 1);
        BaseTester.test(
                "[decrypt] should gracefully handle positive offsets greater than the size of the alphabet",
                cipher.decrypt(PLAIN_TEXT_TEST),
                PLAIN_TEXT_TEST_SHIFTED_RIGHT);
    }

}
