package main.crypto;

public class InvalidAlphabetSizeException extends IllegalArgumentException {

    private static final String MESSAGE = "The alphabet must have at least %d characters";

    public InvalidAlphabetSizeException(int minSize) {
        super(String.format(InvalidAlphabetSizeException.MESSAGE, minSize));
    }
}
