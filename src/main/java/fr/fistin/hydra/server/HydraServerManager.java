package fr.fistin.hydra.server;

import fr.fistin.hydra.Hydra;
import fr.fistin.hydra.docker.image.DockerImage;

public class HydraServerManager {

    private static final DockerImage SERVER_IMAGE = new DockerImage("itzg/minecraft-server", "java8");

    private final Hydra hydra;

    public HydraServerManager(Hydra hydra) {
        this.hydra = hydra;
    }

}