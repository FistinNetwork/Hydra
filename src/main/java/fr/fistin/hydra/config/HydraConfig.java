package fr.fistin.hydra.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.IOException;
import java.nio.file.Paths;

/**
 * Project: Hydra
 * Created by AstFaster
 * on 29/12/2021 at 10:23
 */
public class HydraConfig {

    private static HydraConfig instance;

    private final HydraRedisConfig redis;
    private final HydraDockerConfig docker;

    public HydraConfig(HydraRedisConfig redis, HydraDockerConfig docker) {
        this.redis = redis;
        this.docker = docker;
    }

    public HydraRedisConfig getRedis() {
        return this.redis;
    }

    public HydraDockerConfig getDocker() {
        return this.docker;
    }

    public static HydraConfig load() {
        System.out.println("Loading configuration...");

        final ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

        try {
            return instance = mapper.readValue(Paths.get("./config.yml").toFile(), HydraConfig.class);
        } catch (IOException e) {
            throw new RuntimeException("An error occurred while loading Hydra config!", e);
        }
    }

    public static HydraConfig get() {
        return instance;
    }

}
