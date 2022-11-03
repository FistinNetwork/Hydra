package fr.fistin.hydra.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import fr.fistin.hydra.api.event.HydraEventBus;
import fr.fistin.hydra.api.protocol.HydraConnection;
import fr.fistin.hydra.api.protocol.heartbeat.HydraHeartbeatTask;
import fr.fistin.hydra.api.proxy.HydraProxiesService;
import fr.fistin.hydra.api.redis.HydraPubSub;
import fr.fistin.hydra.api.redis.IHydraRedis;
import fr.fistin.hydra.api.server.HydraServersService;

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
    /** The Redis prefix hash of Hydra */
    public static final String HYDRA_HASH = "hydra:";

    /** A logger used to print info */
    private static Logger logger;
    /** The log header to add before all messages sent by {@link HydraAPI} */
    private static String logHeader;

    /** The type of the application that {@link HydraAPI} is running on */
    private final Type type;
    /** The name of the application that {@link HydraAPI} is running on */
    private final String application;
    /** The {@link IHydraRedis} instance */
    private final IHydraRedis redis;
    /** An executor service that can schedule tasks */
    private final ScheduledExecutorService executorService;
    /** Redis PubSub instance */
    private final HydraPubSub pubSub;
    /** Hydra connection. Used to send and receive packets */
    private final HydraConnection connection;
    /** Hydra event bus */
    private final HydraEventBus eventBus;
    /** The service to interact with servers system */
    private final HydraServersService serversService;
    /** THe service to interact with proxies system */
    private final HydraProxiesService proxiesService;

    /**
     * Constructor of {@link HydraAPI}
     *
     * @param type The application type
     * @param application The application name
     * @param logger The logger used to print info
     * @param logHeader The log header used by the logger
     * @param redis The {@link IHydraRedis} instance
     */
    private HydraAPI(Type type, String application, Logger logger, String logHeader, IHydraRedis redis) {
        this.type = type;
        this.application = application;
        HydraAPI.logger = logger;
        HydraAPI.logHeader = logHeader;
        this.redis = redis;
        this.executorService = Executors.newScheduledThreadPool(32);
        this.pubSub = new HydraPubSub(this);
        this.connection = new HydraConnection(this);
        this.eventBus = new HydraEventBus(this);
        this.serversService = new HydraServersService(this);
        this.proxiesService = new HydraProxiesService(this);
    }

    /**
     * Start {@link HydraAPI}
     */
    public void start() {
        log("Starting " + NAME + "...");

        this.pubSub.start();
        this.eventBus.start();

        new HydraHeartbeatTask(this).start();
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
     * Get the running application type
     *
     * @return A {@link Type}
     */
    public Type getType() {
        return this.type;
    }

    /**
     * Get the running application name
     *
     * @return An application name
     */
    public String getApplication() {
        return this.application;
    }

    /**
     * Get the {@link IHydraRedis} instance
     *
     * @return The {@link IHydraRedis} instance
     */
    public IHydraRedis getRedis() {
        return this.redis;
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
     * Get the service used to interact with servers
     *
     * @return The {@link HydraServersService} instance
     */
    public HydraServersService getServersService() {
        return this.serversService;
    }

    /**
     * Get the service used to interact with proxies
     *
     * @return The {@link HydraProxiesService} instance
     */
    public HydraProxiesService getProxiesService() {
        return this.proxiesService;
    }

    /**
     * The builder class of {@link HydraAPI}
     */
    public static class Builder {

        /** The type of the application running {@link HydraAPI} */
        private Type type;
        /** The application name */
        private String application;
        /** The logger */
        private Logger logger = Logger.getLogger("HydraAPI");
        /** The log header to add before all messages */
        private String logHeader = null;
        /** The {@link IHydraRedis} instance */
        private IHydraRedis redis;

        /**
         * Constructor of {@link Builder}
         *
         * @param type The required application type
         * @param application The required application name
         */
        public Builder(Type type, String application) {
            this.type = type;
            this.application = application;
        }

        /**
         * Set the application type
         *
         * @param type New {@link Type}
         * @return This {@link Builder} instance
         */
        public Builder withType(Type type) {
            this.type = type;
            return this;
        }

        /**
         * Set the application name
         *
         * @param application New application name
         * @return This {@link Builder} instance
         */
        public Builder withApplication(String application) {
            this.application = application;
            return this;
        }

        /**
         * Set the logger
         *
         * @param logger New logger
         * @return This {@link Builder} instance
         */
        public Builder withLogger(Logger logger) {
            this.logger = logger;
            return this;
        }

        /**
         * Set the log header
         *
         * @param logHeader New log header
         * @return This {@link Builder} instance
         */
        public Builder withLogHeader(String logHeader) {
            this.logHeader = logHeader;
            return this;
        }

        /**
         * Set the {@link IHydraRedis} instance
         *
         * @param redis New {@link IHydraRedis}
         * @return This {@link Builder} instance
         */
        public Builder withRedis(IHydraRedis redis) {
            this.redis = redis;
            return this;
        }

        /**
         * Build the builder to an instance of {@link HydraAPI}<br>
         * Warning: some builder variables cannot be null!
         *
         * @return The created {@link HydraAPI} instance
         */
        public HydraAPI build() {
            if (this.type != null && this.logger != null && this.redis != null) {
                return new HydraAPI(this.type, this.application, this.logger, this.logHeader, this.redis);
            }
            throw new HydraException("Cannot build HydraAPI with a null value!");
        }

    }

    /**
     * An enumeration used to know what type of application is running {@link HydraAPI}
     */
    public enum Type {

        /** The application is a simple client that received orders */
        CLIENT,
        /** The application is a server */
        SERVER,
        /** The application is a proxy */
        PROXY,
        /** The application is Hydra */
        HYDRA

    }

}
