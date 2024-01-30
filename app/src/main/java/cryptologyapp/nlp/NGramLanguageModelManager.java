package cryptologyapp.nlp;

import cryptologyapp.util.NotImplementedYetException;

import java.nio.file.Path;

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

    public static NGramLanguageModel train(Path input, String language, int nGramSize, char[] alphabet, char separator, double smoothingConstant) {
        throw new NotImplementedYetException();

    }
}
