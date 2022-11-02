package fr.fistin.hydra.proxy;

import fr.fistin.hydra.Hydra;
import fr.fistin.hydra.api.HydraAPI;
import fr.fistin.hydra.api.protocol.HydraChannel;
import fr.fistin.hydra.api.protocol.response.HydraResponseCallback;
import fr.fistin.hydra.api.protocol.response.HydraResponseType;
import fr.fistin.hydra.api.proxy.HydraProxiesService;
import fr.fistin.hydra.api.proxy.HydraProxy;
import fr.fistin.hydra.api.proxy.HydraProxyCreationInfo;
import fr.fistin.hydra.api.proxy.event.HydraProxyStartedEvent;
import fr.fistin.hydra.api.proxy.event.HydraProxyStoppedEvent;
import fr.fistin.hydra.api.proxy.event.HydraProxyUpdatedEvent;
import fr.fistin.hydra.api.proxy.packet.HydraStopProxyPacket;
import fr.fistin.hydra.docker.image.DockerImage;
import fr.fistin.hydra.docker.swarm.DockerSwarm;
import fr.fistin.hydra.util.References;

import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

public class HydraProxyManager {

    public static final DockerImage PROXY_IMAGE = new DockerImage("hydra-proxy", "latest");

    private final HydraProxiesService proxiesService;
    private final DockerSwarm swarm;

    private final Hydra hydra;

    public HydraProxyManager(Hydra hydra) {
        this.hydra = hydra;
        this.proxiesService = this.hydra.getAPI().getProxiesService();
        this.swarm = this.hydra.getDocker().getSwarm();

        this.hydra.getDocker().getImageManager().buildImage(Paths.get(References.IMAGES_FOLDER.toString(), "ProxyDockerfile").toFile(), PROXY_IMAGE.getName());
    }

    public HydraProxy startProxy(HydraProxyCreationInfo proxyInfo) {
        final HydraProxy proxy = new HydraProxy(proxyInfo.getData(), 25565); // TODO Find a way to not expose port (by using HAProxy), so by using service name but dynamically and not statically
        final HydraProxyService service = new HydraProxyService(proxy);

        this.swarm.runService(service);

        this.hydra.getAPI().getEventBus().publish(new HydraProxyStartedEvent(proxy));

        System.out.println("Started " + proxy.getName() + " (port: " + proxy.getPort() + ").");

        return proxy;
    }

    public boolean stopProxy(String name) {
        final HydraProxy proxy = this.proxiesService.getProxy(name);

        if (proxy == null) {
            System.err.println("Couldn't stop a proxy with the name: " + name + "!");
            return false;
        }

        System.out.println("Stopping " + proxy.getName() + "...");

        proxy.setState(HydraProxy.State.SHUTDOWN);

        this.updateProxy(proxy);

        this.swarm.removeService(name);
        this.hydra.getAPI().getEventBus().publish(new HydraProxyStoppedEvent(proxy));
        this.hydra.getRedis().process(jedis -> jedis.del(HydraProxiesService.HASH + proxy.getName()));

        return true;
    }

    public void updateProxy(HydraProxy proxy) {
        this.saveProxy(proxy);
        this.hydra.getAPI().getEventBus().publish(new HydraProxyUpdatedEvent(proxy));
    }

    public void saveProxy(HydraProxy proxy) {
        this.hydra.getRedis().process(jedis -> jedis.set(HydraProxiesService.HASH + proxy.getName(), HydraAPI.GSON.toJson(proxy)));
    }

}
