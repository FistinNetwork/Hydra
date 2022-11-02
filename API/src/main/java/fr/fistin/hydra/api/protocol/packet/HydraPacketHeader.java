package fr.fistin.hydra.api.protocol.packet;

import fr.fistin.hydra.api.HydraAPI;

/**
 * Created by AstFaster
 * on 01/11/2022 at 15:00
 */
public class HydraPacketHeader {

    /** The identifier of the packet sent. */
    private final int packetId;
    /** The type of the packet sender */
    private final HydraAPI.Type senderType;
    /** The sender of the packet */
    private final String sender;

    /**
     * Default constructor of {@link HydraPacketHeader}
     *
     * @param packetId The id of the packet
     * @param senderType The type of the sender
     * @param sender The sender
     */
    public HydraPacketHeader(int packetId, HydraAPI.Type senderType, String sender) {
        this.packetId = packetId;
        this.senderType = senderType;
        this.sender = sender;
    }

    /**
     * Get the identifier of the packet
     *
     * @return An identifier
     */
    public int getPacketId() {
        return this.packetId;
    }

    /**
     * Get the type of the sender
     *
     * @return A type
     */
    public HydraAPI.Type getSenderType() {
        return this.senderType;
    }

    /**
     * Get the sender of the packet
     *
     * @return A sender
     */
    public String getSender() {
        return this.sender;
    }

}
