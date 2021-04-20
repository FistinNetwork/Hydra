package fr.fistin.hydra.proxy;

import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.command.PullImageResultCallback;
import com.github.dockerjava.api.model.*;
import fr.fistin.hydra.Hydra;
import fr.fistin.hydra.util.References;
import fr.fistin.hydra.util.logger.LogType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class HydraProxyManager {

    private final String minecraftProxyImage = "itzg/bungeecord";
    private final String minecraftProxyImageTag = "latest";

    private final Map<String, HydraProxy> proxies;

    private final Hydra hydra;

    public HydraProxyManager(Hydra hydra) {
        this.hydra = hydra;
        this.proxies = new HashMap<>();

        this.hydra.getLogger().log(LogType.INFO, "Starting proxy manager...");
    }

    public void downloadMinecraftProxyImage() {
        try {
            this.hydra.getLogger().log(LogType.INFO, String.format("Pulling %s:%s image... (this might take few minutes)", this.minecraftProxyImage, this.minecraftProxyImageTag));
            this.hydra.getDocker().getDockerClient().pullImageCmd(this.minecraftProxyImage).withTag(this.minecraftProxyImageTag).exec(new PullImageResultCallback()).awaitCompletion(60, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            this.hydra.getLogger().log(LogType.ERROR, String.format("%s encountered an exception during download image: %s:%s. Cause: %s", References.HYDRA, this.minecraftProxyImage, this.minecraftProxyImageTag, e.getMessage()));
            this.hydra.shutdown();
        }
    }

    @SuppressWarnings("deprecation")
    public void startProxy(HydraProxy proxy) {
        // TODO Multi-proxy with ports
        proxy.setContainer(this.hydra.getDocker().getDockerClient()
                .createContainerCmd(this.minecraftProxyImage + ":" + this.minecraftProxyImageTag)
                .withHostConfig(new HostConfig().withAutoRemove(true))
                .withName(proxy.toString())
                .exec()
                .getId());
        this.hydra.getDocker().getDockerClient().startContainerCmd(proxy.getContainer()).exec();
        proxy.setStartedTime(System.currentTimeMillis());
        proxy.setProxyIp(this.hydra.getDocker().getDockerClient().inspectContainerCmd(proxy.getContainer()).exec().getNetworkSettings().getIpAddress());
    }

    public boolean stopProxy(HydraProxy proxy) {
        if (!proxy.getContainer().isEmpty()) {
            try {
                this.hydra.getDocker().getDockerClient().stopContainerCmd(proxy.getContainer()).exec();
                proxy.setCurrentState(ProxyState.SHUTDOWN);

                if (!this.hydra.isStopping()) this.hydra.getScheduler().schedule(() -> this.checkIfProxyHasShutdown(proxy), 1, 0, TimeUnit.MINUTES);

                return true;
            } catch (Exception e) {
                this.hydra.getLogger().log(LogType.WARN, String.format("Le proxy: %s ne s'est pas stoppé !", proxy.toString()));
            }
        }
        return false;
    }

    public void stopAllProxies() {
        this.hydra.getLogger().log(LogType.INFO, "Stopping all proxies currently running...");

        for (Map.Entry<String, HydraProxy> entry : this.proxies.entrySet()) {
            this.hydra.getScheduler().runTaskAsynchronously(() -> this.stopProxy(entry.getValue()));
        }
    }

    public void checkIfProxyHasShutdown(HydraProxy proxy) {
        final List<Container> containers = this.hydra.getDocker().getDockerClient().listContainersCmd().exec();
        for (Container container : containers) {
            if (proxy.getContainer().equals(container.getId())) {
                this.hydra.getLogger().log(LogType.ERROR,  String.format("Le proxy: %s ne s'est pas stoppé !", proxy.toString()));
                this.hydra.getLogger().log(LogType.INFO,  String.format("Tentative de kill sur le proxy: %s", proxy.toString()));
                this.hydra.getDocker().getDockerClient().killContainerCmd(proxy.getContainer()).exec();
            }
        }
        if (!this.hydra.isStopping()) this.removeProxy(proxy);
    }

    public void checkIfAllProxiesHaveShutdown() {
        this.hydra.getLogger().log(LogType.INFO, "Checking if all proxies have shutdown...");

        for (Map.Entry<String, HydraProxy> entry : this.proxies.entrySet()) {
            this.checkIfProxyHasShutdown(entry.getValue());
        }
        this.proxies.clear();
    }

    public void checkStatus(HydraProxy proxy) {
        if (!proxy.getContainer().isEmpty()) {
            final InspectContainerResponse.ContainerState containerState = this.hydra.getDocker().getDockerClient()
                    .inspectContainerCmd(proxy.getContainer())
                    .exec()
                    .getState();

            switch (containerState.getHealth().getStatus()) {
                case "starting":
                    proxy.setCurrentState(ProxyState.STARTING);
                    break;
                case "healthy":
                    proxy.setCurrentState(ProxyState.READY);
                    break;
                default:
                    proxy.setCurrentState(ProxyState.IDLE);
            }
        }
    }

    public void addProxy(HydraProxy proxy) {
        this.proxies.put(proxy.toString(), proxy);
    }

    public void removeProxy(HydraProxy proxy) {
        this.proxies.remove(proxy.toString());
    }

    public Map<String, HydraProxy> getProxies() {
        return this.proxies;
    }
}
