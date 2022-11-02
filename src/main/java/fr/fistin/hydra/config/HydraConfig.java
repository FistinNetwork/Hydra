package fr.fistin.hydra.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import fr.fistin.hydra.api.protocol.data.RedisData;

import java.io.IOException;
import java.nio.file.Paths;

/**
 * Project: Hydra
 * Created by AstFaster
 * on 29/12/2021 at 10:23
 */
public class HydraConfig {

    private final RedisData redis;
    private final DockerConfig docker;

    public HydraConfig(RedisData redis, DockerConfig docker) {
        this.redis = redis;
        this.docker = docker;
    }

    public RedisData getRedis() {
        return this.redis;
    }

    public DockerConfig getDocker() {
        return this.docker;
    }

    public static HydraConfig load() {
        System.out.println("Loading configuration...");

        final ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

        try {
            return mapper.readValue(Paths.get("./config.yml").toFile(), HydraConfig.class);
        } catch (IOException e) {
            throw new RuntimeException("An error occurred while loading Hydra config!", e);
        }
    }

}
