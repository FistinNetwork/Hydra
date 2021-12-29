package fr.fistin.hydra.server;

import fr.fistin.hydra.Hydra;
import fr.fistin.hydra.api.server.HydraServer;
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

    private static final DockerImage SERVER_IMAGE = new DockerImage("itzg/minecraft-server", "java8");

    public HydraServerService(Hydra hydra, HydraServer server) {
        super(server.getName(), SERVER_IMAGE, DockerNetwork.FISTIN_NETWORK);

        this.hostname = server.getName();
        this.labels.put(References.STACK_NAMESPACE_LABEL, References.STACK_NAME);
        this.envs = this.getEnvs(hydra, server);
    }

    private List<String> getEnvs(Hydra hydra, HydraServer server) {
        final List<String> envs = new ArrayList<>();

        envs.add("EULA=TRUE");
        envs.add("TYPE=SPIGOT");
        envs.add("VERSION=1.8.8-R0.1-SNAPSHOT-latest");
        envs.add("ENABLE_RCON=FALSE");
        envs.add("ONLINE_MODE=FALSE");

        envs.addAll(server.getOptions().getEnvs());
        envs.addAll(hydra.getEnvironment().get());

        return envs;
    }

}
