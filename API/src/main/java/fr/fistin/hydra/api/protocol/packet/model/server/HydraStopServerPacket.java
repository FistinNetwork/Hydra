package fr.fistin.hydra.api.protocol.packet.model.server;

import fr.fistin.hydra.api.protocol.packet.HydraPacket;

/**
 * Project: Hydra
 * Created by AstFaster
 * on 24/11/2021 at 16:28
 */
public class HydraStopServerPacket extends HydraPacket {

    /** Name of the server to stop */
    private final String serverName;

    /**
     * Constructor of {@link HydraStopServerPacket}
     *
     * @param serverName Server's name
     */
    public HydraStopServerPacket(String serverName) {
        this.serverName = serverName;
    }

    /**
     * Get server's name
     *
     * @return A name
     */
    public String getServerName() {
        return this.serverName;
    }

}
