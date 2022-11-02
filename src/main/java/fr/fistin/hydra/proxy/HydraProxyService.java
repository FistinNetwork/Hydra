package fr.fistin.hydra.proxy;

import fr.fistin.hydra.Hydra;
import fr.fistin.hydra.api.proxy.HydraProxy;
import fr.fistin.hydra.config.HydraConfig;
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

    public HydraProxyService(HydraProxy proxy) {
        super(proxy.getName(), HydraProxyManager.PROXY_IMAGE, DockerNetwork.HYDRA);
        this.hostname = proxy.getName();
        this.targetPort = 25577;
        this.publishedPort = proxy.getPort();

        this.labels.put(References.STACK_NAMESPACE_LABEL, Hydra.get().getConfig().getDocker().getStackName());
    }

}
