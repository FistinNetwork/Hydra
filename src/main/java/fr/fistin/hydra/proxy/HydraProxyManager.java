package fr.fistin.hydra.proxy;

import fr.fistin.hydra.Hydra;
import fr.fistin.hydra.api.HydraAPI;
import fr.fistin.hydra.api.proxy.HydraProxiesService;
import fr.fistin.hydra.api.proxy.HydraProxy;
import fr.fistin.hydra.api.proxy.HydraProxyCreationInfo;
import fr.fistin.hydra.api.proxy.event.HydraProxyStartedEvent;
import fr.fistin.hydra.api.proxy.event.HydraProxyStoppedEvent;
import fr.fistin.hydra.api.proxy.event.HydraProxyUpdatedEvent;

public class HydraProxyManager {

    private final HydraProxiesService proxiesService;
    private final HydraProxiesHandler handler;

    private final Hydra hydra;

    public HydraProxyManager(Hydra hydra) {
        this.hydra = hydra;
        this.proxiesService = this.hydra.getAPI().getProxiesService();
        this.handler = new HydraProxiesHandler();
    }

    public HydraProxy startProxy(HydraProxyCreationInfo proxyInfo) {
        final HydraProxy proxy = new HydraProxy(proxyInfo.getData(), 25577); // TODO Find a way to not expose port (by using HAProxy), so by using service name but dynamically and not statically

        this.handler.startProxy(proxy);
        this.hydra.getAPI().getEventBus().publish(new HydraProxyStartedEvent(proxy));

        System.out.println("Started " + proxy.getName() + " (port: " + proxy.getPort() + ").");

        return proxy;
    }

    public boolean stopProxy(String name) {
        final HydraProxy proxy = this.proxiesService.getProxy(name);

        this.handler.stopProxy(name);

        if (proxy == null) {
            System.err.println("Couldn't stop a proxy with the name: " + name + "!");
            return false;
        }

        proxy.setState(HydraProxy.State.SHUTDOWN);

        this.updateProxy(proxy);

        this.hydra.getAPI().getEventBus().publish(new HydraProxyStoppedEvent(proxy));
        this.hydra.getRedis().process(jedis -> jedis.del(HydraProxiesService.HASH + proxy.getName()));

        System.out.println("Stopped '" + proxy.getName() + "'.");

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
