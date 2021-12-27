package fr.fistin.hydra.api.protocol.packet.codec;

import fr.fistin.hydra.api.HydraAPI;
import fr.fistin.hydra.api.protocol.HydraProtocol;
import fr.fistin.hydra.api.protocol.packet.HydraPacket;

import java.util.Base64;

/**
 * Project: Hydra
 * Created by AstFaster
 * on 27/12/2021 at 09:02
 */
public class HydraCodec implements IHydraCodec {

    /**
     * Decode a given message to a packet object<br>
     * This default decoder decode the message from base64
     *
     * @param message Message to decode
     * @return Decoded {@link HydraPacket}
     */
    @Override
    public HydraPacket decode(String message) {
        try {
            final Base64.Decoder decoder = Base64.getDecoder();
            final String[] splitedRaw  = message.split(HydraProtocol.SPLIT_CHAR);
            final int id = Integer.parseInt(splitedRaw[0]);
            final String json = new String(decoder.decode(splitedRaw[1]));

            return HydraAPI.GSON.fromJson(json, HydraProtocol.getPacketClassById(id));
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Encode a given packet to a string<br>
     * This default encoder encode the message to base64
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
