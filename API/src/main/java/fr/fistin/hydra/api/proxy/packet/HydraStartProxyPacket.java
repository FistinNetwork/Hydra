package fr.fistin.hydra.api.proxy.packet;

import fr.fistin.hydra.api.protocol.packet.HydraPacket;
import fr.fistin.hydra.api.proxy.HydraProxyCreationInfo;

/**
 * Project: Hydra
 * Created by AstFaster
 * on 24/11/2021 at 16:30
 *
 * Packet used to start a new proxy with given information.
 */
public class HydraStartProxyPacket extends HydraPacket {

    /** The information of the proxy to create */
    private final HydraProxyCreationInfo proxyInfo;

    /**
     * Constructor of a {@link HydraStartProxyPacket}
     *
     * @param proxyInfo The information of the proxy to create
     */
    public HydraStartProxyPacket(HydraProxyCreationInfo proxyInfo) {
        this.proxyInfo = proxyInfo;
    }

    /**
     * Get the information of the proxy to create
     *
     * @return A {@link HydraProxyCreationInfo} object
     */
    public HydraProxyCreationInfo getProxyInfo() {
        return this.proxyInfo;
    }

}
