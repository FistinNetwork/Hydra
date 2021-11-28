package fr.fistin.hydra.proxy;

import fr.fistin.hydra.api.proxy.HydraProxy;
import fr.fistin.hydra.api.server.HydraServer;
import fr.fistin.hydra.docker.image.DockerImage;
import fr.fistin.hydra.docker.network.DockerNetwork;
import fr.fistin.hydra.docker.swarm.DockerService;
import fr.fistin.hydra.util.PortUtil;
import fr.fistin.hydra.util.References;

/**
 * Project: Hydra
 * Created by AstFaster
 * on 27/11/2021 at 14:21
 */
public class HydraProxyService extends DockerService {

    public static final DockerImage PROXY_IMAGE = new DockerImage("itzg/bungeecord", "latest");

    private static final int MIN_PORT = 45565;
    private static final int MAX_PORT = 65535;

    public HydraProxyService(HydraProxy proxy) {
        super(proxy.getName(), PROXY_IMAGE, DockerNetwork.FISTIN_NETWORK);

        this.hostname = proxy.getName();
        this.targetPort = 25577;
        this.publishedPort = PortUtil.nextAvailablePort(MIN_PORT, MAX_PORT);

        this.addLabel("com.docker.stack.namespace", References.STACK_NAME);
        this.addEnv("TYPE", "BUNGEECORD");
        this.addEnv("ENABLE_RCON", "FALSE");

        proxy.setPort(this.publishedPort);
    }

}
