package fr.fistin.hydra.api.protocol.codec;

import fr.fistin.hydra.api.HydraAPI;
import fr.fistin.hydra.api.protocol.HydraProtocol;
import fr.fistin.hydra.api.protocol.packet.HydraPacket;

import java.util.Base64;

/**
 * Project: Hydra
 * Created by AstFaster
 * on 23/11/2021 at 07:15
 */
public class HydraPacketEncoder implements IHydraPacketEncoder {

    /**
     * Serialize packet to json and return it encoded in Base64
     *
     * @param packet Packet to encode
     * @return Encoded packet
     */
    @Override
    public String encode(HydraPacket packet) {
        final Base64.Encoder encoder = Base64.getEncoder();

        return HydraProtocol.getPacketIdByClass(packet.getClass()) + HydraProtocol.SPLIT_CHAR + encoder.encodeToString(HydraAPI.GSON.toJson(packet).getBytes());
    }

}
