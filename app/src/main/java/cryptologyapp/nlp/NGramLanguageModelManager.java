package cryptologyapp.nlp;

import cryptologyapp.util.NotImplementedYetException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
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

    public static void save(NGramLanguageModel model, Path file) {
        throw new NotImplementedYetException();
    }

    public static NGramLanguageModel train(Path input, String language, int nGramSize, char[] alphabetCharacters, char separator, double smoothingConstant) {
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
            throw new RuntimeException(e);
        }
    }
}
