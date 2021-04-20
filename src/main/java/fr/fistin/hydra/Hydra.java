package fr.fistin.hydra;

import fr.fistin.hydra.configuration.HydraConfiguration;
import fr.fistin.hydra.configuration.HydraConfigurationManager;
import fr.fistin.hydra.docker.DockerAPI;
import fr.fistin.hydra.docker.DockerConnector;
import fr.fistin.hydra.packet.HydraPacketManager;
import fr.fistin.hydra.packet.channel.HydraChannel;
import fr.fistin.hydra.packet.receiver.HydraQueryReceiver;
import fr.fistin.hydra.proxy.HydraProxyManager;
import fr.fistin.hydra.proxy.HydraProxy;
import fr.fistin.hydra.proxy.ProxyOptions;
import fr.fistin.hydra.redis.RedisChannelsHandler;
import fr.fistin.hydra.redis.RedisConnector;
import fr.fistin.hydra.scheduler.HydraScheduler;
import fr.fistin.hydra.server.HydraServerManager;
import fr.fistin.hydra.server.template.HydraTemplateManager;
import fr.fistin.hydra.util.References;
import fr.fistin.hydra.util.logger.HydraLogger;
import fr.fistin.hydra.util.logger.LogType;

import java.io.File;
import java.util.concurrent.*;

public class Hydra {

    private ExecutorService executorService;
    private boolean running = false;
    private boolean stopping = false;

    private final HydraLogger logger;
    private final HydraConfigurationManager configurationManager;
    private final HydraScheduler scheduler;
    private final HydraProxyManager proxyManager;
    private final HydraServerManager serverManager;
    private final RedisConnector redisConnector;
    private final RedisChannelsHandler redisChannelsHandler;
    private final DockerAPI dockerAPI;
    private final DockerConnector docker;
    private final HydraPacketManager packetManager;
    private final HydraTemplateManager templateManager;

    public Hydra() {
        this.logger = new HydraLogger(this, String.format("[%s]", References.HYDRA), new File("latest.log"));
        this.configurationManager = new HydraConfigurationManager(this, new File("hydra-config.yml"));
        this.scheduler = new HydraScheduler(this);
        this.proxyManager = new HydraProxyManager(this);
        this.serverManager = new HydraServerManager(this);
        this.redisConnector = new RedisConnector(this);
        this.redisChannelsHandler = new RedisChannelsHandler(this);
        this.dockerAPI = new DockerAPI(this);
        this.docker = new DockerConnector();
        this.packetManager = new HydraPacketManager();
        this.templateManager = new HydraTemplateManager(this);
    }

    public void start() {
        this.configurationManager.loadConfiguration();
        this.logger.createLogFile();
        this.proxyManager.downloadMinecraftProxyImage();
        this.serverManager.downloadMinecraftServerImage();
        this.redisConnector.connect();

        // If redis connection failed
        if (!this.stopping) {
            this.redisChannelsHandler.subscribe();
            this.redisConnector.startReconnectTask();
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

            this.redisConnector.disconnect();
            this.redisChannelsHandler.stop();

            this.logger.log(LogType.INFO, "Shutting down executor service...");
            this.executorService.shutdown();

            this.dockerAPI.unload();

            this.logger.log(LogType.INFO, "Hydra is now down. See you soon !");
            System.exit(0);

        });
    }

    private void registerReceivers() {
        this.redisChannelsHandler.registerPacketReceiver(HydraChannel.QUERY.toString(), new HydraQueryReceiver(this));
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

    public RedisConnector getRedisConnector() {
        return this.redisConnector;
    }

    public RedisChannelsHandler getRedisChannelsHandler() {
        return this.redisChannelsHandler;
    }

    public DockerAPI getDockerAPI() {
        return this.dockerAPI;
    }

    public DockerConnector getDocker() {
        return this.docker;
    }

    public HydraPacketManager getPacketManager() {
        return this.packetManager;
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