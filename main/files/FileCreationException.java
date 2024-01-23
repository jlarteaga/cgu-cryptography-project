package main.files;

import java.nio.file.Path;

public class FileCreationException extends RuntimeException {

    public FileCreationException(Path path) {
        super(String.format("Could not create the output file with path %s}",
                path.toString()));
    }
}
