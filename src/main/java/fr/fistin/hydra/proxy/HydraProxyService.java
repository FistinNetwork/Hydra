package fr.fistin.hydra.proxy;

import fr.fistin.hydra.Hydra;
import fr.fistin.hydra.api.proxy.HydraProxy;
import fr.fistin.hydra.api.server.HydraServer;
import fr.fistin.hydra.docker.image.DockerImage;
import fr.fistin.hydra.docker.network.DockerNetwork;
import fr.fistin.hydra.docker.swarm.DockerService;
import fr.fistin.hydra.util.PortUtil;
import fr.fistin.hydra.util.References;

import java.util.Base64;

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

        this.addLabel(References.STACK_NAMESPACE_LABEL, References.STACK_NAME);
        this.addEnv("TYPE", "WATERFALL");
        this.addEnv("ENABLE_RCON", "FALSE");
        this.addEnv("PLUGINS", "https://hyriode.fr/HydraBungee-1.0.0-all.jar");

        this.addEnv("PUBLIC_KEY", Base64.getEncoder().encodeToString(hydra.getPublicKey().getEncoded()));

        proxy.setPort(this.publishedPort);
    }

}
