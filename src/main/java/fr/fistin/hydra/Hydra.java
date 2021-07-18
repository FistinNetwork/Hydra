package fr.fistin.hydra;

import fr.fistin.hydra.command.HydraCommandManager;
import fr.fistin.hydra.command.model.HelpCommand;
import fr.fistin.hydra.command.model.ServerCommand;
import fr.fistin.hydra.command.model.StopCommand;
import fr.fistin.hydra.configuration.HydraConfiguration;
import fr.fistin.hydra.configuration.HydraConfigurationManager;
import fr.fistin.hydra.docker.DockerConnector;
import fr.fistin.hydra.docker.container.DockerContainerManager;
import fr.fistin.hydra.docker.image.DockerImageManager;
import fr.fistin.hydra.proxy.HydraProxyManager;
import fr.fistin.hydra.receiver.HydraProxyReceiver;
import fr.fistin.hydra.receiver.HydraQueryReceiver;
import fr.fistin.hydra.receiver.HydraServerReceiver;
import fr.fistin.hydra.scheduler.HydraScheduler;
import fr.fistin.hydra.server.HydraServerManager;
import fr.fistin.hydra.server.template.HydraTemplateManager;
import fr.fistin.hydra.util.References;
import fr.fistin.hydra.util.logger.HydraLogger;
import fr.fistin.hydra.util.logger.LoggingOutputStream;
import fr.fistin.hydraconnector.HydraConnector;
import fr.fistin.hydraconnector.protocol.channel.HydraChannel;
import fr.fistin.hydraconnector.protocol.packet.HydraPacket;
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

    private ConsoleReader consoleReader;
    private HydraConnector hydraConnector;
    private HydraLogger logger;

    /**
     * Hydra
     */
    private final HydraConfigurationManager configurationManager;
    private final HydraScheduler scheduler;
    private final HydraProxyManager proxyManager;
    private final HydraServerManager serverManager;
    private final HydraTemplateManager templateManager;
    private final HydraCommandManager commandManager;

    /**
     * Docker
     */
    private final DockerConnector docker;
    private final DockerContainerManager containerManager;
    private final DockerImageManager imageManager;

    public Hydra() {
        this.configurationManager = new HydraConfigurationManager(this, new File("hydra-config.yml"));
        this.scheduler = new HydraScheduler(this);
        this.proxyManager = new HydraProxyManager(this);
        this.serverManager = new HydraServerManager(this);
        this.templateManager = new HydraTemplateManager(this);
        this.commandManager = new HydraCommandManager(this);
        this.docker = new DockerConnector();
        this.containerManager = new DockerContainerManager(this);
        this.imageManager = new DockerImageManager(this);
    }

    public void start() {
        this.setupConsoleReader();
        this.setupLogger();

        this.configurationManager.loadConfiguration();

        this.hydraConnector = new HydraConnector(References.GSON, this.getConfiguration().getRedisIp(), this.getConfiguration().getRedisPort(), this.getConfiguration().getRedisPassword());

        this.templateManager.createTemplatesFolder();

        if (!this.hydraConnector.connectToRedis()) System.exit(0);

        this.proxyManager.load();

        // If redis connection failed
        if (!this.stopping) {
            this.logger.printHeaderMessage();

            this.proxyManager.pullMinecraftProxyImage();
            this.serverManager.pullMinecraftServerImage();

            this.templateManager.loadAllTemplatesFromTemplatesFolder();

            this.hydraConnector.startPacketHandler();

            this.registerCommands();
            this.registerReceivers();

            this.running = true;

            this.scheduler.runTaskAsynchronously(this.commandManager::start);
        }
    }

    public void shutdown() {
        this.stopping = true;

        this.logger.printFooterMessage();

        this.serverManager.stopAllServers();
        this.proxyManager.stopAllProxies();

        this.scheduler.schedule(() -> {

            this.serverManager.checkIfAllServersHaveShutdown();
            this.proxyManager.checkIfAllProxiesHaveShutdown();

        }, 1, 0, TimeUnit.MINUTES).andThen(() -> {

            this.hydraConnector.getRedisHandler().stop();
            this.hydraConnector.getRedisConnection().disconnect();

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
    }

    private void registerReceivers() {
        System.out.println("Registering all hydra receivers...");

        this.hydraConnector.getRedisHandler().registerPacketReceiver(HydraChannel.QUERY, new HydraQueryReceiver(this));
        this.hydraConnector.getRedisHandler().registerPacketReceiver(HydraChannel.SERVERS, new HydraServerReceiver(this));
        this.hydraConnector.getRedisHandler().registerPacketReceiver(HydraChannel.PROXIES, new HydraProxyReceiver(this));
    }

    public void sendPacket(HydraChannel channel, HydraPacket packet) {
        this.hydraConnector.getConnectionManager().sendPacket(channel, packet);
    }

    public void sendPacket(String channel, HydraPacket packet) {
        this.hydraConnector.getConnectionManager().sendPacket(channel, packet);
    }

    public ConsoleReader getConsoleReader() {
        return this.consoleReader;
    }

    public HydraConnector getHydraConnector() {
        return this.hydraConnector;
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

    public DockerConnector getDocker() {
        return this.docker;
    }

    public DockerContainerManager getContainerManager() {
        return this.containerManager;
    }

    public DockerImageManager getImageManager() {
        return this.imageManager;
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