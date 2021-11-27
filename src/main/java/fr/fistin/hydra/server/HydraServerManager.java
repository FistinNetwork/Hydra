package fr.fistin.hydra.server;

import fr.fistin.hydra.Hydra;
import fr.fistin.hydra.api.server.HydraServer;
import fr.fistin.hydra.api.server.HydraServerOptions;
import fr.fistin.hydra.docker.image.DockerImage;
import fr.fistin.hydra.docker.network.DockerNetwork;
import fr.fistin.hydra.docker.swarm.DockerService;

import java.util.UUID;

public class HydraServerManager {

    private final Hydra hydra;

    public HydraServerManager(Hydra hydra) {
        this.hydra = hydra;
    }

    public void startServer() {
        final HydraServer server = new HydraServer("test");
        final HydraServerService service = new HydraServerService(server);

        this.hydra.getDocker().getSwarmManager().runService(service);

        System.out.println("Starting '" + server + "'...");
    }

}