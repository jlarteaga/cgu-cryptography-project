package main.files;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.UnaryOperator;

public class FileProcessor {

    private int batchSize;
    private Charset defaultCharset;

    public FileProcessor(int batchSize, Charset defaultCharset) {
        FileProcessor.validateBatchSize(batchSize);
        FileProcessor.validateCharset(defaultCharset);
        this.batchSize = batchSize;
        this.defaultCharset = defaultCharset;
    }

    private static void validateBatchSize(int batchSize) {
        if (batchSize < 1) {
            throw new IllegalArgumentException("Batch processing size should be at least 1");
        }
    }

    private static void validateCharset(Charset charset) {
        if (charset == null) {
            throw new IllegalArgumentException("the charset must not be null");
        }
    }

    public int getBatchSize() {
        return this.batchSize;
    }

    public void setBatchSize(int batchSize) {
        FileProcessor.validateBatchSize(batchSize);
        this.batchSize = batchSize;
    }

    public Charset getDefaultCharset() {
        return this.defaultCharset;
    }

    public void setDefaultCharset(Charset defaultCharset) {
        FileProcessor.validateCharset(defaultCharset);
        this.defaultCharset = defaultCharset;
    }

    public void transformFile(Path input, Path output, UnaryOperator<String> transformer) {
        FileValidator.validateThatExists(input);
        FileValidator.validateThatIsRegularFile(input);
        FileValidator.validateThatDoesNotExist(output);

        ByteBuffer inputBuffer = ByteBuffer.allocate(this.batchSize);
        ByteBuffer outputBuffer = ByteBuffer.allocate(this.batchSize);

        Path createdOutput;

        try {
            createdOutput = Files.createFile(output);
        } catch (IOException e) {
            throw new FileCreationException(output);
        }

        try (RandomAccessFile inputFile = new RandomAccessFile(input.toString(),
                "r"); RandomAccessFile outputFile = new RandomAccessFile(createdOutput.toString(),
                "rw"); SeekableByteChannel inputChannel = inputFile.getChannel(); SeekableByteChannel outputChannel = outputFile.getChannel()) {
            int readBytes = inputChannel.read(inputBuffer);
            while (readBytes > -1) {
                inputBuffer.flip();
                String inputString = new String(inputBuffer.array(), this.defaultCharset);
                String outputString = transformer.apply(inputString);
                outputBuffer.put(outputString.getBytes(this.defaultCharset));

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
