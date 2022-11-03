package fr.fistin.hydra.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import fr.fistin.hydra.api.protocol.data.RedisData;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Project: Hydra
 * Created by AstFaster
 * on 29/12/2021 at 10:23
 */
public class HydraConfig {

    private RedisData redis;
    private KubernetesConfig kubernetes;

    private HydraConfig() {}

    public HydraConfig(RedisData redis, KubernetesConfig kubernetes) {
        this.redis = redis;
        this.kubernetes = kubernetes;
    }

    public RedisData getRedis() {
        return this.redis;
    }

    public KubernetesConfig getKubernetes() {
        return this.kubernetes;
    }

    public static HydraConfig load() {
        System.out.println("Loading configuration...");

        final ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

        try {
            final File file = Paths.get("./config.yml").toFile();

            if (file.exists()) {
                return mapper.readValue(file, HydraConfig.class);
            }

            final HydraConfig config = new HydraConfig(new RedisData("localhost", 6379, ""), new KubernetesConfig("default"));

            mapper.writeValue(file, config);

            return config;
        } catch (IOException e) {
            throw new RuntimeException("An error occurred while loading Hydra config!", e);
        }
    }

}
