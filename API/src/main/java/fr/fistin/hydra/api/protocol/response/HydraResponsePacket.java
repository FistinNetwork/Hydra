package fr.fistin.hydra.api.protocol.response;

import fr.fistin.hydra.api.protocol.packet.HydraPacket;

import java.util.UUID;

/**
 * Project: Hydra
 * Created by AstFaster
 * on 24/11/2021 at 13:17
 */
public class HydraResponsePacket extends HydraPacket {

    /** Responded packet's unique id */
    private final UUID respondedPacketUniqueId;

    /** Response's type */
    private final HydraResponseType response;

    /** Response's message */
    private final String message;

    /**
     * Constructor of {@link HydraResponsePacket}
     *
     * @param respondedPacketUniqueId Responded packet's unique id
     * @param response Response's type
     * @param message Response's message
     */
    public HydraResponsePacket(UUID respondedPacketUniqueId, HydraResponseType response, String message) {
        this.respondedPacketUniqueId = respondedPacketUniqueId;
        this.response = response;
        this.message = message;
    }

    /**
     * Get responded packet unique id
     *
     * @return {@link UUID} object
     */
    public UUID getRespondedPacketUniqueId() {
        return this.respondedPacketUniqueId;
    }

    /**
     * Get response's type
     *
     * @return {@link HydraResponseType} object
     */
    public HydraResponseType getResponse() {
        return this.response;
    }

    /**
     * Get response's message
     *
     * @return A message
     */
    public String getMessage() {
        return this.message;
    }

}
