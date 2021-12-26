package fr.fistin.hydra.server;

import fr.fistin.hydra.api.server.HydraServer;
import fr.fistin.hydra.docker.image.DockerImage;
import fr.fistin.hydra.docker.network.DockerNetwork;
import fr.fistin.hydra.docker.swarm.DockerService;
import fr.fistin.hydra.util.References;

/**
 * Project: Hydra
 * Created by AstFaster
 * on 27/11/2021 at 09:38
 */
public class HydraServerService extends DockerService {

    private static final DockerImage SERVER_IMAGE = new DockerImage("itzg/minecraft-server", "java8");

    public HydraServerService(HydraServer server) {
        super(server.getName(), SERVER_IMAGE, DockerNetwork.FISTIN_NETWORK);

        this.hostname = server.getName();
        this.labels.put(References.STACK_NAMESPACE_LABEL, References.STACK_NAME);
        this.envs = server.getOptions().getEnvs();
    }

}
