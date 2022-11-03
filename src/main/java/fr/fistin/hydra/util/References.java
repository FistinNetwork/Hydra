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

}
