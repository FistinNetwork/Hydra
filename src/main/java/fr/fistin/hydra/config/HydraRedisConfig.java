package fr.fistin.hydra.config;

import fr.fistin.hydra.util.References;

/**
 * Project: Hydra
 * Created by AstFaster
 * on 29/12/2021 at 10:24
 */
public class HydraRedisConfig {

    private final String hostname;
    private final short port;
    private final String password;

    public HydraRedisConfig(String hostname, short port, String password) {
        this.hostname = hostname;
        this.port = port;
        this.password = password;
    }

    public String getHost() {
        return this.hostname;
    }

    public short getPort() {
        return this.port;
    }

    public String getPassword() {
        return this.password;
    }

    public static HydraRedisConfig load() {
        System.out.println("Loading Redis configuration...");

        return new HydraRedisConfig(References.STACK_NAME + "_" + System.getenv("REDIS_HOST"), Short.parseShort(System.getenv("REDIS_PORT")), System.getenv("REDIS_PASS"));
    }

}
