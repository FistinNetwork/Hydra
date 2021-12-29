package fr.fistin.hydra.api.protocol.packet;

import java.util.UUID;

/**
 * Project: Hydra
 * Created by AstFaster
 * on 23/11/2021 at 07:14
 */
public abstract class HydraPacket {

    /** The unique id of the packet */
    private final UUID uniqueId;

    /**
     * Constructor of {@link HydraPacket}
     */
    public HydraPacket() {
        this.uniqueId = UUID.randomUUID();
    }

    /**
     * Get the unique id of the packet
     *
     * @return Packet {@link UUID}
     */
    public UUID getUniqueId() {
        return this.uniqueId;
    }

}
