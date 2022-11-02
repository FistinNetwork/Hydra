package fr.fistin.hydra.api.server.packet;

import fr.fistin.hydra.api.protocol.packet.HydraPacket;
import fr.fistin.hydra.api.server.HydraServer;

/**
 * Created by AstFaster
 * on 02/11/2022 at 09:22
 *
 * Packet used to update all server information in cache by asking Hydra.<br>
 * Warning: Only the concerned server can update its information!
 */
public class HydraUpdateServerPacket extends HydraPacket {

    /** The server to update */
    private final HydraServer server;

    /**
     * Default constructor of a {@link HydraUpdateServerPacket}
     *
     * @param server The server to update
     */
    public HydraUpdateServerPacket(HydraServer server) {
        this.server = server;
    }

    /**
     * Get the server to update.
     *
     * @return The {@link HydraServer} object
     */
    public HydraServer getServer() {
        return this.server;
    }

}
