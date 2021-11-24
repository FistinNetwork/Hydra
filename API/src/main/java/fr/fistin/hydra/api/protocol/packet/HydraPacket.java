package fr.fistin.hydra.api.protocol.packet;

import java.util.UUID;

/**
 * Project: Hydra
 * Created by AstFaster
 * on 23/11/2021 at 07:14
 */
public abstract class HydraPacket {

    private final UUID uniqueId;

    public HydraPacket() {
        this.uniqueId = UUID.randomUUID();
    }

    public UUID getUniqueId() {
        return this.uniqueId;
    }

}
