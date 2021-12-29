package fr.fistin.hydra.configuration.nested;

import fr.fistin.hydra.util.References;

/**
 * Project: Hydra
 * Created by AstFaster
 * on 29/12/2021 at 10:24
 */
public class HydraRedisConfiguration {

    private final String redisHost;
    private final short redisPort;
    private final String redisPassword;

    public HydraRedisConfiguration(String redisHost, short redisPort, String redisPassword) {
        this.redisHost = redisHost;
        this.redisPort = redisPort;
        this.redisPassword = redisPassword;
    }

    public String getRedisHost() {
        return this.redisHost;
    }

    public short getRedisPort() {
        return this.redisPort;
    }

    public String getRedisPassword() {
        return this.redisPassword;
    }

    public static HydraRedisConfiguration load() {
        System.out.println("Loading Redis configuration...");

        return new HydraRedisConfiguration(References.STACK_NAME + "_" + System.getenv("REDIS_HOST"), Short.parseShort(System.getenv("REDIS_PORT")), System.getenv("REDIS_PASS"));
    }

}
