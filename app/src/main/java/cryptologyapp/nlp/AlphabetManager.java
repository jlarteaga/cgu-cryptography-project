package cryptologyapp.nlp;

import cryptologyapp.files.FileValidator;
import cryptologyapp.util.Strings;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class AlphabetManager {

    /**
     * Included to hide the implicit public constructor
     */
    private AlphabetManager() {
    }

    public static void save(Alphabet alphabet, Path filePath) {
        filePath = filePath.toAbsolutePath();
        FileValidator.validateThatDoesNotExist(filePath);

        try {
            Files.createDirectories(filePath.getParent());
            Files.createFile(filePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try (FileWriter fileWriter = new FileWriter(filePath.toFile())) {
            fileWriter.write(Strings.join("", alphabet.getAlphabetSet()));
            fileWriter.write('\n');
            fileWriter.write(alphabet.getSeparator());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Alphabet load(Path filePath) {
        filePath = filePath.toAbsolutePath();
        FileValidator.validateThatExists(filePath);
        FileValidator.validateThatIsRegularFile(filePath);

        try (FileReader fileReader = new FileReader(filePath.toFile());
             Scanner scanner = new Scanner(fileReader)) {
            String alphabetAsString = scanner.nextLine();
            String separator = scanner.nextLine();
            if (separator.length() > 1) {
                throw new IllegalStateException(String.format("Invalid separator '%s'", separator));
            }
            return new Alphabet(alphabetAsString.toCharArray(), separator.charAt(0));
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (NoSuchElementException | IllegalStateException e) {
            throw new InvalidFileStructure();
        }
    }
}
