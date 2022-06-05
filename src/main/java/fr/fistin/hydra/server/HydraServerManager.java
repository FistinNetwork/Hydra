package fr.fistin.hydra.server;

import fr.fistin.hydra.Hydra;
import fr.fistin.hydra.api.event.model.HydraServerStartEvent;
import fr.fistin.hydra.api.event.model.HydraServerStopEvent;
import fr.fistin.hydra.api.protocol.HydraChannel;
import fr.fistin.hydra.api.protocol.HydraConnection;
import fr.fistin.hydra.api.protocol.packet.model.proxy.HydraProxyServerActionPacket;
import fr.fistin.hydra.api.protocol.packet.model.server.HydraStopServerPacket;
import fr.fistin.hydra.api.protocol.response.HydraResponseCallback;
import fr.fistin.hydra.api.protocol.response.HydraResponseType;
import fr.fistin.hydra.api.server.HydraServer;
import fr.fistin.hydra.docker.image.DockerImage;
import fr.fistin.hydra.docker.swarm.DockerSwarm;
import fr.fistin.hydra.util.References;

import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

public class HydraServerManager {

    public static final DockerImage SERVER_IMAGE = new DockerImage("hydra-server", "latest");

    private final Set<HydraServer> servers;

    private final DockerSwarm swarm;

    private final HydraConnection connection;
    private final Hydra hydra;

    public HydraServerManager(Hydra hydra) {
        this.hydra = hydra;
        this.connection = this.hydra.getAPI().getConnection();
        this.swarm = this.hydra.getDocker().getSwarm();
        this.servers = new HashSet<>();

        this.hydra.getDocker().getImageManager().buildImage(Paths.get(References.IMAGES_FOLDER.toString(), "ServerDockerfile").toFile(), SERVER_IMAGE.getName());
    }

    public void startServer(String type) {
        final HydraServer server = new HydraServer(type);
        final HydraServerService service = new HydraServerService(this.hydra, server);

        this.swarm.runService(service);
        this.servers.add(server);

        this.connection.sendPacket(HydraChannel.PROXIES, new HydraProxyServerActionPacket(HydraProxyServerActionPacket.Action.ADD, server.getName()))
                .exec();
        this.hydra.getAPI().getEventBus().publish(new HydraServerStartEvent(server.getName()));

        System.out.println("Starting '" + server + "' server...");
    }

    public boolean stopServer(String name) {
        final HydraServer server = this.getServerByName(name);

        if (server != null) {
            final HydraStopServerPacket packet = new HydraStopServerPacket(name);
            final HydraResponseCallback responseCallback = response -> {
                if (response.getType() != HydraResponseType.OK) {
                    System.err.println("'" + server.getName() + "' server doesn't want to stop! Response: " + response + ". Reason: " + response.getMessage() + ". Forcing it to stop!");
                }

                this.swarm.removeService(name);

                this.hydra.getAPI().getEventBus().publish(new HydraServerStopEvent(server.getName()));

                System.out.println("Stopping '" + server.getName() + "' server...");
            };

            this.connection.sendPacket(HydraChannel.PROXIES, new HydraProxyServerActionPacket(HydraProxyServerActionPacket.Action.REMOVE, name)).exec();
            this.connection.sendPacket(HydraChannel.SERVERS, packet)
                    .withResponseCallback(responseCallback)
                    .exec();

            return true;
        } else {
            System.err.println("Couldn't stop a server with the name: " + name + "!");
        }
        return false;
    }

    public HydraServer getServerByName(String name) {
        for (HydraServer server : this.servers) {
            if (server.getName().equals(name)) {
                return server;
            }
        }
        return null;
    }

    public Set<HydraServer> getServers() {
        return this.servers;
    }

}