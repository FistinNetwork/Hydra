package fr.fistin.hydra.api.proxy.packet;

import fr.fistin.hydra.api.protocol.packet.HydraPacket;

/**
 * Project: Hydra
 * Created by AstFaster
 * on 24/11/2021 at 16:31
 */
public class HydraStopProxyPacket extends HydraPacket {

    /** Name  of the proxy to stop */
    private final String proxyName;

    /**
     * Constructor of {@link HydraStopProxyPacket}
     *
     * @param proxyName Proxy's name
     */
    public HydraStopProxyPacket(String proxyName) {
        this.proxyName = proxyName;
    }

    /**
     * Get proxy's name
     *
     * @return A name
     */
    public String getProxyName() {
        return this.proxyName;
    }

}
