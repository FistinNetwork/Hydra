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
public class HydraPacketDecoder implements IHydraPacketDecoder {

    /**
     * Deserialize packet from json and return it decoded in Base64
     *
     * @param message Message to decode
     * @return Decoded packet
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

}
