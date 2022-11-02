package fr.fistin.hydra.server;

import fr.fistin.hydra.Hydra;
import fr.fistin.hydra.api.HydraAPI;
import fr.fistin.hydra.api.protocol.HydraChannel;
import fr.fistin.hydra.api.protocol.HydraConnection;
import fr.fistin.hydra.api.protocol.response.HydraResponseCallback;
import fr.fistin.hydra.api.protocol.response.HydraResponseType;
import fr.fistin.hydra.api.server.HydraServer;
import fr.fistin.hydra.api.server.HydraServerCreationInfo;
import fr.fistin.hydra.api.server.HydraServersService;
import fr.fistin.hydra.api.server.event.HydraServerStartedEvent;
import fr.fistin.hydra.api.server.event.HydraServerStoppedEvent;
import fr.fistin.hydra.api.server.event.HydraServerUpdatedEvent;
import fr.fistin.hydra.api.server.packet.HydraStopServerPacket;
import fr.fistin.hydra.docker.image.DockerImage;
import fr.fistin.hydra.docker.swarm.DockerSwarm;
import fr.fistin.hydra.util.References;

import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

public class HydraServerManager {

    public static final DockerImage SERVER_IMAGE = new DockerImage("hydra-server", "latest");

    private final HydraServersService serversService;
    private final DockerSwarm swarm;

    private final Hydra hydra;

    public HydraServerManager(Hydra hydra) {
        this.hydra = hydra;
        this.serversService = this.hydra.getAPI().getServersService();
        this.swarm = this.hydra.getDocker().getSwarm();

        this.hydra.getDocker().getImageManager().buildImage(Paths.get(References.IMAGES_FOLDER.toString(), "ServerDockerfile").toFile(), SERVER_IMAGE.getName());
    }

    public HydraServer startServer(HydraServerCreationInfo serverInfo) {
        final HydraServer server = new HydraServer(serverInfo);
        final HydraServerService service = new HydraServerService(server);

        this.swarm.runService(service);
        this.saveServer(server);
        this.hydra.getAPI().getEventBus().publish(new HydraServerStartedEvent(server));

        System.out.println("Starting '" + server + "' server...");

        return server;
    }

    public boolean stopServer(String name) {
        final HydraServer server = this.serversService.getServer(name);

        if (server == null) {
            System.err.println("Couldn't stop a server with the name: " + name + "!");
            return false;
        }

        System.out.println("Stopping '" + server.getName() + "' server...");

        server.setState(HydraServer.State.SHUTDOWN);

        this.updateServer(server);

        this.swarm.removeService(name);
        this.hydra.getAPI().getEventBus().publish(new HydraServerStoppedEvent(server));
        this.hydra.getRedis().process(jedis -> jedis.del(HydraServersService.HASH + server.getType() + ":" + server.getName()));

        return true;
    }

    public void updateServer(HydraServer server) {
        this.saveServer(server);
        this.hydra.getAPI().getEventBus().publish(new HydraServerUpdatedEvent(server));
    }

    public void saveServer(HydraServer server) {
        this.hydra.getRedis().process(jedis -> jedis.set(HydraServersService.HASH + server.getType() + ":" + server.getName(), HydraAPI.GSON.toJson(server)));
    }

}