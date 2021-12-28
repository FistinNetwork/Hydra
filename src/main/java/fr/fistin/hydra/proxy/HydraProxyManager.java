package fr.fistin.hydra.proxy;

import fr.fistin.hydra.Hydra;
import fr.fistin.hydra.api.event.model.HydraProxyStartEvent;
import fr.fistin.hydra.api.event.model.HydraProxyStopEvent;
import fr.fistin.hydra.api.event.model.HydraServerStartEvent;
import fr.fistin.hydra.api.protocol.HydraChannel;
import fr.fistin.hydra.api.protocol.packet.model.proxy.HydraProxyServerActionPacket;
import fr.fistin.hydra.api.protocol.packet.model.proxy.HydraStopProxyPacket;
import fr.fistin.hydra.api.protocol.response.HydraResponseCallback;
import fr.fistin.hydra.api.protocol.response.HydraResponseType;
import fr.fistin.hydra.api.proxy.HydraProxy;
import fr.fistin.hydra.docker.swarm.DockerSwarm;

import java.util.HashSet;
import java.util.Set;

public class HydraProxyManager {

    private final Set<HydraProxy> proxies;

    private final DockerSwarm swarm;

    private final Hydra hydra;

    public HydraProxyManager(Hydra hydra) {
        this.hydra = hydra;
        this.swarm = this.hydra.getDocker().getSwarm();
        this.proxies = new HashSet<>();
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

            this.hydra.getAPI().getConnection().sendPacket(HydraChannel.PROXIES, packet).withResponseCallback(responseCallback).exec();

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
