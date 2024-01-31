package cryptologyapp.nlp;

import cryptologyapp.files.FileValidator;
import cryptologyapp.util.NotImplementedYetException;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class NGramLanguageModelManager {

    /**
     * Included to hide the implicit public constructor
     */
    private NGramLanguageModelManager() {
    }

    public static NGramLanguageModel load(Path file) {
        throw new NotImplementedYetException();
    }

    public static void save(NGramLanguageModel model, Path filePath) {
        filePath = filePath.toAbsolutePath();
        FileValidator.validateThatDoesNotExist(filePath);
        Map<String, Integer> frequencyMap = model.getFrequencyMap();

        try {
            Files.createDirectories(filePath.getParent());
            Files.createFile(filePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try (FileWriter fileWriter = new FileWriter(filePath.toFile(), false)) {
            fileWriter.write(model.getLanguage());
            fileWriter.write('\n');
            fileWriter.write(Integer.toString(model.getNGramSize()));
            fileWriter.write('\n');
            fileWriter.write(Double.toString(model.getSmoothingConstant()));
            fileWriter.write('\n');

            // Frequency map
            Iterator<Map.Entry<String, Integer>> mapIterator = frequencyMap.entrySet().iterator();
            while (mapIterator.hasNext()) {
                Map.Entry<String, Integer> entry = mapIterator.next();
                fileWriter.write(String.format("%s%n%s", entry.getKey(), entry.getValue()));
                if (mapIterator.hasNext()) {
                    fileWriter.write('\n');
                }
            }

            fileWriter.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public static NGramLanguageModel train(Path input, String language, int nGramSize, char[] alphabetCharacters, char separator, double smoothingConstant) {
        FileValidator.validateThatExists(input);
        FileValidator.validateThatIsRegularFile(input);

        try {
            Alphabet alphabet = new Alphabet(alphabetCharacters, separator);
            List<String> fileLines = Files.readAllLines(input);
            Map<String, Integer> frequencyMap = NGramLanguageModel.buildFrequencyMap(fileLines, alphabet, nGramSize);
            return new NGramLanguageModel(language,
                    nGramSize,
                    alphabet,
                    frequencyMap,
                    smoothingConstant);
        } catch (IOException e) {
            // Since file validations were performed before, this block should not be reached.
            throw new RuntimeException(e);
        }
    }
}
