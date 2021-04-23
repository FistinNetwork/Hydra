package fr.fistin.hydra.server;

import fr.fistin.hydra.Hydra;
import fr.fistin.hydra.scheduler.HydraTask;

import java.util.UUID;

public class HydraServer {

    protected UUID uuid;
    protected String type;

    protected String serverIp = null;

    protected final int serverPort = 25565;

    protected String mapUrl;
    protected String pluginUrl;

    protected int slots;
    protected int currentPlayers = 0;

    protected ServerOptions options;
    protected ServerState currentState;

    protected long startedTime;

    protected String container;

    private HydraTask healthyTask;
    private int checkAlive;

    protected final Hydra hydra;

    public HydraServer(Hydra hydra, String type, String mapUrl, String pluginUrl, int slots, int checkAlive, ServerOptions options) {
        this.hydra = hydra;
        this.uuid = UUID.randomUUID();
        this.type = type;
        this.mapUrl = mapUrl;
        this.pluginUrl = pluginUrl;
        this.slots = slots;
        this.checkAlive = checkAlive;
        this.options = options;
        this.currentState = ServerState.CREATING;

        this.hydra.getServerManager().addServer(this);
    }

    public void start() {
        this.hydra.getServerManager().startServer(this);
    }

    public boolean stop() {
        return this.hydra.getServerManager().stopServer(this);
    }

    public void checkStatus() {
        this.hydra.getServerManager().checkStatus(this);
    }

    public UUID getUuid() {
        return this.uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getServerIp() {
        return this.serverIp;
    }

    public void setServerIp(String serverIp) {
        this.serverIp = serverIp;
    }

    public int getServerPort() {
        return this.serverPort;
    }

    public String getMapUrl() {
        return this.mapUrl;
    }

    public void setMapUrl(String mapUrl) {
        this.mapUrl = mapUrl;
    }

    public String getPluginUrl() {
        return this.pluginUrl;
    }

    public void setPluginUrl(String pluginUrl) {
        this.pluginUrl = pluginUrl;
    }

    public int getSlots() {
        return this.slots;
    }

    public void setSlots(int slots) {
        this.slots = slots;
    }

    public int getCurrentPlayers() {
        return this.currentPlayers;
    }

    public void setCurrentPlayers(int currentPlayers) {
        this.currentPlayers = currentPlayers;
    }

    public ServerOptions getOptions() {
        return this.options;
    }

    public void setOptions(ServerOptions options) {
        this.options = options;
    }

    public ServerState getCurrentState() {
        return this.currentState;
    }

    public void setCurrentState(ServerState currentState) {
        this.currentState = currentState;
    }

    public long getStartedTime() {
        return this.startedTime;
    }

    public void setStartedTime(long startedTime) {
        this.startedTime = startedTime;
    }

    public String getContainer() {
        return this.container;
    }

    public void setContainer(String container) {
        this.container = container;
    }

    public HydraTask getHealthyTask() {
        return this.healthyTask;
    }

    public void setHealthyTask(HydraTask healthyTask) {
        this.healthyTask = healthyTask;
    }

    public int getCheckAlive() {
        return this.checkAlive;
    }

    public void setCheckAlive(int checkAlive) {
        this.checkAlive = checkAlive;
    }

    @Override
    public String toString() {
        return this.type + "-" + this.uuid.toString().split("-")[0];
    }
}
