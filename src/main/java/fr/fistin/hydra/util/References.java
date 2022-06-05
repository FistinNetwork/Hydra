package fr.fistin.hydra.util;

import java.nio.file.Path;
import java.nio.file.Paths;

public class References {

    /** Global */
    public static final String NAME = "Hydra";

    /** Docker */
    public static final String STACK_NAMESPACE_LABEL = "com.docker.stack.namespace";

    /** Files */
    public static final Path LOG_FOLDER = Paths.get("logs");
    public static final Path LOG_FILE = Paths.get(LOG_FOLDER.toString(), "hydra.log");
    public static final Path PRIVATE_KEY_FILE = Paths.get("private.key");
    public static final Path IMAGES_FOLDER = Paths.get("images");

}
