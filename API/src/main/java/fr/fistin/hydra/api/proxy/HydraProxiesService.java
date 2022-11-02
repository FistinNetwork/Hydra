package fr.fistin.hydra.api.proxy;

import fr.fistin.hydra.api.HydraAPI;
import fr.fistin.hydra.api.HydraException;
import fr.fistin.hydra.api.protocol.HydraChannel;
import fr.fistin.hydra.api.protocol.response.HydraResponseCallback;
import fr.fistin.hydra.api.protocol.response.HydraResponseType;
import fr.fistin.hydra.api.proxy.packet.HydraStartProxyPacket;
import fr.fistin.hydra.api.proxy.packet.HydraStopProxyPacket;
import fr.fistin.hydra.api.proxy.packet.HydraUpdateProxyPacket;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Created by AstFaster
 * on 02/11/2022 at 09:56
 */
public class HydraProxiesService {

    /** The proxies Redis hash */
    public static final String HASH = HydraAPI.HYDRA_HASH + "proxies:";

    /** The {@link HydraAPI} instance */
    private final HydraAPI hydraAPI;

    /**
     * Constructor of a {@link HydraProxiesService}
     *
     * @param hydraAPI The {@link HydraAPI} instance
     */
    public HydraProxiesService(HydraAPI hydraAPI) {
        this.hydraAPI = hydraAPI;
    }

    /**
     * Create a proxy with given information by querying Hydra.
     *
     * @param proxyInfo The information of the proxy to create
     */
    public void createProxy(HydraProxyCreationInfo proxyInfo, Consumer<HydraProxy> onCreated) {
        if (proxyInfo.getData() == null) {
            throw new HydraException("Invalid proxy creation information!");
        }

        this.hydraAPI.getConnection().sendPacket(HydraChannel.QUERY, new HydraStartProxyPacket(proxyInfo))
                .withResponseCallback(response -> {
                    final HydraResponseType type = response.getType();

                    if (type == HydraResponseType.OK) {
                        final HydraProxy createProxy = response.getMessage(HydraProxy.class);

                        onCreated.accept(createProxy);
                    } else {
                        final String message = response.getMessage();

                        throw new HydraException("Couldn't create proxy! Return response: " + type + (message != null ? "(message: " + message + ")" : ""));
                    }
                })
                .exec();
    }

    /**
     * Update a proxy in cache by asking Hydra.<br>
     * Only the concerned proxy can perform this action.
     *
     * @param proxy The proxy to update
     */
    public void updateProxy(HydraProxy proxy) {
        if (!this.hydraAPI.getApplication().equals(proxy.getName())) {
            throw new HydraException("Proxies can only be updated by themself or Hydra!");
        }

        this.hydraAPI.getConnection().sendPacket(HydraChannel.PROXIES, new HydraUpdateProxyPacket(proxy)).exec();
    }

    /**
     * Stop a running proxy by querying Hydra
     *
     * @param name The name of the proxy to stop
     * @param callback The callback to trigger after the proxy has been stopped
     */
    public void stopProxy(String name, HydraResponseCallback callback) {
        this.hydraAPI.getConnection().sendPacket(HydraChannel.QUERY, new HydraStopProxyPacket(name))
                .withResponseCallback(callback)
                .exec();
    }

    /**
     * Stop a running proxy by querying Hydra
     *
     * @param name The name of the proxy to stop
     */
    public void stopProxy(String name) {
        this.stopProxy(name, null);
    }

    /**
     * Get a proxy from the Redis cache.
     *
     * @param name The name of the proxy to get
     * @return The found {@link HydraProxy}; or <code>null</code> if nothing was found
     */
    public HydraProxy getProxy(String name) {
        return this.hydraAPI.getRedis().get(jedis -> {
            final String key = new ArrayList<>(jedis.keys(HASH + "*:" + name)).get(0);
            final String json = jedis.get(key);

            return json == null ? null : HydraAPI.GSON.fromJson(json, HydraProxy.class);
        });
    }

    /**
     * Get all proxies from Redis cache.
     *
     * @return A list of {@link HydraProxy}
     */
    public List<HydraProxy> getProxies() {
        return this.hydraAPI.getRedis().get(jedis -> {
            final List<HydraProxy> proxies = new ArrayList<>();

            for (String key : jedis.keys(HASH + "*")) {
                proxies.add(HydraAPI.GSON.fromJson(jedis.get(key), HydraProxy.class));
            }
            return proxies;
        });
    }
    
}
