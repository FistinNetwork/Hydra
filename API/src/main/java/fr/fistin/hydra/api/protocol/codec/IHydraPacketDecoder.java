package fr.fistin.hydra.api.protocol.codec;

import fr.fistin.hydra.api.protocol.packet.HydraPacket;

/**
 * Project: Hydra
 * Created by AstFaster
 * on 23/11/2021 at 07:15
 */
public interface IHydraPacketDecoder {

    /**
     * Decode a given message to a packet
     *
     * @param message Message to decode
     * @return Decoded {@link HydraPacket}
     */
    HydraPacket decode(String message);

}
