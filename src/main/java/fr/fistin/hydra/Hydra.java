package fr.fistin.hydra;

import fr.fistin.hydra.api.HydraAPI;
import fr.fistin.hydra.configuration.HydraConfiguration;
import fr.fistin.hydra.configuration.HydraConfigurationManager;
import fr.fistin.hydra.docker.Docker;
import fr.fistin.hydra.proxy.HydraProxyManager;
import fr.fistin.hydra.redis.RedisConnection;
import fr.fistin.hydra.server.HydraServerManager;
import fr.fistin.hydra.util.References;
import fr.fistin.hydra.util.logger.HydraLogger;
import fr.fistin.hydra.util.logger.HydraLoggingOutputStream;
import jline.console.ConsoleReader;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.util.logging.Level;

public class Hydra {

    /** Logger */
    private ConsoleReader consoleReader;
    private HydraLogger logger;

    /** Hydra */
    private HydraAPI api;
    private final HydraConfigurationManager configurationManager;
    private final HydraProxyManager proxyManager;
    private final HydraServerManager serverManager;

    /** State */
    private boolean running = false;

    /** Docker */
    private Docker docker;

    /** Redis */
    private RedisConnection redisConnection;

    public Hydra() {
        this.configurationManager = new HydraConfigurationManager(this);
        this.proxyManager = new HydraProxyManager(this);
        this.serverManager = new HydraServerManager(this);
    }

    public void start() {
        HydraLogger.printHeaderMessage();

        this.setupConsoleReader();
        this.setupLogger();

        this.configurationManager.loadConfiguration();
        this.redisConnection = new RedisConnection(this.getConfiguration());

        if (!this.redisConnection.connect()) {
            System.exit(-1);
        }

        this.api = new HydraAPI(new HydraProvider(this));
        this.docker = new Docker(this);

        this.running = true;
    }

    public void shutdown() {
        this.running = false;

        if (this.redisConnection != null && this.redisConnection.isConnected()) {
            this.api.stop();
            this.redisConnection.disconnect();
        }

        this.logger.log(Level.INFO, "Hydra is now down. See you soon !");

        System.exit(0);
    }

    private void setupConsoleReader() {
        try {
            this.consoleReader = new ConsoleReader();
            this.consoleReader.setExpandEvents(false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setupLogger() {
        try {
            if (!Files.exists(References.LOG_FOLDER)) {
                Files.createDirectory(References.LOG_FOLDER);
            }

            this.logger = new HydraLogger(this, References.NAME, References.LOG_FILE.toString());

            System.setErr(new PrintStream(new HydraLoggingOutputStream(this.logger, Level.SEVERE), true));
            System.setOut(new PrintStream(new HydraLoggingOutputStream(this.logger, Level.INFO), true));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ConsoleReader getConsoleReader() {
        return this.consoleReader;
    }

    public HydraAPI getAPI() {
        return this.api;
    }

    public HydraLogger getLogger() {
        return this.logger;
    }

    public HydraConfiguration getConfiguration() {
        return this.configurationManager.getConfiguration();
    }

    public HydraConfigurationManager getConfigurationManager() {
        return this.configurationManager;
    }

    public HydraProxyManager getProxyManager() {
        return this.proxyManager;
    }

    public HydraServerManager getServerManager() {
        return this.serverManager;
    }

    public RedisConnection getRedisConnection() {
        return this.redisConnection;
    }

    public Docker getDocker() {
        return this.docker;
    }

    public boolean isRunning() {
        return this.running;
    }

}