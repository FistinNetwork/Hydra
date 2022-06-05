package fr.fistin.hydra.server;

import fr.fistin.hydra.Hydra;
import fr.fistin.hydra.api.server.HydraServer;
import fr.fistin.hydra.config.HydraConfig;
import fr.fistin.hydra.docker.image.DockerImage;
import fr.fistin.hydra.docker.network.DockerNetwork;
import fr.fistin.hydra.docker.swarm.DockerService;
import fr.fistin.hydra.util.References;

import java.util.ArrayList;
import java.util.List;

/**
 * Project: Hydra
 * Created by AstFaster
 * on 27/11/2021 at 09:38
 */
public class HydraServerService extends DockerService {

    public HydraServerService(Hydra hydra, HydraServer server) {
        super(server.getName(), HydraServerManager.SERVER_IMAGE, DockerNetwork.HYDRA);
        this.hostname = server.getName();
        this.labels.put(References.STACK_NAMESPACE_LABEL, HydraConfig.get().getDocker().getStackName());
    }

}
