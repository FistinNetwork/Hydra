package fr.fistin.hydra.heartbeat;

import fr.fistin.hydra.Hydra;
import fr.fistin.hydra.api.HydraAPI;
import fr.fistin.hydra.api.proxy.HydraProxy;
import fr.fistin.hydra.api.server.HydraServer;

import java.util.concurrent.TimeUnit;

/**
 * Created by AstFaster
 * on 02/11/2022 at 12:35
 */
public class HydraHeartbeatsChecker {

    public static final long IDLE_TIME = 20 * 1000;
    public static final long MAX_IDLE_TIME = 30 * 1000;

    public void start() {
        Hydra.get().getAPI().getExecutorService().scheduleAtFixedRate(this::check, 0, 10, TimeUnit.SECONDS);
    }

    private void check() {
        final HydraAPI hydraAPI = Hydra.get().getAPI();

        for (HydraServer server : hydraAPI.getServersService().getServers()) {
            if (server.getState() == HydraServer.State.CREATING) {
                continue;
            }

            final String serverName = server.getName();
            final long elapsedTime = System.currentTimeMillis() - server.getLastHeartbeat();

            if (elapsedTime >= MAX_IDLE_TIME) {
                System.out.println("'" + serverName + "' didn't send a heartbeat for more than 30s! Stopping it...");

                Hydra.get().getServerManager().stopServer(serverName);
            } else if (elapsedTime >= IDLE_TIME) {
                server.setState(HydraServer.State.IDLE);

                Hydra.get().getServerManager().updateServer(server);
            }
        }

        for (HydraProxy proxy : hydraAPI.getProxiesService().getProxies()) {
            if (proxy.getState() == HydraProxy.State.CREATING) {
                continue;
            }

            final String proxyName = proxy.getName();
            final long elapsedTime = System.currentTimeMillis() - proxy.getLastHeartbeat();

            if (elapsedTime >= MAX_IDLE_TIME) {
                System.out.println("'" + proxyName + "' didn't send a heartbeat for more than 30s! Stopping it...");

                Hydra.get().getProxyManager().stopProxy(proxyName);
            } else if (elapsedTime >= IDLE_TIME) {
                proxy.setState(HydraProxy.State.IDLE);

                Hydra.get().getProxyManager().updateProxy(proxy);
            }
        }
    }

}
