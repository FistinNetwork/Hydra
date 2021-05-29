package fr.fistin.hydra.server;

import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.command.PullImageResultCallback;
import com.github.dockerjava.api.model.*;
import fr.fistin.hydra.Hydra;
import fr.fistin.hydra.server.template.HydraTemplate;
import fr.fistin.hydra.util.References;
import fr.fistin.hydraconnector.protocol.channel.HydraChannel;
import fr.fistin.hydraconnector.protocol.packet.event.ServerStartedPacket;
import fr.fistin.hydraconnector.protocol.packet.event.ServerStoppedPacket;
import fr.fistin.hydraconnector.protocol.packet.proxy.HookServerToProxyPacket;
import fr.fistin.hydraconnector.protocol.packet.proxy.RemoveServerFromProxyPacket;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

public class HydraServerManager {

    private final String minecraftServerImage = "itzg/minecraft-server";
    private final String minecraftServerImageTag = "java8";

    private final Map<String, HydraServer> servers;

    private final Hydra hydra;

    public HydraServerManager(Hydra hydra) {
        this.hydra = hydra;
        this.servers = new HashMap<>();
    }

    public void downloadMinecraftServerImage() {
        try {
            this.hydra.getLogger().log(Level.INFO, String.format("Pulling %s:%s image... (this might take few minutes)", this.minecraftServerImage, this.minecraftServerImageTag));
            this.hydra.getDocker().getDockerClient().pullImageCmd(this.minecraftServerImage).withTag(this.minecraftServerImageTag).exec(new PullImageResultCallback()).awaitCompletion(60, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            this.hydra.getLogger().log(Level.SEVERE, String.format("%s encountered an exception during download image: %s. Cause: %s", References.HYDRA, this.minecraftServerImage + ":" + this.minecraftServerImageTag, e.getMessage()));
            this.hydra.shutdown();
        }
    }

    @SuppressWarnings("deprecation")
    public void startServer(HydraServer server) {
        final List<String> env = server.getOptions().getEnv();
        if (server.getMapUrl() != null) env.add("WORLD=" + server.getMapUrl());
        if (server.getPluginUrl() != null) env.add("MODPACK=" +  server.getPluginUrl());;

        final Ports portsBinding = new Ports();
        portsBinding.bind(ExposedPort.tcp(25565), Ports.Binding.empty());

        server.setContainer(this.hydra.getDocker().getDockerClient()
                .createContainerCmd(this.minecraftServerImage + ":" + this.minecraftServerImageTag)
                .withEnv(env)
            .withHostConfig(new HostConfig().withAutoRemove(true).withPortBindings(portsBinding))
                .withHostName(server.toString())
                .withName(server.toString())
                .exec()
                .getId());

        this.hydra.getDocker().getDockerClient().startContainerCmd(server.getContainer()).exec();

        server.setStartedTime(System.currentTimeMillis());

        try {
            server.setServerIp(InetAddress.getLocalHost().getHostAddress());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        server.setHealthyTask(this.hydra.getScheduler().schedule(() ->  {
            if (server.getCurrentState() != ServerState.SHUTDOWN) server.checkStatus();
        }, server.getCheckAlive(), server.getCheckAlive(), TimeUnit.MILLISECONDS));

        final Ports.Binding[] bindings = this.hydra.getDocker().getDockerClient().inspectContainerCmd(server.getContainer()).exec().getNetworkSettings().getPorts().getBindings().get(ExposedPort.tcp(25565));
        final int port = Integer.parseInt(bindings[0].getHostPortSpec());

        server.setServerPort(port);

        System.out.println("Started " + server + " server");

        this.hydra.sendPacket(HydraChannel.EVENT, new ServerStartedPacket(server.toString(), server.getType()));
        this.hydra.sendPacket(HydraChannel.PROXIES, new HookServerToProxyPacket(server.toString(), server.getServerIp(), server.getServerPort()));
    }

    public void startServer(HydraTemplate template) {
        final HydraServer server = new HydraServer(
                this.hydra, template.getName(),
                template.getDependencies().getMapUrl(),
                template.getDependencies().getPluginUrl(),
                template.getHydraOptions().getSlots(),
                template.getHydraOptions().getCheckAlive(),
                template.getHydraOptions().getServerOptions());

        this.hydra.getScheduler().schedule(() -> this.startServer(server), template.getStartingOptions().getTimeToWait(), TimeUnit.MILLISECONDS);
    }

    public boolean stopServer(HydraServer server) {
        if (!server.getContainer().isEmpty()) {
            try {
                this.hydra.getDocker().getDockerClient().stopContainerCmd(server.getContainer()).exec();
                server.setCurrentState(ServerState.SHUTDOWN);
                this.hydra.getScheduler().cancel(server.getHealthyTask().getId());

                if (!this.hydra.isStopping()) this.hydra.getScheduler().schedule(() -> this.checkIfServerHasShutdown(server), 1,  TimeUnit.MINUTES);

                this.hydra.sendPacket(HydraChannel.EVENT, new ServerStoppedPacket(server.toString(), server.getType()));
                this.hydra.sendPacket(HydraChannel.PROXIES, new RemoveServerFromProxyPacket(server.toString()));

                System.out.println("Stopped " + server + " server. (Checking if really in 1 minutes if Hydra is not stopping)");

                return true;
            } catch (Exception e) {
                this.hydra.getLogger().log(Level.SEVERE, String.format("The server: %s didn't stopped !", server));
            }
        }
        return false;
    }

    public void stopAllServers() {
        this.hydra.getLogger().log(Level.INFO, "Stopping all servers currently running...");

        for (Map.Entry<String, HydraServer> entry : this.servers.entrySet()) {
            this.hydra.getScheduler().runTaskAsynchronously(() -> this.stopServer(entry.getValue()));
        }
    }

    public void checkIfServerHasShutdown(HydraServer server) {
        final List<Container> containers = this.hydra.getDocker().getDockerClient().listContainersCmd().exec();
        for (Container container : containers) {
            if (server.getContainer().equals(container.getId())) {
                this.hydra.getLogger().log(Level.SEVERE,  String.format("The server: %s didn't stopped !", server));
                this.hydra.getLogger().log(Level.INFO,  String.format("Trying to kill the server: %s", server));
                this.hydra.getDocker().getDockerClient().killContainerCmd(server.getContainer()).exec();
            }
        }
        if (!this.hydra.isStopping()) this.removeServer(server);
    }

    public void checkIfAllServersHaveShutdown() {
        this.hydra.getLogger().log(Level.INFO, "Checking if all servers have shutdown...");

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