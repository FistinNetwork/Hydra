package fr.fistin.hydra.configuration;

import fr.fistin.hydra.configuration.nested.HydraRedisConfiguration;

/**
 * Project: Hydra
 * Created by AstFaster
 * on 29/12/2021 at 10:23
 */
public class HydraConfiguration {

    private final HydraRedisConfiguration redisConfiguration;

    public HydraConfiguration(HydraRedisConfiguration redisConfiguration) {
        this.redisConfiguration = redisConfiguration;
    }

    public HydraRedisConfiguration getRedisConfiguration() {
        return this.redisConfiguration;
    }

    public static HydraConfiguration load() {
        System.out.println("Loading configuration...");

        return new HydraConfiguration(HydraRedisConfiguration.load());
    }

}
