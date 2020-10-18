package fr.fistinnetwork.hydra.servermanager;

import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.model.HostConfig;
import fr.fistinnetwork.hydra.Hydra;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public abstract class Server {
    private static final Map<String, Server> SERVER_LIST = new HashMap<>();

    protected UUID uuid;
    protected String type;

    protected String serverIP = null;

    protected Integer serverPort = 25565;

    protected String mapUrl;
    protected String pluginUrl;

    protected int slots;
    protected int currentPlayers = 0;

    protected ServerOptions options;

    protected ServerState currentState = ServerState.CREATING;

    protected long startedTime;

    protected String container;

    public Server(String type, String mapUrl, String pluginUrl, int slots, ServerOptions options) {
        this.uuid = UUID.randomUUID();
        this.type = type;
        this.mapUrl = mapUrl;
        this.pluginUrl = pluginUrl;
        this.options = options;
        this.slots = slots;
        addServer(this);
    }

    public String getPluginUrl() {
        return pluginUrl;
    }

    public void setPluginUrl(String pluginUrl) {
        this.pluginUrl = pluginUrl;
    }

    public String getServerIP() {
        return serverIP;
    }

    public int getServerPort() {
        return serverPort;
    }

    public String getMapUrl() {
        return mapUrl;
    }

    public void setMapUrl(String mapUrl) {
        this.mapUrl = mapUrl;
    }

    public int getSlots() {
        return slots;
    }

    public void setSlots(int slots) {
        this.slots = slots;
    }

    public Integer getCurrentPlayers() {
        return this.currentPlayers;
    }

    public void setCurrentPlayers(int currentPlayers) {
        this.currentPlayers = currentPlayers;
    }

    public ServerState getCurrentState() {
        return currentState;
    }

    public void setCurrentState(ServerState currentState) {
        this.currentState = currentState;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public void setStartedTime(long startedTime) {
        this.startedTime = startedTime;
    }

    public String getType() {
        return type;
    }

    public void setType(String name) {
        this.type = name;
    }

    public String getContainer() {
        return container;
    }

    public void setContainer(String containerId) {
        this.container = containerId;
    }

    public static Map<String, Server> getServerList() {
        return SERVER_LIST;
    }

    public static void addServer(Server server) {
        SERVER_LIST.put(server.toString(), server);
    }
    public static void removeServer(Server server) {
        SERVER_LIST.remove(server.toString());
    }

    public static Server getServerByName(String name) {
        return SERVER_LIST.getOrDefault(name, null);
    }

    public void start()
    {
        this.container = Hydra.getInstance().getDocker().getDockerClient().createContainerCmd("itzg/minecraft-server")
                .withEnv(this.options.getEnv())
                .withHostConfig(new HostConfig().withAutoRemove(true))
                .withName(this.toString())
                .exec()
                .getId();
        Hydra.getInstance().getDocker().getDockerClient().startContainerCmd(this.container).exec();
        this.serverIP = Hydra.getInstance().getDocker().getDockerClient().inspectContainerCmd(this.container).exec().getNetworkSettings().getIpAddress();
        ServerInfo serverInfo = ProxyServer.getInstance().constructServerInfo(type, new InetSocketAddress(this.serverIP, 25565), "", false);
        ProxyServer.getInstance().getServersCopy().put(this.toString(), serverInfo);
    }

    public boolean stop() {
        if(!this.container.isEmpty()){
            try {
                Hydra.getInstance().getDocker().getDockerClient().stopContainerCmd(this.getContainer()).exec();
                Server.removeServer(this);
                this.setCurrentState(ServerState.CREATING);
                return true;
            } catch(Exception e) {
                Hydra.getInstance().getLogger().warning("Le serveur " + this.toString() + "ne s'est pas stop");
                return false;
            }
        }
        return false;
    }

    protected void checkStatus()
    {
        if(this.container.isEmpty())
            return;
        InspectContainerResponse.ContainerState containerState = Hydra.getInstance().getDocker().getDockerClient().inspectContainerCmd(this.container).exec().getState();
        switch(containerState.getHealth().getStatus()){
            case "starting":
                this.currentState = ServerState.STARTING;
                break;

            case "healthy":
                this.currentState = ServerState.READY;
                break;

            default:
                this.currentState = ServerState.IDLE;
                break;
        }
    }

    @Override
    public String toString() {
        return this.type + "-" + this.uuid.toString().split("-")[0];
    }
}
