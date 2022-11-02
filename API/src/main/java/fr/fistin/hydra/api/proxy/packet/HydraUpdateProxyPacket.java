package fr.fistin.hydra.api.proxy.packet;

import fr.fistin.hydra.api.protocol.packet.HydraPacket;
import fr.fistin.hydra.api.proxy.HydraProxy;

/**
 * Created by AstFaster
 * on 02/11/2022 at 09:22
 *
 * Packet used to update all proxy information in cache by asking Hydra.<br>
 * Warning: Only the concerned proxy can update its information!
 */
public class HydraUpdateProxyPacket extends HydraPacket {

    /** The proxy to update */
    private final HydraProxy proxy;

    /**
     * Default constructor of a {@link HydraUpdateProxyPacket}
     *
     * @param proxy The proxy to update
     */
    public HydraUpdateProxyPacket(HydraProxy proxy) {
        this.proxy = proxy;
    }

    /**
     * Get the proxy to update.
     *
     * @return The {@link HydraProxy} object
     */
    public HydraProxy getProxy() {
        return this.proxy;
    }

}
