package fr.fistin.hydra.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import fr.fistin.hydra.api.event.HydraEventBus;
import fr.fistin.hydra.api.protocol.HydraConnection;
import fr.fistin.hydra.api.redis.HydraPubSub;
import redis.clients.jedis.Jedis;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Project: Hydra
 * Created by AstFaster
 * on 20/11/2021 at 09:36
 */
public class HydraAPI {

    /** Some constants */
    public static final String HYDRA_NAME = "Hydra";
    public static final String NAME = HYDRA_NAME + "API";
    public static final Gson GSON = new GsonBuilder().create();

    /** {@link IHydraProvider} instance */
    private final IHydraProvider provider;

    /** Redis PubSub instance */
    private final HydraPubSub pubSub;

    /** Hydra connection. Used to send and receive packets */
    private final HydraConnection connection;

    /** Hydra event bus */
    private final HydraEventBus eventBus;

    /** A logger used to print info */
    private static Logger logger;

    /**
     * Constructor of {@link HydraAPI}
     *
     * @param provider {@link IHydraProvider} object to provide to make the API working
     */
    public HydraAPI(IHydraProvider provider) {
        logger = provider.getLogger();

        log("Starting " + NAME + "...");

        this.provider = provider;
        this.pubSub = new HydraPubSub(this);
        this.connection = new HydraConnection(this);
        this.eventBus = new HydraEventBus(this);
    }

    /**
     * To call when you want to stop {@link HydraAPI}
     */
    public void stop() {
        log("Stopping " + NAME + "...");

        this.pubSub.stop();
    }

    /**
     * Print a message in the terminal
     *
     * @param level Message's level
     * @param message Message to print
     */
    public static void log(Level level, String message) {
        logger.log(level, "[" + NAME + "] " + message);
    }

    /**
     * Print a message in the terminal
     *
     * @param message Message to print
     */
    public static void log(String message) {
        log(Level.INFO, message);
    }

    /**
     * Get {@link IHydraProvider} instance
     *
     * @return Provider
     */
    public IHydraProvider getProvider() {
        return this.provider;
    }

    /**
     * Get Redis PubSub instance
     *
     * @return {@link HydraPubSub}
     */
    public HydraPubSub getPubSub() {
        return this.pubSub;
    }

    /**
     * Get Hydra connection instance
     *
     * @return {@link HydraConnection}
     */
    public HydraConnection getConnection() {
        return this.connection;
    }

    /**
     * Get Hydra event bus
     *
     * @return {@link HydraEventBus}
     */
    public HydraEventBus getEventBus() {
        return this.eventBus;
    }

    /**
     * Get a resource from Redis
     *
     * @return {@link Jedis} object
     */
    public Jedis getRedisResource() {
        return this.provider.getJedisPool().getResource();
    }

}
