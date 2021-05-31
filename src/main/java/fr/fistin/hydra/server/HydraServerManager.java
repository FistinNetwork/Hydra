package fr.fistin.hydra.server;

import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.model.Container;
import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.Ports;
import fr.fistin.hydra.Hydra;
import fr.fistin.hydra.docker.container.DockerContainer;
import fr.fistin.hydra.docker.image.DockerImage;
import fr.fistin.hydra.server.template.HydraTemplate;
import fr.fistin.hydra.util.References;
import fr.fistin.hydraconnector.api.ServerState;
import fr.fistin.hydraconnector.protocol.channel.HydraChannel;
import fr.fistin.hydraconnector.protocol.packet.event.ServerStartedPacket;
import fr.fistin.hydraconnector.protocol.packet.event.ServerStoppedPacket;
import fr.fistin.hydraconnector.protocol.packet.proxy.HookServerToProxyPacket;
import fr.fistin.hydraconnector.protocol.packet.proxy.RemoveServerFromProxyPacket;
import fr.fistin.hydraconnector.protocol.packet.server.EvacuateServerPacket;
import redis.clients.jedis.Jedis;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

public class HydraServerManager {

    private final DockerImage minecraftServerImage = new DockerImage("itzg/minecraft-server", "java8");

    private final Map<String, HydraServer> servers;

    private final Hydra hydra;

    public HydraServerManager(Hydra hydra) {
        this.hydra = hydra;
        this.servers = new HashMap<>();
    }

    public void pullMinecraftServerImage() {
        this.hydra.getImageManager().pullImage(this.minecraftServerImage);
    }

    public void startServer(HydraServer server) {
        final List<String> env = server.getOptions().getEnv();
        if (server.getMapUrl() != null) env.add("WORLD=" + server.getMapUrl());
        if (server.getPluginUrl() != null) env.add("MODPACK=" +  server.getPluginUrl());

        final DockerContainer container = new DockerContainer(server.toString(), this.minecraftServerImage);
        container.setHostname(server.toString());
        container.setEnvs(env);
        container.setPublishedPort(25565);

        server.setContainer(this.hydra.getContainerManager().runContainer(container));
        server.setStartedTime(System.currentTimeMillis());

        try {
            server.setServerIp(InetAddress.getLocalHost().getHostAddress());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        server.setHealthyTask(this.hydra.getScheduler().schedule(() ->  {
            if (server.getCurrentState() != ServerState.SHUTDOWN) server.checkStatus();
        }, server.getCheckAlive(), server.getCheckAlive(), TimeUnit.MILLISECONDS));

        final Ports.Binding[] bindings = this.hydra.getContainerManager().inspectContainer(container).getNetworkSettings().getPorts().getBindings().get(ExposedPort.tcp(25565));
        final int port = Integer.parseInt(bindings[0].getHostPortSpec());

        server.setServerPort(port);

        System.out.println("Started " + server + " server");

        this.hydra.sendPacket(HydraChannel.EVENT, new ServerStartedPacket(server.toString(), server.getType()));
        this.hydra.sendPacket(HydraChannel.PROXIES, new HookServerToProxyPacket(server.toString(), server.getServerIp(), server.getServerPort()));

        this.sendServerInfoToRedis(server);
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
                this.hydra.getContainerManager().stopContainer(server.getContainer());

                server.setCurrentState(ServerState.SHUTDOWN);

                this.hydra.getScheduler().cancel(server.getHealthyTask().getId());

                if (!this.hydra.isStopping()) this.hydra.getScheduler().schedule(() -> this.checkIfServerHasShutdown(server), 1,  TimeUnit.MINUTES);

                this.hydra.sendPacket(HydraChannel.EVENT, new ServerStoppedPacket(server.toString(), server.getType()));
                this.hydra.sendPacket(HydraChannel.PROXIES, new RemoveServerFromProxyPacket(server.toString()));

                this.removeServerInfoFromRedis(server);

                System.out.println("Stopped " + server + " server. (Checking if really in 1 minutes if Hydra is not stopping)");

                return true;
            } catch (Exception e) {
                this.hydra.getLogger().log(Level.SEVERE, String.format("The server: %s didn't stopped !", server));
            }
        }
        return false;
    }

    public void evacuateServer(HydraServer server, HydraServer destinationServer) {
        this.evacuateServer(server.toString(), destinationServer.toString());
    }

    public void evacuateServer(String serverId, String destinationServerId) {
        System.out.println("Evacuating '" + serverId + "' server to '" + destinationServerId + "' server...");

        this.hydra.getHydraConnector().getConnectionManager().sendPacket(HydraChannel.PROXIES, new EvacuateServerPacket(serverId, destinationServerId));
    }

    public void sendServerInfoToRedis(HydraServer server) {
        final Jedis jedis = this.hydra.getHydraConnector().getRedisConnection().getJedis();
        final String hash = References.HYDRA_REDIS_HASH + server.toString() + ":";

        jedis.hmset(hash, this.getServerInfo(server));
    }

    public void removeServerInfoFromRedis(HydraServer server) {
        final Jedis jedis = this.hydra.getHydraConnector().getRedisConnection().getJedis();
        final String hash = References.HYDRA_REDIS_HASH + server.toString() + ":";

        for (Map.Entry<String, String> entry : this.getServerInfo(server).entrySet()) {
            jedis.hdel(hash, entry.getKey());
        }
    }

    private Map<String, String> getServerInfo(HydraServer server) {
        final Map<String, String> infos = new HashMap<>();

        infos.put("slots", String.valueOf(server.getSlots()));
        infos.put("currentPlayers", String.valueOf(server.getCurrentPlayers()));
        infos.put("state", String.valueOf(server.getCurrentState().getId()));
        infos.put("startedTime", String.valueOf(server.getStartedTime()));
        infos.put("ip", server.getServerIp());
        infos.put("port", String.valueOf(server.getServerPort()));

        return infos;
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
            final List<Container> containers = this.hydra.getContainerManager().listContainers();

            for (Container container : containers) {
                if (container.getId().equals(server.getContainer())) {
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
                            break;
                    }
                    return;
                }
            }

            System.err.println("'" + server + "' is no longer running !");

            this.hydra.getScheduler().cancel(server.getHealthyTask().getId());

            this.hydra.sendPacket(HydraChannel.EVENT, new ServerStoppedPacket(server.toString(), server.getType()));
            this.hydra.sendPacket(HydraChannel.PROXIES, new RemoveServerFromProxyPacket(server.toString()));

            this.removeServer(server);
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