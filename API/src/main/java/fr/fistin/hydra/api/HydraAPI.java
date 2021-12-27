package fr.fistin.hydra.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import fr.fistin.hydra.api.event.HydraEventBus;
import fr.fistin.hydra.api.jwt.HydraJWTManager;
import fr.fistin.hydra.api.protocol.HydraConnection;
import fr.fistin.hydra.api.protocol.packet.codec.HydraCodec;
import fr.fistin.hydra.api.protocol.packet.codec.IHydraCodec;
import fr.fistin.hydra.api.redis.HydraPubSub;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
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

    /** The type of the application that {@link HydraAPI} is running on */
    private final Type type;
    /** The {@link JedisPool} instance */
    private final JedisPool jedisPool;
    /** The codec to use for packets */
    private final IHydraCodec codec;
    /** The private key used to sign requests */
    private final PrivateKey privateKey;
    /** The public key used to verify requests */
    private final PublicKey publicKey;
    /** The Hydra JWT manager */
    private final HydraJWTManager jwtManager;
    /** An executor service that can schedule tasks */
    private final ScheduledExecutorService executorService;
    /** Redis PubSub instance */
    private final HydraPubSub pubSub;
    /** Hydra connection. Used to send and receive packets */
    private final HydraConnection connection;
    /** Hydra event bus */
    private final HydraEventBus eventBus;
    /** A logger used to print info */
    private static Logger logger;
    /** The log header to add before all messages sent by {@link HydraAPI} */
    private static String logHeader;

    /**
     * Constructor of {@link HydraAPI}
     * @param type The application type
     * @param logger The logger used to print info
     * @param logHeader The log header used by the logger
     * @param jedisPool The {@link JedisPool} instance
     * @param codec The codec used for packets
     * @param privateKey The private key
     * @param publicKey The public key
     */
    private HydraAPI(Type type, Logger logger, String logHeader, JedisPool jedisPool, IHydraCodec codec, PrivateKey privateKey, PublicKey publicKey) {
        this.type = type;
        HydraAPI.logger = logger;
        HydraAPI.logHeader = logHeader;
        this.jedisPool = jedisPool;
        this.codec = codec;
        this.privateKey = privateKey;
        this.publicKey = publicKey;
        this.jwtManager = new HydraJWTManager(this);
        this.executorService = Executors.newScheduledThreadPool(32);
        this.pubSub = new HydraPubSub(this);
        this.connection = new HydraConnection(this);
        this.eventBus = new HydraEventBus(this);
    }

    /**
     * Start {@link HydraAPI}
     */
    public void start() {
        log("Starting " + NAME + "...");

        this.pubSub.start();
        this.eventBus.start();
    }

    /**
     * To call when you want to stop {@link HydraAPI}
     */
    public void stop(String reason) {
        log("Stopping " + NAME + " (reason: " + reason + ")...");

        this.pubSub.stop();
        this.executorService.shutdown();
    }

    /**
     * Print a message in the terminal
     *
     * @param level Message's level
     * @param message Message to print
     */
    public static void log(Level level, String message) {
        logger.log(level, (logHeader == null ? "" : "[" + logHeader + "] ") + message);
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
     * Get running application type
     *
     * @return A {@link Type}
     */
    public Type getType() {
        return this.type;
    }

    /**
     * Get a resource from Redis
     *
     * @return {@link Jedis} object
     */
    public Jedis getRedisResource() {
        return this.jedisPool.getResource();
    }

    /**
     * Get the codec used for packets
     *
     * @return {@link IHydraCodec} instance
     */
    public IHydraCodec getCodec() {
        return this.codec;
    }

    /**
     * Get the private key used to sign requests
     *
     * @return The private key
     */
    public PrivateKey getPrivateKey() {
        return this.privateKey;
    }

    /**
     * Get the public key used to verify requests
     *
     * @return The public key
     */
    public PublicKey getPublicKey() {
        return this.publicKey;
    }

    /**
     * Get the JWTs manager instance
     *
     * @return {@link HydraJWTManager} instance
     */
    public HydraJWTManager getJWTManager() {
        return this.jwtManager;
    }

    /**
     * Get the executor service that can schedule tasks
     *
     * @return {@link ScheduledExecutorService} instance
     */
    public ScheduledExecutorService getExecutorService() {
        return this.executorService;
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
     * The builder class of {@link HydraAPI}
     */
    public static class Builder {

        /** The type of the application running {@link HydraAPI} */
        private Type type;
        /** The logger */
        private Logger logger = Logger.getLogger("HydraAPI");
        /** The log header to add before all messages */
        private String logHeader = null;
        /** The {@link JedisPool} instance */
        private JedisPool jedisPool;
        /** The codec used to encode and decode packets */
        private IHydraCodec codec = new HydraCodec();
        /** The private key used to authenticate sends (only if the application is {@link Type#SERVER} */
        private PrivateKey privateKey;
        /** The public key used to verify the authenticity of messages received (only if the application is {@link Type#CLIENT} */
        private PublicKey publicKey;

        /**
         * Constructor of {@link Builder}
         *
         * @param type The required application type
         */
        public Builder(Type type) {
            this.type = type;
        }

        /**
         * Set the application type
         *
         * @param type New {@link Type}
         * @return {@link Builder}
         */
        public Builder withType(Type type) {
            this.type = type;
            return this;
        }

        /**
         * Set the logger
         *
         * @param logger New logger
         * @return {@link Builder}
         */
        public Builder withLogger(Logger logger) {
            this.logger = logger;
            return this;
        }

        /**
         * Set the log header
         *
         * @param logHeader New log header
         * @return {@link Builder}
         */
        public Builder withLogHeader(String logHeader) {
            this.logHeader = logHeader;
            return this;
        }

        /**
         * Set the {@link JedisPool} instance
         *
         * @param jedisPool New {@link JedisPool}
         * @return {@link Builder}
         */
        public Builder withJedisPool(JedisPool jedisPool) {
            this.jedisPool = jedisPool;
            return this;
        }

        /**
         * Set the codec
         *
         * @param codec New codec
         * @return {@link Builder}
         */
        public Builder withCodec(IHydraCodec codec) {
            this.codec = codec;
            return this;
        }

        /**
         * Set the private key used to authenticate requests
         *
         * @param privateKey New private key
         * @return {@link Builder}
         */
        public Builder withPrivateKey(PrivateKey privateKey) {
            this.privateKey = privateKey;
            return this;
        }

        /**
         * Set the public key used to verify authenticity of received messages
         *
         * @param publicKey New public key
         * @return {@link Builder}
         */
        public Builder withPublicKey(PublicKey publicKey) {
            this.publicKey = publicKey;
            return this;
        }

        /**
         * Build the builder to an instance of {@link HydraAPI}<br>
         * Warning: some builder variables cannot be null!
         *
         * @return {@link HydraAPI} instance
         */
        public HydraAPI build() {
            if (this.type != null && this.logger != null && this.jedisPool != null && this.codec != null) {
                if (this.type == Type.CLIENT && this.publicKey == null) {
                    throw new RuntimeException("If you are running on a client, public key cannot be null!");
                } else if (this.type == Type.SERVER && this.privateKey == null) {
                    throw new RuntimeException("If you are running on a server, private key cannot be null!");
                }
                return new HydraAPI(type, this.logger, this.logHeader, this.jedisPool, this.codec, this.privateKey, this.publicKey);
            }
            throw new RuntimeException("Cannot build HydraAPI with a null value!");
        }

    }

    /**
     * An enumeration used to know what type of application is running {@link HydraAPI}
     */
    public enum Type {
        /** The application is a simple client that received orders */
        CLIENT,
        /** The application is a server that manage all clients */
        SERVER
    }

}
