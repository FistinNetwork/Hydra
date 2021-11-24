package fr.fistin.hydra.api.protocol.packet.model;

import fr.fistin.hydra.api.protocol.packet.HydraPacket;

/**
 * Project: Hydra
 * Created by AstFaster
 * on 24/11/2021 at 18:16
 */
public class HydraHeartbeatPacket extends HydraPacket {

    /** Identifier. Example: lobby-1vfd5 or proxy-vxc44 */
    private final String id;

    /**
     * Constructor of {@link HydraHeartbeatPacket}
     *
     * @param id Identifier
     */
    public HydraHeartbeatPacket(String id) {
        this.id = id;
    }

    /**
     * Get identifier
     *
     * @return Identifier
     */
    public String getId() {
        return this.id;
    }

}
