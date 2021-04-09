package fr.fistinnetwork.hydra;

import fr.fistinnetwork.hydra.configuration.HydraConfiguration;
import fr.fistinnetwork.hydra.configuration.HydraConfigurationManager;
import fr.fistinnetwork.hydra.docker.DockerAPI;
import fr.fistinnetwork.hydra.docker.DockerConnector;
import fr.fistinnetwork.hydra.proxy.HydraProxyManager;
import fr.fistinnetwork.hydra.proxy.Proxy;
import fr.fistinnetwork.hydra.proxy.ProxyOptions;
import fr.fistinnetwork.hydra.scheduler.HydraScheduler;
import fr.fistinnetwork.hydra.server.HydraServerManager;
import fr.fistinnetwork.hydra.util.References;
import fr.fistinnetwork.hydra.util.logger.HydraLogger;
import fr.fistinnetwork.hydra.util.logger.LogType;

import java.io.File;
import java.util.concurrent.*;

public class Hydra {

    private ExecutorService executorService;

    private final HydraLogger logger;
    private final HydraConfigurationManager configurationManager;
    private final HydraScheduler scheduler;
    private final HydraProxyManager proxyManager;
    private final HydraServerManager serverManager;
    private final DockerAPI dockerAPI;
    private final DockerConnector docker;

    public Hydra() {
        this.logger = new HydraLogger(this, String.format("[%s]", References.HYDRA), new File("latest.log"));
        this.configurationManager = new HydraConfigurationManager(this, new File("hydra-config.yml"));
        this.scheduler = new HydraScheduler(this);
        this.proxyManager = new HydraProxyManager(this);
        this.serverManager = new HydraServerManager(this);
        this.dockerAPI = new DockerAPI(this);
        this.docker = new DockerConnector(this);
    }

    public void start() {
        this.configurationManager.loadConfiguration();

        this.proxyManager.downloadMinecraftProxyImage();
        this.serverManager.downloadMinecraftServerImage();

        // Start a default proxy
        this.proxyManager.startProxy(new Proxy(this, new ProxyOptions()));

        this.logger.printHeaderMessage();
    }

    public void shutdown() {
        this.logger.printFooterMessage();

        this.logger.log(LogType.INFO, "Shutting down executor service...");
        this.executorService.shutdown();

        this.dockerAPI.unload();

        this.serverManager.stopAllServers();
        this.proxyManager.stopAllProxies();

        this.logger.log(LogType.INFO, "Hydra is now down. See you soon !");
        System.exit(0);
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

    public ExecutorService getExecutorService() {
        return this.executorService == null ? this.executorService = Executors.newCachedThreadPool() : this.executorService;
    }
}