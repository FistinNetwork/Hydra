package fr.fistin.hydra;

import fr.fistin.hydra.api.HydraAPI;
import fr.fistin.hydra.api.protocol.HydraChannel;
import fr.fistin.hydra.api.protocol.HydraConnection;
import fr.fistin.hydra.api.proxy.HydraProxyCreationInfo;
import fr.fistin.hydra.config.HydraConfig;
import fr.fistin.hydra.heartbeat.HydraHeartbeatsChecker;
import fr.fistin.hydra.kubernetes.Kubernetes;
import fr.fistin.hydra.proxy.HydraProxyManager;
import fr.fistin.hydra.receiver.HydraProxiesReceiver;
import fr.fistin.hydra.receiver.HydraQueryReceiver;
import fr.fistin.hydra.receiver.HydraServersReceiver;
import fr.fistin.hydra.redis.HydraRedis;
import fr.fistin.hydra.server.HydraServerManager;
import fr.fistin.hydra.util.References;
import fr.fistin.hydra.util.logger.HydraLogger;

import java.io.IOException;
import java.nio.file.Files;

public class Hydra {

    private static Hydra instance;

    /** Logger */
    private HydraLogger logger;

    /** K8s system */
    private Kubernetes kubernetes;

    /** Redis */
    private HydraRedis redis;

    /** Hydra */
    private HydraConfig config;
    private HydraAPI api;
    private HydraProxyManager proxyManager;
    private HydraServerManager serverManager;

    /** State */
    private boolean running = false;

    public void start() {
        this.running = true;
        instance = this;

        HydraLogger.printHeaderMessage();

        this.logger = new HydraLogger();

        System.out.println("Starting " + References.NAME + "...");

        this.config = HydraConfig.load();
        this.kubernetes = new Kubernetes();
        this.redis = new HydraRedis();

        if (!this.redis.connect()) {
            System.exit(-1);
        }

        this.api = new HydraAPI.Builder(HydraAPI.Type.SERVER, References.NAME)
                .withLogger(this.logger)
                .withLogHeader("API")
                .withRedis(this.redis)
                .build();
        this.api.start();
        this.proxyManager = new HydraProxyManager(this);
        this.serverManager = new HydraServerManager(this);

        this.registerReceivers();

        new HydraHeartbeatsChecker().start();

        this.proxyManager.startProxy(new HydraProxyCreationInfo());

        Runtime.getRuntime().addShutdownHook(new Thread(this::shutdown));
    }

    public void shutdown() {
        if (!this.running) {
            return;
        }

        System.out.println("Stopping " + References.NAME + "...");

        this.running = false;

        if (this.redis != null && this.redis.isConnected()) {
            this.api.stop("Stopping " + References.NAME + " application");

            this.redis.disconnect();
        }

        System.out.println(References.NAME + " is now down. See you soon!");
    }

    private void registerReceivers() {
        final HydraConnection connection = this.api.getConnection();

        connection.registerReceiver(HydraChannel.QUERY, new HydraQueryReceiver());
        connection.registerReceiver(HydraChannel.SERVERS, new HydraServersReceiver());
        connection.registerReceiver(HydraChannel.PROXIES, new HydraProxiesReceiver());
    }

    public static Hydra get() {
        return instance;
    }

    public HydraLogger getLogger() {
        return this.logger;
    }

    public HydraConfig getConfig() {
        return this.config;
    }

    public Kubernetes getKubernetes() {
        return this.kubernetes;
    }

    public HydraRedis getRedis() {
        return this.redis;
    }

    public HydraAPI getAPI() {
        return this.api;
    }

    public HydraProxyManager getProxyManager() {
        return this.proxyManager;
    }

    public HydraServerManager getServerManager() {
        return this.serverManager;
    }

}