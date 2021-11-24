package fr.fistin.hydra.proxy;

import fr.fistin.hydra.Hydra;
import fr.fistin.hydra.docker.image.DockerImage;

public class HydraProxyManager {

    private static final DockerImage PROXY_IMAGE = new DockerImage("itzg/bungeecord", "latest");

    private final Hydra hydra;

    public HydraProxyManager(Hydra hydra) {
        this.hydra = hydra;
    }

}
