package cryptologyapp.nlp;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NGramLanguageModel {
    private String language;
    private int nGramSize;
    private Alphabet alphabet;
    private Map<String, Integer> frequencyMap;
    private Map<String, Double> probabilityMap;
    private Normalizer normalizer;
    private double smoothingConstant;

    public NGramLanguageModel(String language, int nGramSize, Alphabet alphabet, Map<String, Integer> frequencyMap, double smoothingConstant) {
        NGramLanguageModel.validateNGramSize(nGramSize);
        NGramLanguageModel.validateFrequencyMap(frequencyMap, nGramSize);
        NGramLanguageModel.validateSmoothingConstant(smoothingConstant);

        this.normalizer = new Normalizer(alphabet);
        this.language = language;
        this.nGramSize = nGramSize;
        this.alphabet = alphabet;
        this.frequencyMap = frequencyMap;
        this.probabilityMap = NGramLanguageModel.generateProbabilityMap(frequencyMap, nGramSize);
        this.smoothingConstant = smoothingConstant;
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

    private static void validateSmoothingConstant(double smoothingConstant) {
        if (smoothingConstant < 0.0 || smoothingConstant > 1.0) {
            throw new IllegalArgumentException("Smoothing constant must be between 0.0 and 1.0");
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

        /**
         * Calculate probability of last letter according to the base frequency
         */
        for (Map.Entry<String, Integer> entry : frequencyMap.entrySet()) {
            String key = entry.getKey();
            String baseKey = entry.getKey().substring(0, nGramBaseSize);
            probabilityMap.compute(key, (k, v) -> 1.0 * entry.getValue() / baseFrequencyMap.get(baseKey));
        }

        return probabilityMap;
    }

    public static Map<String, Integer> buildFrequencyMap(List<String> input, Alphabet alphabet, int nGramSize) {
        Map<String, Integer> frequencyMap = new HashMap<>();

        Normalizer normalizer = new Normalizer(alphabet);
        input = input.stream().map(normalizer::normalize).toList();

        for (String line : input) {
            int nGramCount = calculateNumberOfNGrams(line.length(), nGramSize);
            for (int i = 0; i < nGramCount; i++) {
                String nGram = line.substring(i, i + nGramSize);
                frequencyMap.compute(nGram, (k, v) -> (v == null ? 0 : v) + 1);
            }
        }

        return frequencyMap;
    }

    public static int calculateNumberOfNGrams(int normalizedInputSize, int nGramSize) {
        return normalizedInputSize - (nGramSize - 1);
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
        int nGramCount = NGramLanguageModel.calculateNumberOfNGrams(input.length(), this.nGramSize);
        for (int i = 0; i < nGramCount; i++) {
            String nGram = input.substring(i, i + this.nGramSize);
            probability *= this.probabilityMap.getOrDefault(nGram, this.getSmoothingConstant());
        }
        return probability;
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

    public Map<String, Integer> getFrequencyMap() {
        return Collections.unmodifiableMap(this.frequencyMap);
    }

    public Map<String, Double> getProbabilityMap() {
        return Collections.unmodifiableMap(this.probabilityMap);
    }

    public double getSmoothingConstant() {
        return smoothingConstant;
    }
}
