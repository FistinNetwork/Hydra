package fr.fistin.hydra.api.protocol.packet.model.proxy;

import fr.fistin.hydra.api.protocol.packet.HydraPacket;

/**
 * Project: Hydra
 * Created by AstFaster
 * on 24/11/2021 at 16:31
 */
public class HydraStopProxyPacket extends HydraPacket {

    /** Identifier of the proxy to stop */
    private final String proxyId;

    /**
     * Constructor of {@link HydraStopProxyPacket}
     *
     * @param proxyId Proxy's id
     */
    public HydraStopProxyPacket(String proxyId) {
        this.proxyId = proxyId;
    }

    /**
     * Get proxy's id
     *
     * @return An id
     */
    public String getProxyId() {
        return this.proxyId;
    }

}
