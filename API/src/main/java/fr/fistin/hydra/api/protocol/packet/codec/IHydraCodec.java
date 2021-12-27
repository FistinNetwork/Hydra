package fr.fistin.hydra.api.protocol.packet.codec;

import fr.fistin.hydra.api.protocol.packet.HydraPacket;

/**
 * Project: Hydra
 * Created by AstFaster
 * on 27/12/2021 at 09:02
 */
public interface IHydraCodec {

    /**
     * Decode a given message to a packet
     *
     * @param message Message to decode
     * @return Decoded {@link HydraPacket}
     */
    HydraPacket decode(String message);

    /**
     * Encode a given packet to a string
     *
     * @param packet Packet to encode
     * @return Encoded {@link HydraPacket}
     */
    String encode(HydraPacket packet);

}
