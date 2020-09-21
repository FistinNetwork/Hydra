package fr.fistin.hydra.servermanager;

import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.model.HostConfig;
import fr.fistin.hydra.Hydra;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.scheduler.ScheduledTask;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class LobbyServer extends Server {
    protected ScheduledTask healthyTask;
    public LobbyServer() {
        super("lobby", "https://fistincdn.blob.core.windows.net/serverdata/lobby/world.zip", "https://fistincdn.blob.core.windows.net/serverdata/lobby/plugins.zip", 10, new ServerOptions());
    }

    @Override
    public Integer getCurrentPlayers() {
        return ProxyServer.getInstance().getServerInfo(this.toString()).getPlayers().size();
    }

    public void start() {

        this.container = Hydra.getInstance().getDocker().getDockerClient().createContainerCmd("itzg/minecraft-server")
                .withEnv(this.options.getEnv())
                .withEnv("")
                .withHostConfig(new HostConfig().withAutoRemove(true))
                .withName(this.toString())
                .exec()
                .getId();
        Hydra.getInstance().getDocker().getDockerClient().startContainerCmd(this.container).exec();
        this.serverIP = Hydra.getInstance().getDocker().getDockerClient().inspectContainerCmd(this.container).exec().getNetworkSettings().getIpAddress();
        ServerInfo serverInfo = ProxyServer.getInstance().constructServerInfo(type, new InetSocketAddress(this.serverIP, 25565), "", false);
        ProxyServer.getInstance().getServers().put(this.toString(), serverInfo);
        healthyTask = Hydra.getInstance().getProxy().getScheduler().schedule(Hydra.getInstance(), this::checkStatut, 2, 2, TimeUnit.MINUTES);
    }

    protected void checkStatut() {
        if(this.container.isEmpty())
            return;
        InspectContainerResponse.ContainerState containerState = Hydra.getInstance().getDocker().getDockerClient().inspectContainerCmd(this.container).exec().getState();
        switch(containerState.getHealth().getStatus()){
            case "starting":
                this.currentState = ServerState.STARTING;
                break;

            case "unhealthy":
                this.currentState = ServerState.IDLE;
                break;

            case "healthy":
                this.currentState = ServerState.READY;
                break;

            default:
                this.currentState = ServerState.IDLE;
        }
    }

    @Override
    public boolean stop() {
        Hydra.getInstance().getProxy().getScheduler().cancel(healthyTask);
        return super.stop();
    }
}
