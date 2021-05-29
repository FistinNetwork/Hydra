package fr.fistin.hydra.proxy;

import fr.fistin.hydra.Hydra;

import java.util.UUID;

public class HydraProxy {

    private UUID uuid;

    private String proxyIp;

    private final int serverPort = 25565;

    private long startedTime;

    private ProxyOptions options;
    private ProxyState currentState;

    private String container;

    private final Hydra hydra;

    public HydraProxy(Hydra hydra, ProxyOptions options) {
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
        return this.uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getProxyIp() {
        return this.proxyIp;
    }

    public void setProxyIp(String proxyIp) {
        this.proxyIp = proxyIp;
    }

    public int getServerPort() {
        return this.serverPort;
    }

    public long getStartedTime() {
        return this.startedTime;
    }

    public void setStartedTime(long startedTime) {
        this.startedTime = startedTime;
    }

    public ProxyOptions getOptions() {
        return this.options;
    }

    public void setOptions(ProxyOptions options) {
        this.options = options;
    }

    public ProxyState getCurrentState() {
        return this.currentState;
    }

    public void setCurrentState(ProxyState currentState) {
        this.currentState = currentState;
    }

    public String getContainer() {
        return this.container;
    }

    public void setContainer(String container) {
        this.container = container;
    }

    @Override
    public String toString() {
        return "proxy-" + this.uuid.toString().split("-")[0];
    }
}
