package cryptologyapp.nlp;

import cryptologyapp.files.FileValidator;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class NGramLanguageModelManager {

    /**
     * Included to hide the implicit public constructor
     */
    private NGramLanguageModelManager() {
    }

    public static NGramLanguageModel load(Alphabet alphabet, Path filePath) {
        filePath = filePath.toAbsolutePath();
        FileValidator.validateThatExists(filePath);
        FileValidator.validateThatIsRegularFile(filePath);

        try (FileInputStream fis = new FileInputStream(filePath.toFile());
             InputStreamReader reader = new InputStreamReader(fis);
             BufferedReader bufferedReader = new BufferedReader(reader)) {
            String language;
            String nGramSize;
            Map<String, Integer> frequencyMap = new TreeMap<>();
            String smoothing;

            language = bufferedReader.readLine();
            nGramSize = bufferedReader.readLine();
            smoothing = bufferedReader.readLine();

            if (language == null || nGramSize == null || smoothing == null) {
                throw new NoSuchElementException();
            }

            String nGram = bufferedReader.readLine();
            String frequency = bufferedReader.readLine();
            while (nGram != null && frequency != null) {
                frequencyMap.put(nGram, Integer.parseInt(frequency));
                nGram = bufferedReader.readLine();
                frequency = bufferedReader.readLine();
            }


            return new NGramLanguageModel(language,
                    Integer.parseInt(nGramSize),
                    alphabet,
                    frequencyMap,
                    Double.parseDouble(smoothing));
        } catch (IOException e) {
            // Since file validations were performed before, this block should not be reached.
            throw new RuntimeException(e);
        } catch (NoSuchElementException | IllegalStateException e) {
            throw new InvalidFileStructure();
        }
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

    public static NGramLanguageModel train(Path input, String language, int nGramSize, Alphabet alphabet, double smoothingConstant) {
        FileValidator.validateThatExists(input);
        FileValidator.validateThatIsRegularFile(input);

        try {
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
