package cryptologyapp.nlp;

import java.util.HashMap;
import java.util.Map;

public class NGramLanguageModel {
    private String language;
    private int nGramSize;
    private Alphabet alphabet;
    private Map<String, Double> probabilityMap;
    private Normalizer normalizer;

    public NGramLanguageModel(String language, int nGramSize, Alphabet alphabet, Map<String, Integer> frequencyMap) {
        NGramLanguageModel.validateNGramSize(nGramSize);
        NGramLanguageModel.validateFrequencyMap(frequencyMap, nGramSize);

        this.normalizer = new Normalizer(alphabet);
        this.language = language;
        this.nGramSize = nGramSize;
        this.alphabet = alphabet;
        this.probabilityMap = NGramLanguageModel.generateProbabilityMap(frequencyMap, nGramSize);
    }

    private static void validateNGramSize(int nGramSize) {
        if (nGramSize < 2) {
            throw new IllegalArgumentException("NGramSize must be greater or equal than 2");
        }
    }

    private static void validateFrequencyMap(Map<String, Integer> frequencyMap, int nGramSize) {
        if (frequencyMap == null) {
            throw new IllegalArgumentException("The frequencyMap can not be null");
        }

        if (frequencyMap.size() == 0) {
            throw new IllegalArgumentException("The frequencyMap must have at least one entry");
        }

        for (Map.Entry<String, Integer> entry : frequencyMap.entrySet()) {
            String key = entry.getKey();
            if (key.length() != nGramSize) {
                throw new IllegalArgumentException(String.format(
                        "All the keys of frequencyMap must have the size of nGramSize (Key '%s' has size %d)",
                        key,
                        key.length()));
            }
            Integer frequency = entry.getValue();
            if (frequency == null || frequency < 0) {
                throw new IllegalArgumentException(String.format("Invalid value for the frequency of key '%s': %s",
                        key,
                        frequency));
            }
        }

    }

    private static Map<String, Double> generateProbabilityMap(Map<String, Integer> frequencyMap, int nGramSize) {
        Map<String, Double> probabilityMap = new HashMap<>();
        final int nGramBaseSize = nGramSize - 1;

        /**
         * Load base frequencies
         */
        Map<String, Integer> baseFrequencyMap = new HashMap<>();
        for (Map.Entry<String, Integer> entry : frequencyMap.entrySet()) {
            Integer frequency = entry.getValue();
            String baseKey = entry.getKey().substring(0, nGramBaseSize);
            baseFrequencyMap.compute(baseKey, (k, v) -> (v == null ? 0 : v) + frequency);
        }

        for (Map.Entry<String, Integer> entry : frequencyMap.entrySet()) {
            String key = entry.getKey();
            String baseKey = entry.getKey().substring(0, nGramBaseSize);
            probabilityMap.compute(key, (k, v) -> 1.0 * entry.getValue() / baseFrequencyMap.get(baseKey));
        }

        return probabilityMap;
    }

    public double calculatePerplexity(String input, boolean isNormalized) {
        if (!isNormalized) {
            input = this.normalizer.normalize(input);
        }
        double probability = this.calculateProbability(input, true);
        if (probability == 0) {
            return Double.POSITIVE_INFINITY;
        }
        return Math.pow(probability, -1.0 / input.length());
    }

    public double calculateProbability(String input, boolean isNormalized) {
        if (!isNormalized) {
            input = this.normalizer.normalize(input);
        }
        double probability = 1.0;
        int nGramCount = this.calculateNumberOfNGrams(input.length());
        for (int i = 0; i < nGramCount; i++) {
            String nGram = input.substring(i, i + this.nGramSize);
            probability *= this.probabilityMap.getOrDefault(nGram, 0.0);
        }
        return probability;
    }

    public int calculateNumberOfNGrams(int normalizedInputSize) {
        return normalizedInputSize - (this.nGramSize - 1);
    }

    public String getLanguage() {
        return language;
    }

    public int getNGramSize() {
        return nGramSize;
    }

    public Alphabet getAlphabet() {
        return alphabet;
    }

    public Map<String, Double> getProbabilityMap() {
        return probabilityMap;
    }
}
