package cryptologyapp.nlp;

public class InvalidFileStructure extends IllegalArgumentException {
    public InvalidFileStructure() {
        super("Invalid file structure");
    }
}
