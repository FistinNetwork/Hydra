package fr.fistin.hydra;

import fr.fistin.hydra.configuration.HydraConfiguration;
import fr.fistin.hydra.configuration.HydraConfigurationManager;
import fr.fistin.hydra.docker.DockerAPI;
import fr.fistin.hydra.docker.DockerConnector;
import fr.fistin.hydra.query.HydraQueryReceiver;
import fr.fistin.hydraconnector.HydraConnector;
import fr.fistin.hydraconnector.protocol.HydraProtocol;
import fr.fistin.hydraconnector.protocol.channel.HydraChannel;
import fr.fistin.hydraconnector.protocol.packet.HydraPacket;
import fr.fistin.hydraconnector.protocol.packet.event.ServerStartedPacket;
import fr.fistin.hydraconnector.protocol.packet.event.ServerStoppedPacket;
import fr.fistin.hydraconnector.protocol.packet.server.StartServerPacket;
import fr.fistin.hydra.proxy.HydraProxyManager;
import fr.fistin.hydraconnector.redis.RedisHandler;
import fr.fistin.hydraconnector.redis.RedisConnection;
import fr.fistin.hydra.scheduler.HydraScheduler;
import fr.fistin.hydra.server.HydraServerManager;
import fr.fistin.hydra.server.template.HydraTemplateManager;
import fr.fistin.hydra.util.References;
import fr.fistin.hydra.util.logger.HydraLogger;
import fr.fistin.hydra.util.logger.LogType;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Hydra {

    private ExecutorService executorService;
    private boolean running = false;
    private boolean stopping = false;

    private HydraConnector hydraConnector;

    private final HydraLogger logger;
    private final HydraConfigurationManager configurationManager;
    private final HydraScheduler scheduler;
    private final HydraProxyManager proxyManager;
    private final HydraServerManager serverManager;
    private final DockerAPI dockerAPI;
    private final DockerConnector docker;
    private final HydraTemplateManager templateManager;

    public Hydra() {
        this.logger = new HydraLogger(this, String.format("[%s]", References.HYDRA), new File("latest.log"));
        this.configurationManager = new HydraConfigurationManager(this, new File("hydra-config.yml"));
        this.scheduler = new HydraScheduler(this);
        this.proxyManager = new HydraProxyManager(this);
        this.serverManager = new HydraServerManager(this);
        this.dockerAPI = new DockerAPI(this);
        this.docker = new DockerConnector();
        this.templateManager = new HydraTemplateManager(this);
    }

    public void start() {
        this.configurationManager.loadConfiguration();

        this.hydraConnector = new HydraConnector(References.GSON, this.getConfiguration().getRedisIp(), this.getConfiguration().getRedisPort(), this.getConfiguration().getRedisPassword());

        this.logger.createLogFile();
        this.templateManager.createTemplatesFolder();
        this.templateManager.loadAllTemplatesFromTemplatesFolder();
        this.proxyManager.downloadMinecraftProxyImage();
        this.serverManager.downloadMinecraftServerImage();
        if (!this.hydraConnector.connectToRedis()) this.stopping = true;

        // If redis connection failed
        if (!this.stopping) {
            this.hydraConnector.startPacketHandler();
            this.registerReceivers();

            this.logger.printHeaderMessage();

            this.running = true;
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

            this.logger.log(LogType.INFO, "Shutting down executor service...");
            this.executorService.shutdown();

            this.dockerAPI.unload();

            this.logger.log(LogType.INFO, "Hydra is now down. See you soon !");
            System.exit(0);

        });
    }

    private void registerReceivers() {
        this.hydraConnector.getRedisHandler().registerPacketReceiver(HydraChannel.QUERY, new HydraQueryReceiver(this));
    }

    public void sendPacket(HydraChannel channel, HydraPacket packet) {
        this.hydraConnector.getConnectionManager().sendPacket(channel, packet);
    }

    public void sendPacket(String channel, HydraPacket packet) {
        this.hydraConnector.getConnectionManager().sendPacket(channel, packet);
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

    public DockerAPI getDockerAPI() {
        return this.dockerAPI;
    }

    public DockerConnector getDocker() {
        return this.docker;
    }

    public HydraTemplateManager getTemplateManager() {
        return this.templateManager;
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