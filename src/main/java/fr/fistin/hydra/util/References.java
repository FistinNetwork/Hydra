package fr.fistin.hydra.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public class References {

    /** Global */
    public static final String NAME = "Hydra";

    /** Redis */
    public static final String REDIS_KEY = "hydra:";

    /** Docker */
    public static final String STACK_NAMESPACE_LABEL = "com.docker.stack.namespace";
    public static final String STACK_NAME = System.getenv("STACK_NAME");

    /** Files */
    public static final Path LOG_FOLDER = Paths.get("logs");
    public static final Path LOG_FILE = Paths.get(LOG_FOLDER.toString(), "hydra.log");

    /** Other */
    public static final Gson GSON = new GsonBuilder().create();

}
