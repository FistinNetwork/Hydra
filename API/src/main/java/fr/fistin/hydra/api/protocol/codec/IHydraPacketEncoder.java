package fr.fistin.hydra.api.protocol.codec;

import fr.fistin.hydra.api.protocol.packet.HydraPacket;

/**
 * Project: Hydra
 * Created by AstFaster
 * on 23/11/2021 at 07:15
 */
public interface IHydraPacketEncoder {

    /**
     * Encode a given packet to a string
     *
     * @param packet Packet to encode
     * @return Encoded {@link HydraPacket}
     */
    String encode(HydraPacket packet);

}
