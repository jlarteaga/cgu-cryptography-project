package cryptologyapp.crypto;

public class AlphabetNotProvidedException extends IllegalArgumentException {

    public AlphabetNotProvidedException() {
        super("An alphabet must be provided");
    }
}
