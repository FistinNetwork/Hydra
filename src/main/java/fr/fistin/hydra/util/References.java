package fr.fistin.hydra.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public class References {

    public static final String NAME = "Hydra";

    public static final String HYDRA_REDIS_KEY = "hydra:";

    public static final Gson GSON = new GsonBuilder().create();

    public static final Path CONFIG_FILE = Paths.get("hydra-config.yml");
    public static final Path LOG_FOLDER = Paths.get("logs");
    public static final Path LOG_FILE = Paths.get(LOG_FOLDER.toString(), "hydra.log");

}
