package fr.fistin.hydra.api.protocol.packet.model.server;

import fr.fistin.hydra.api.protocol.packet.HydraPacket;

/**
 * Project: Hydra
 * Created by AstFaster
 * on 24/11/2021 at 16:28
 */
public class HydraStopServerPacket extends HydraPacket {

    /** Identifier of the server to stop */
    private final String serverId;

    /**
     * Constructor of {@link HydraStopServerPacket}
     *
     * @param serverId Server's id
     */
    public HydraStopServerPacket(String serverId) {
        this.serverId = serverId;
    }

    /**
     * Get server's id
     *
     * @return An id
     */
    public String getServerId() {
        return this.serverId;
    }

}
