package cryptologyapp.crypto;

import cryptologyapp.TestingConstants;
import cryptologyapp.files.FileProcessor;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CaesarCipherTest {

    private static final char[] ALPHABET = {'a', 'b', 'c', 'd', 'e', 'f'};
    private static final String PLAIN_TEXT_TEST = "abcdef";
    private static final String PLAIN_TEXT_TEST_SHIFTED_LEFT = "bcdefa";
    private static final String PLAIN_TEXT_TEST_SHIFTED_RIGHT = "fabcde";

    private static final Path TEST_1_INPUT_FILE = TestingConstants.BASE_TEST_RESOURCES_PATH.resolve("test1-input.txt");
    private static final Path TEST_2_INPUT_FILE = TestingConstants.BASE_TEST_RESOURCES_PATH.resolve("test2-input.txt");
    private static final Path TEST_1_OUTPUT_FILE = TestingConstants.BASE_TEST_RESOURCES_PATH.resolve("test1-output.txt");
    private static final Path TEST_2_OUTPUT_FILE = TestingConstants.BASE_TEST_RESOURCES_PATH.resolve("test2-output.txt");

    @Test
    void testCipherWithExtraneousCharacter() {
        CaesarCipher cipher = new CaesarCipher(CaesarCipherTest.ALPHABET, 0);
        String stringWithExtraneousCharacters = "aqbwcrdteyf,!@%";
        assertEquals(PLAIN_TEXT_TEST,
                cipher.encrypt(stringWithExtraneousCharacters),
                "[encrypt] should ignore characters outside the alphabet");
        assertEquals(PLAIN_TEXT_TEST,
                cipher.decrypt(stringWithExtraneousCharacters),
                "[decrypt] should ignore characters outside the alphabet");
    }

    @Test
    void testBasicTransformations() {
        CaesarCipher cipher = new CaesarCipher(CaesarCipherTest.ALPHABET, 1);
        assertEquals(PLAIN_TEXT_TEST_SHIFTED_LEFT,
                cipher.encrypt(PLAIN_TEXT_TEST),
                "[encrypt] should move all characters one position forward");
        assertEquals(PLAIN_TEXT_TEST_SHIFTED_RIGHT,
                cipher.decrypt(PLAIN_TEXT_TEST),
                "[decrypt] should move all characters one position backward");
    }

    @Test
    void testBorderCasesForOffsets() {
        CaesarCipher cipher = new CaesarCipher(CaesarCipherTest.ALPHABET, CaesarCipherTest.ALPHABET.length);
        assertEquals(PLAIN_TEXT_TEST,
                cipher.decrypt(PLAIN_TEXT_TEST),
                "[encrypt] should return the same string when the offset is equals to the size of the alphabet");
        assertEquals(PLAIN_TEXT_TEST,
                cipher.decrypt(PLAIN_TEXT_TEST),
                "[decrypt] should return the same string when the offset is equals to the size of the alphabet");

        cipher.setOffset(-1);
        assertEquals(PLAIN_TEXT_TEST_SHIFTED_RIGHT,
                cipher.encrypt(PLAIN_TEXT_TEST),
                "[encrypt] should gracefully handle negative offsets (1/2)");

        cipher.setOffset(-CaesarCipherTest.ALPHABET.length - 1);
        assertEquals(PLAIN_TEXT_TEST_SHIFTED_RIGHT,
                cipher.encrypt(PLAIN_TEXT_TEST),
                "[encrypt] should gracefully handle negative offsets (2/2)");
        cipher.setOffset(CaesarCipherTest.ALPHABET.length + 1);
        assertEquals(PLAIN_TEXT_TEST_SHIFTED_LEFT,
                cipher.encrypt(PLAIN_TEXT_TEST),
                "[encrypt] should gracefully handle positive offsets greater than the size of the alphabet");

        cipher.setOffset(-1);
        assertEquals(PLAIN_TEXT_TEST_SHIFTED_LEFT,
                cipher.decrypt(PLAIN_TEXT_TEST),
                "[decrypt] should gracefully handle negative offsets (1/2)");
        cipher.setOffset(-CaesarCipherTest.ALPHABET.length - 1);
        assertEquals(PLAIN_TEXT_TEST_SHIFTED_LEFT,
                cipher.decrypt(PLAIN_TEXT_TEST),
                "[decrypt] should gracefully handle negative offsets (2/2)");
        cipher.setOffset(CaesarCipherTest.ALPHABET.length + 1);
        assertEquals(PLAIN_TEXT_TEST_SHIFTED_RIGHT,
                cipher.decrypt(PLAIN_TEXT_TEST),
                "[decrypt] should gracefully handle positive offsets greater than the size of the alphabet");
    }

    @Test
    void testFileTransformations() {
        CaesarCipher cipher = new CaesarCipher(CaesarCipherTest.ALPHABET, 0);

        FileProcessor fileProcessor = new FileProcessor(1024, StandardCharsets.UTF_8);
        cipher.setOffset(1);

        try {
            fileProcessor.transformFile(TEST_1_INPUT_FILE, TEST_1_OUTPUT_FILE, cipher::encrypt);
            String result = Files.readString(TEST_1_OUTPUT_FILE);
            assertEquals(PLAIN_TEXT_TEST_SHIFTED_LEFT,
                    result,
                    "[encodeFile] should shift test1-input.txt characters one position forward");
        } catch (Exception e) {
            assertEquals(PLAIN_TEXT_TEST_SHIFTED_LEFT,
                    e.toString(),
                    "[encodeFile] should shift test1-input.txt characters one position forward");
            e.printStackTrace();
        }

        try {
            Files.deleteIfExists(TEST_1_OUTPUT_FILE);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            cipher.setOffset(1);
            fileProcessor.transformFile(TEST_2_INPUT_FILE, TEST_2_OUTPUT_FILE, cipher::decrypt);
            String result = Files.readString(TEST_2_OUTPUT_FILE);
            assertEquals(PLAIN_TEXT_TEST,
                    result,
                    "[encodeFile] should shift test2-input.txt characters one position backward");
        } catch (Exception e) {
            assertEquals(PLAIN_TEXT_TEST,
                    e.toString(),
                    "[encodeFile] should shift test1-input.txt characters one position backward");
            e.printStackTrace();
        }

        try {
            Files.deleteIfExists(TEST_2_OUTPUT_FILE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
