package main.crypto;

import main.FileCreationException;
import main.files.FileValidator;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class CaesarCipher implements Cipher {
    private final Map<Character, Integer> alphabetIndex;
    private final char[] alphabet;
    private int offset;
    private int batchSize = 1024;

    public CaesarCipher(char[] alphabet, int offset) {
        this(alphabet);
        this.setOffset(offset);
    }

    private CaesarCipher(char[] alphabet) {
        this.alphabet = Arrays.copyOf(alphabet, alphabet.length);
        this.alphabetIndex = new HashMap<>();
        for (int letterIndex = 0; letterIndex < alphabet.length; letterIndex++) {
            this.alphabetIndex.put(alphabet[letterIndex], letterIndex);
        }
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

    public void setBatchSize(int batchSize) {
        if (batchSize < 1) {
            throw new IllegalArgumentException("Batch size must be a positive number");
        }
        this.batchSize = batchSize;
    }

    @Override
    public void encryptFile(Path input, Path output) {
        this.shiftCharactersInFile(input, output, this.offset);
    }

    @Override
    public void decryptFile(Path input, Path output) {
        this.shiftCharactersInFile(input, output, this.alphabet.length - this.offset);
    }

    @Override
    public String encryptString(String plainText) {
        return this.shiftCharactersInString(plainText, this.offset);
    }

    @Override
    public String decryptString(String encryptedText) {
        return this.shiftCharactersInString(encryptedText, this.alphabet.length - this.offset);
    }

    private Character shiftCharacter(char character, int delta) {
        Integer index = this.alphabetIndex.get(character);

        if (index == null) {
            return null;
        }

        index = (index + delta) % this.alphabet.length;

        return this.alphabet[index];
    }

    private String shiftCharactersInString(String input, int delta) {
        StringBuilder sb = new StringBuilder();

        Character encryptedCharacter;
        for (int index = 0; index < input.length(); index++) {
            encryptedCharacter = this.shiftCharacter(input.charAt(index), delta);
            if (encryptedCharacter != null) {
                sb.append(encryptedCharacter);
            }
        }

        return sb.toString();
    }

    private void shiftCharactersInFile(Path input, Path output, int delta) {
        FileValidator.validateThatExists(input);
        FileValidator.validateThatIsRegularFile(input);
        FileValidator.validateThatDoesNotExist(output);

        ByteBuffer inputBuffer = ByteBuffer.allocate(this.batchSize);
        ByteBuffer outputBuffer = ByteBuffer.allocate(this.batchSize);

        Path createdOutput = null;

        try {
            createdOutput = Files.createFile(output);
        } catch (IOException e) {
            throw new FileCreationException(output);
        }

        try (
                RandomAccessFile inputFile = new RandomAccessFile(input.toString(), "r");
                RandomAccessFile outputFile = new RandomAccessFile(createdOutput.toString(), "rw");
                SeekableByteChannel inputChannel = inputFile.getChannel();
                SeekableByteChannel outputChannel = outputFile.getChannel()
        ) {
            int readBytes = inputChannel.read(inputBuffer);
            while (readBytes > 0) {
                inputBuffer.flip();
                String inputString = new String(inputBuffer.array());
                String outputString = this.shiftCharactersInString(inputString, delta);
                outputBuffer.put(outputString.getBytes());

                outputBuffer.flip();
                outputChannel.write(outputBuffer);

                outputBuffer.clear();
                inputBuffer.clear();

                readBytes = inputChannel.read(inputBuffer);
            }
        } catch (IOException exception) {
            // Since file validations were performed before, this block should not be reached.
            exception.printStackTrace();
        }
    }
}
