package fr.fistin.hydra;

import fr.fistin.hydra.api.HydraAPI;
import fr.fistin.hydra.api.protocol.channel.HydraChannel;
import fr.fistin.hydra.api.protocol.packet.HydraPacket;
import fr.fistin.hydra.command.HydraCommandManager;
import fr.fistin.hydra.command.model.HelpCommand;
import fr.fistin.hydra.command.model.ProxyCommand;
import fr.fistin.hydra.command.model.ServerCommand;
import fr.fistin.hydra.command.model.StopCommand;
import fr.fistin.hydra.configuration.HydraConfiguration;
import fr.fistin.hydra.configuration.HydraConfigurationManager;
import fr.fistin.hydra.docker.Docker;
import fr.fistin.hydra.proxy.HydraProxyManager;
import fr.fistin.hydra.receiver.HydraProxyReceiver;
import fr.fistin.hydra.receiver.HydraQueryReceiver;
import fr.fistin.hydra.receiver.HydraServerReceiver;
import fr.fistin.hydra.redis.RedisConnection;
import fr.fistin.hydra.scheduler.HydraScheduler;
import fr.fistin.hydra.server.HydraServerManager;
import fr.fistin.hydra.server.template.HydraTemplateManager;
import fr.fistin.hydra.util.References;
import fr.fistin.hydra.util.logger.HydraLogger;
import fr.fistin.hydra.util.logger.LoggingOutputStream;
import jline.console.ConsoleReader;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

public class Hydra {

    private ExecutorService executorService;
    private boolean running = false;
    private boolean stopping = false;

    /**
     * Docker
     */
    private Docker docker;

    /**
     * Redis
     */
    private RedisConnection redisConnection;

    /**
     * Hydra
     */
    private HydraAPI api;
    private final HydraConfigurationManager configurationManager;
    private final HydraScheduler scheduler;
    private final HydraProxyManager proxyManager;
    private final HydraServerManager serverManager;
    private final HydraTemplateManager templateManager;
    private final HydraCommandManager commandManager;


    /**
     * Logger
     */
    private ConsoleReader consoleReader;
    private HydraLogger logger;

    public Hydra() {
        this.configurationManager = new HydraConfigurationManager(this, new File("hydra-config.yml"));
        this.scheduler = new HydraScheduler(this);
        this.proxyManager = new HydraProxyManager(this);
        this.serverManager = new HydraServerManager(this);
        this.templateManager = new HydraTemplateManager(this);
        this.commandManager = new HydraCommandManager(this);
    }

    public void start() {
        HydraLogger.printHeaderMessage();

        this.setupConsoleReader();
        this.setupLogger();

        this.configurationManager.loadConfiguration();
        this.redisConnection = new RedisConnection(this.getConfiguration());

        if (!this.redisConnection.connect()) System.exit(-1);

        this.api = new HydraAPI(References.GSON, this.redisConnection.getJedisPool());
        this.api.start();

        this.templateManager.createTemplatesFolder();
        this.docker = new Docker(this);
        this.templateManager.loadAllTemplatesFromTemplatesFolder();

        this.registerCommands();
        this.registerReceivers();

        this.running = true;

        this.scheduler.runTaskAsynchronously(this.commandManager::start);
    }

    public void shutdown() {
        this.stopping = true;

        this.serverManager.stopAllServers();
        this.proxyManager.stopAllProxies();

        this.logger.log(Level.INFO, "Waiting 20 seconds to check if all servers and proxies have shutdown !");

        this.scheduler.schedule(() -> {

            this.serverManager.checkIfAllServersHaveShutdown();
            this.proxyManager.checkIfAllProxiesHaveShutdown();

        }, 20, 0, TimeUnit.SECONDS).andThen(() -> {

            this.api.stop();
            this.redisConnection.disconnect();

            this.logger.log(Level.INFO, "Shutting down executor service...");
            this.executorService.shutdown();

            this.logger.log(Level.INFO, "Hydra is now down. See you soon !");

            this.running = false;

            System.exit(0);
        });
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
        new File("logs/").mkdirs();

        this.logger = new HydraLogger(this, References.HYDRA, "logs/hydra.log");

        System.setErr(new PrintStream(new LoggingOutputStream(this.logger, Level.SEVERE), true));
        System.setOut(new PrintStream(new LoggingOutputStream(this.logger, Level.INFO), true));
    }

    private void registerCommands() {
        System.out.println("Registering all hydra commands...");

        this.commandManager.addCommand(new HelpCommand(this, "help"));
        this.commandManager.addCommand(new StopCommand(this, "stop"));
        this.commandManager.addCommand(new ServerCommand(this, "server"));
        this.commandManager.addCommand(new ProxyCommand(this, "proxy"));
    }

    private void registerReceivers() {
        System.out.println("Registering all hydra receivers...");

        this.api.getRedisHandler().registerPacketReceiver(HydraChannel.QUERY, new HydraQueryReceiver(this));
        this.api.getRedisHandler().registerPacketReceiver(HydraChannel.SERVERS, new HydraServerReceiver(this));
        this.api.getRedisHandler().registerPacketReceiver(HydraChannel.PROXIES, new HydraProxyReceiver(this));
    }

    public void sendPacket(HydraChannel channel, HydraPacket packet) {
        this.api.getConnectionManager().sendPacket(channel, packet);
    }

    public void sendPacket(String channel, HydraPacket packet) {
        this.api.getConnectionManager().sendPacket(channel, packet);
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

    public HydraScheduler getScheduler() {
        return this.scheduler;
    }

    public HydraProxyManager getProxyManager() {
        return this.proxyManager;
    }

    public HydraServerManager getServerManager() {
        return this.serverManager;
    }

    public HydraTemplateManager getTemplateManager() {
        return this.templateManager;
    }

    public HydraCommandManager getCommandManager() {
        return this.commandManager;
    }

    public RedisConnection getRedisConnection() {
        return this.redisConnection;
    }

    public Docker getDocker() {
        return this.docker;
    }

    public ExecutorService getExecutorService() {
        return this.executorService == null ? this.executorService = Executors.newCachedThreadPool() : this.executorService;
    }

    public boolean isRunning() {
        return this.running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public boolean isStopping() {
        return this.stopping;
    }

    public void setStopping(boolean stopping) {
        this.stopping = stopping;
    }
}