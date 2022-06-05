package fr.fistin.hydra.proxy;

import fr.fistin.hydra.Hydra;
import fr.fistin.hydra.api.event.model.HydraProxyStartEvent;
import fr.fistin.hydra.api.event.model.HydraProxyStopEvent;
import fr.fistin.hydra.api.protocol.HydraChannel;
import fr.fistin.hydra.api.protocol.packet.model.proxy.HydraStopProxyPacket;
import fr.fistin.hydra.api.protocol.response.HydraResponseCallback;
import fr.fistin.hydra.api.protocol.response.HydraResponseType;
import fr.fistin.hydra.api.proxy.HydraProxy;
import fr.fistin.hydra.docker.image.DockerImage;
import fr.fistin.hydra.docker.swarm.DockerSwarm;
import fr.fistin.hydra.util.References;

import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

public class HydraProxyManager {

    public static final DockerImage PROXY_IMAGE = new DockerImage("hydra-proxy", "latest");

    private final Set<HydraProxy> proxies;

    private final DockerSwarm swarm;

    private final Hydra hydra;

    public HydraProxyManager(Hydra hydra) {
        this.hydra = hydra;
        this.swarm = this.hydra.getDocker().getSwarm();
        this.proxies = new HashSet<>();

        this.hydra.getDocker().getImageManager().buildImage(Paths.get(References.IMAGES_FOLDER.toString(), "ProxyDockerfile").toFile(), PROXY_IMAGE.getName());
    }

    public void startProxy() {
        final HydraProxy proxy = new HydraProxy();
        final HydraProxyService service = new HydraProxyService(this.hydra, proxy);

        this.swarm.runService(service);
        this.proxies.add(proxy);

        this.hydra.getAPI().getEventBus().publish(new HydraProxyStartEvent(proxy.getName()));

        System.out.println("Started " + proxy.getName() + " (port: " + proxy.getPort() + ").");
    }

    public boolean stopProxy(String name) {
        final HydraProxy proxy = this.getProxyByName(name);

        if (proxy != null) {
            final HydraStopProxyPacket packet = new HydraStopProxyPacket(name);
            final HydraResponseCallback responseCallback = response -> {
                if (response.getType() != HydraResponseType.OK) {
                    System.err.println(proxy.getName() + " doesn't want to stop! Response: " + response + ". Reason: " + response.getMessage() + ". Forcing it to stop!");
                }

                this.swarm.removeService(name);

                this.hydra.getAPI().getEventBus().publish(new HydraProxyStopEvent(proxy.getName()));

                System.out.println("Stopping " + proxy.getName() + "...");
            };

            this.hydra.getAPI().getConnection().sendPacket(HydraChannel.PROXIES, packet)
                    .withResponseCallback(responseCallback)
                    .exec();

            return true;
        } else {
            System.err.println("Couldn't stop a proxy with the name: " + name + "!");
        }
        return false;
    }

    public HydraProxy getProxyByName(String name) {
        for (HydraProxy proxy : this.proxies) {
            if (proxy.getName().equals(name)) {
                return proxy;
            }
        }
        return null;
    }

    public Set<HydraProxy> getProxies() {
        return this.proxies;
    }

}
