package fr.fistinnetwork.hydra.proxy;

import fr.fistinnetwork.hydra.Hydra;

import java.util.UUID;

public class Proxy {

    private UUID uuid;

    private String proxyIp;

    private final int serverPort = 25565;

    private long startedTime;

    private ProxyOptions options;
    private ProxyState currentState;

    private String container;

    private final Hydra hydra;

    public Proxy(Hydra hydra, ProxyOptions options) {
        this.hydra = hydra;
        this.uuid = UUID.randomUUID();
        this.options = options;
        this.currentState = ProxyState.CREATING;

        this.hydra.getProxyManager().addProxy(this);
    }

    public void start() {
        this.hydra.getProxyManager().startProxy(this);
    }

    public boolean stop() {
        return this.hydra.getProxyManager().stopProxy(this);
    }

    public void checkStatus() {
        this.hydra.getProxyManager().checkStatus(this);
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getProxyIp() {
        return proxyIp;
    }

    public void setProxyIp(String proxyIp) {
        this.proxyIp = proxyIp;
    }

    public int getServerPort() {
        return serverPort;
    }

    public long getStartedTime() {
        return startedTime;
    }

    public void setStartedTime(long startedTime) {
        this.startedTime = startedTime;
    }

    public ProxyOptions getOptions() {
        return options;
    }

    public void setOptions(ProxyOptions options) {
        this.options = options;
    }

    public ProxyState getCurrentState() {
        return currentState;
    }

    public void setCurrentState(ProxyState currentState) {
        this.currentState = currentState;
    }

    public String getContainer() {
        return container;
    }

    public void setContainer(String container) {
        this.container = container;
    }

    @Override
    public String toString() {
        return "proxy-" + this.uuid.toString().split("-")[0];
    }
}
