package fr.fistin.hydra.api.server.packet;

import fr.fistin.hydra.api.protocol.packet.HydraPacket;

/**
 * Project: Hydra
 * Created by AstFaster
 * on 24/11/2021 at 16:28
 *
 * Packet used to stop a server by querying Hydra.
 */
public class HydraStopServerPacket extends HydraPacket {

    /** The name of the server to stop */
    private final String serverName;

    /**
     * Constructor of a {@link HydraStopServerPacket}
     *
     * @param serverName The name of the server
     */
    public HydraStopServerPacket(String serverName) {
        this.serverName = serverName;
    }

    /**
     * Get the name of the server to stop
     *
     * @return A server's name
     */
    public String getServerName() {
        return this.serverName;
    }

}
