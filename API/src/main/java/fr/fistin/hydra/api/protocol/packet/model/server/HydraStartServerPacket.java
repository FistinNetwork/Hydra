package fr.fistin.hydra.api.protocol.packet.model.server;

import fr.fistin.hydra.api.protocol.packet.HydraPacket;

/**
 * Project: Hydra
 * Created by AstFaster
 * on 24/11/2021 at 16:28
 */
public class HydraStartServerPacket extends HydraPacket {

    /** Type of the server to start */
    private final String serverType;

    /**
     * Constructor of {@link HydraStartServerPacket}
     *
     * @param serverType Server's type
     */
    public HydraStartServerPacket(String serverType) {
        this.serverType = serverType;
    }

    /**
     * Get server's type
     *
     * @return Type
     */
    public String getServerType() {
        return this.serverType;
    }

}
