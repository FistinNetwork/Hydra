package fr.fistin.hydra.proxy;

import fr.fistin.hydra.Hydra;
import fr.fistin.hydra.api.proxy.HydraProxy;
import fr.fistin.hydra.api.server.HydraServer;
import fr.fistin.hydra.docker.image.DockerImage;
import fr.fistin.hydra.docker.network.DockerNetwork;
import fr.fistin.hydra.docker.swarm.DockerService;
import fr.fistin.hydra.util.PortUtil;
import fr.fistin.hydra.util.References;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

/**
 * Project: Hydra
 * Created by AstFaster
 * on 27/11/2021 at 14:21
 */
public class HydraProxyService extends DockerService {

    private static final DockerImage PROXY_IMAGE = new DockerImage("itzg/bungeecord", "java11");

    private static final int MIN_PORT = 45565;
    private static final int MAX_PORT = 65535;

    public HydraProxyService(Hydra hydra, HydraProxy proxy) {
        super(proxy.getName(), PROXY_IMAGE, DockerNetwork.FISTIN_NETWORK);

        this.hostname = proxy.getName();
        this.targetPort = 25577;
        this.publishedPort = PortUtil.nextAvailablePort(MIN_PORT, MAX_PORT);

        this.labels.put(References.STACK_NAMESPACE_LABEL, References.STACK_NAME);
        this.envs = this.getEnvs(hydra);

        proxy.setPort(this.publishedPort);
    }

    private List<String> getEnvs(Hydra hydra) {
        final List<String> envs = new ArrayList<>();

        envs.add("TYPE=WATERFALL");
        envs.add("ENABLE_RCON=FALSE");
        envs.add("PLUGINS=https://hyriode.fr/HydraBungee-1.0.0-all.jar");

        envs.addAll(hydra.getEnvironment().get());

        return envs;
    }

}
