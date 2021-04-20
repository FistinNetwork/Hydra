package fr.fistin.hydra.server;

import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.command.PullImageResultCallback;
import com.github.dockerjava.api.model.*;
import fr.fistin.hydra.Hydra;
import fr.fistin.hydra.util.References;
import fr.fistin.hydra.util.logger.LogType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class HydraServerManager {

    private final String minecraftServerImage = "itzg/minecraft-server";
    private final String minecraftServerImageTag = "java8";

    private final Map<String, HydraServer> servers;

    private final Hydra hydra;

    public HydraServerManager(Hydra hydra) {
        this.hydra = hydra;
        this.servers = new HashMap<>();

        this.hydra.getLogger().log(LogType.INFO, "Starting server manager...");
    }

    public void downloadMinecraftServerImage() {
        try {
            this.hydra.getLogger().log(LogType.INFO, String.format("Pulling %s:%s image... (this might take few minutes)", this.minecraftServerImage, this.minecraftServerImageTag));
            this.hydra.getDocker().getDockerClient().pullImageCmd(this.minecraftServerImage).withTag(this.minecraftServerImageTag).exec(new PullImageResultCallback()).awaitCompletion(60, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            this.hydra.getLogger().log(LogType.ERROR, String.format("%s encountered an exception during download image: %s. Cause: %s", References.HYDRA, this.minecraftServerImage + ":" + this.minecraftServerImageTag, e.getMessage()));
            this.hydra.shutdown();
        }
    }

    @SuppressWarnings("deprecation")
    public void startServer(HydraServer server) {
        //TODO Link to Bungeecord
        server.setContainer(this.hydra.getDocker().getDockerClient()
                .createContainerCmd(this.minecraftServerImage + ":" + this.minecraftServerImageTag)
                .withEnv(server.getOptions().getEnv())
                .withHostConfig(new HostConfig().withAutoRemove(true))
                .withName(server.toString())
                .exec()
                .getId());
        this.hydra.getDocker().getDockerClient().startContainerCmd(server.getContainer()).exec();
        server.setStartedTime(System.currentTimeMillis());
        server.setServerIp(this.hydra.getDocker().getDockerClient().inspectContainerCmd(server.getContainer()).exec().getNetworkSettings().getIpAddress());
    }

    public boolean stopServer(HydraServer server) {
        if (!server.getContainer().isEmpty()) {
            try {
                this.hydra.getDocker().getDockerClient().stopContainerCmd(server.getContainer()).exec();
                server.setCurrentState(ServerState.SHUTDOWN);

                if (!this.hydra.isStopping()) this.hydra.getScheduler().schedule(() -> this.checkIfServerHasShutdown(server), 1, 0, TimeUnit.MINUTES);

                return true;
            } catch (Exception e) {
                this.hydra.getLogger().log(LogType.ERROR, String.format("Le serveur: %s ne s'est pas stoppé !", server.toString()));
            }
        }
        return false;
    }

    public void stopAllServers() {
        this.hydra.getLogger().log(LogType.INFO, "Stopping all servers currently running...");

        for (Map.Entry<String, HydraServer> entry : this.servers.entrySet()) {
            this.hydra.getScheduler().runTaskAsynchronously(() -> this.stopServer(entry.getValue()));
        }
    }

    public void checkIfServerHasShutdown(HydraServer server) {
        final List<Container> containers = this.hydra.getDocker().getDockerClient().listContainersCmd().exec();
        for (Container container : containers) {
            if (server.getContainer().equals(container.getId())) {
                this.hydra.getLogger().log(LogType.ERROR,  String.format("Le serveur: %s ne s'est pas stoppé !", server.toString()));
                this.hydra.getLogger().log(LogType.INFO,  String.format("Tentative de kill sur le serveur: %s", server.toString()));
                this.hydra.getDocker().getDockerClient().killContainerCmd(server.getContainer()).exec();
            }
        }
        if (!this.hydra.isStopping()) this.removeServer(server);
    }

    public void checkIfAllServersHaveShutdown() {
        this.hydra.getLogger().log(LogType.INFO, "Checking if all servers have shutdown...");

        for (Map.Entry<String, HydraServer> entry : this.servers.entrySet()) {
            this.checkIfServerHasShutdown(entry.getValue());
        }
        this.servers.clear();
    }

    public void checkStatus(HydraServer server) {
        if (!server.getContainer().isEmpty()) {
            final InspectContainerResponse.ContainerState containerState = this.hydra.getDocker().getDockerClient()
                    .inspectContainerCmd(server.getContainer())
                    .exec()
                    .getState();

            switch (containerState.getHealth().getStatus()) {
                case "starting":
                    server.setCurrentState(ServerState.STARTING);
                    break;
                case "healthy":
                    server.setCurrentState(ServerState.READY);
                    break;
                default:
                    server.setCurrentState(ServerState.IDLE);
            }
        }
    }

    public void addServer(HydraServer server) {
        this.servers.putIfAbsent(server.toString(), server);
    }

    public void removeServer(HydraServer server) {
        this.servers.remove(server.toString());
    }

    public HydraServer getServerByName(String name) {
        return this.servers.getOrDefault(name, null);
    }

    public Map<String, HydraServer> getServers() {
        return this.servers;
    }
}