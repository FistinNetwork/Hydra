package fr.fistin.hydra.api.server.packet;

import fr.fistin.hydra.api.protocol.packet.HydraPacket;
import fr.fistin.hydra.api.server.HydraServerCreationInfo;

/**
 * Project: Hydra
 * Created by AstFaster
 * on 24/11/2021 at 16:28
 *
 * Packet used to ask Hydra to start a new server with given information.
 */
public class HydraStartServerPacket extends HydraPacket {

    /** The information of the server to create */
    private final HydraServerCreationInfo serverInfo;

    /**
     * Default constructor of a {@link HydraStartServerPacket}
     *
     * @param serverInfo The information of the server to create
     */
    public HydraStartServerPacket(HydraServerCreationInfo serverInfo) {
        this.serverInfo = serverInfo;
    }

    /**
     * Get the {@linkplain HydraServerCreationInfo information} of the server to create
     *
     * @return A {@link HydraServerCreationInfo} object
     */
    public HydraServerCreationInfo getServerInfo() {
        return this.serverInfo;
    }

}
