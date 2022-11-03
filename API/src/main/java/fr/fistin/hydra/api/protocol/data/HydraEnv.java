package fr.fistin.hydra.api.protocol.data;

import fr.fistin.hydra.api.HydraAPI;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by AstFaster
 * on 03/11/2022 at 13:19
 *
 * Represents the environment variables provided to an application started by Hydra.
 */
public class HydraEnv {

    /** The prefix of all Hydra environment variables */
    private static final String PREFIX = "HYDRA_";

    /** The name of the application started by Hydra */
    private final String name;
    /** The Redis credentials */
    private final RedisData redis;

    /**
     * Constructor of a {@link HydraEnv}
     *
     * @param name The name of the application
     * @param redis The Redis credentials
     */
    public HydraEnv(String name, RedisData redis) {
        this.name = name;
        this.redis = redis;
    }

    /**
     * Get the name of the application started by Hydra
     *
     * @return A name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Get the Redis credentials used to connect to it
     *
     * @return A {@link RedisData} object
     */
    public RedisData getRedis() {
        return this.redis;
    }

    /**
     * Convert the {@link HydraEnv} object to a {@link Map}
     *
     * @return A {@link Map} of environment variables
     */
    public Map<String, String> asMap() {
        return new HashMap<String, String>(){{
            put(PREFIX + "APP", name);
            put(PREFIX + "REDIS", HydraAPI.GSON.toJson(redis));
        }};
    }

    /**
     * Load a {@link HydraEnv} object from the environment variables
     *
     * @return A {@link HydraEnv} object; but might throw an error if not present in environment variables
     */
    public static HydraEnv load() {
        final String name = System.getenv(PREFIX + "APP");
        final RedisData redis = HydraAPI.GSON.fromJson(System.getenv(PREFIX + "REDIS"), RedisData.class);

        return new HydraEnv(name, redis);
    }

}
