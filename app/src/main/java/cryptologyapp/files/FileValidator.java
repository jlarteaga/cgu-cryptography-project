package cryptologyapp.files;

import java.nio.file.Files;
import java.nio.file.Path;

public class FileValidator {

    public static final String PATH_IS_DIRECTORY_ERROR_MESSAGE = "The path %s is a directory";
    public static final String PATH_IS_NOT_REGULAR_FILE_ERROR_MESSAGE = "The path %s is not a regular file";
    public static final String FILE_ALREADY_EXISTS_ERROR_MESSAGE = "The path %s already exists";
    public static final String FILE_NOT_FOUND_ERROR_MESSAGE = "The path %s does not exists";

    /**
     * Included to hide the implicit public constructor
     */
    private FileValidator() {
    }

    public static void validateThatIsDirectory(Path path) {
        if (!Files.isDirectory(path)) {
            throw new RuntimeException(String.format(PATH_IS_DIRECTORY_ERROR_MESSAGE, path));
        }
    }

    public static void validateThatIsRegularFile(Path path) {
        if (!Files.isRegularFile(path)) {
            throw new RuntimeException(String.format(PATH_IS_NOT_REGULAR_FILE_ERROR_MESSAGE, path.toAbsolutePath()));
        }
    }

    public static void validateThatExists(Path path) {
        if (Files.notExists(path)) {
            throw new RuntimeException(String.format(FILE_NOT_FOUND_ERROR_MESSAGE, path.toAbsolutePath()));
        }
    }

    public static void validateThatDoesNotExist(Path path) {
        if (Files.exists(path)) {
            throw new RuntimeException(String.format(FILE_ALREADY_EXISTS_ERROR_MESSAGE, path.toAbsolutePath()));
        }
    }
}
