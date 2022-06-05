package fr.fistin.hydra.proxy;

import fr.fistin.hydra.Hydra;
import fr.fistin.hydra.api.proxy.HydraProxy;
import fr.fistin.hydra.config.HydraConfig;
import fr.fistin.hydra.docker.image.DockerImage;
import fr.fistin.hydra.docker.network.DockerNetwork;
import fr.fistin.hydra.docker.swarm.DockerService;
import fr.fistin.hydra.util.PortUtil;
import fr.fistin.hydra.util.References;

import java.util.ArrayList;
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
        super(proxy.getName(), PROXY_IMAGE, DockerNetwork.HYDRA);
        this.hostname = proxy.getName();
        this.targetPort = 25577;
        this.publishedPort = PortUtil.nextAvailablePort(MIN_PORT, MAX_PORT);

        this.labels.put(References.STACK_NAMESPACE_LABEL, HydraConfig.get().getDocker().getStackName());

        proxy.setPort(this.publishedPort);
    }

}
