package fr.fistin.hydra;

import fr.fistin.hydra.api.HydraAPI;
import fr.fistin.hydra.api.protocol.HydraChannel;
import fr.fistin.hydra.api.protocol.HydraConnection;
import fr.fistin.hydra.docker.Docker;
import fr.fistin.hydra.proxy.HydraProxyManager;
import fr.fistin.hydra.receiver.HydraQueryReceiver;
import fr.fistin.hydra.redis.HydraRedisConnection;
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
    private HydraProxyManager proxyManager;
    private HydraServerManager serverManager;

    /** State */
    private boolean running = false;

    /** Docker */
    private Docker docker;

    /** Redis */
    private HydraRedisConnection redisConnection;

    public void start() {
        HydraLogger.printHeaderMessage();

        this.setupConsoleReader();
        this.setupLogger();

        System.out.println("Starting " + References.NAME + "...");

        this.docker = new Docker();
        this.redisConnection = new HydraRedisConnection();

        if (!this.redisConnection.connect()) {
            System.exit(-1);
        }

        this.api = new HydraAPI(new HydraProvider(this));
        this.api.start();
        this.proxyManager = new HydraProxyManager(this);
        this.serverManager = new HydraServerManager(this);

        this.registerReceivers();

        this.proxyManager.startProxy();

        this.running = true;

        Runtime.getRuntime().addShutdownHook(new Thread(this::shutdown));
    }

    public void shutdown() {
        if (this.running) {
            System.out.println("Stopping " + References.NAME + "...");

            this.running = false;

            if (this.redisConnection != null && this.redisConnection.isConnected()) {
                this.api.stop("Stopping " + References.NAME + " application");
                this.redisConnection.disconnect();
            }

            System.out.println("Hydra is now down. See you soon!");
        }
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

    private void registerReceivers() {
        final HydraConnection connection = this.api.getConnection();

        connection.registerReceiver(HydraChannel.QUERY, new HydraQueryReceiver(this));
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

    public HydraProxyManager getProxyManager() {
        return this.proxyManager;
    }

    public HydraServerManager getServerManager() {
        return this.serverManager;
    }

    public HydraRedisConnection getRedisConnection() {
        return this.redisConnection;
    }

    public Docker getDocker() {
        return this.docker;
    }

    public boolean isRunning() {
        return this.running;
    }

}