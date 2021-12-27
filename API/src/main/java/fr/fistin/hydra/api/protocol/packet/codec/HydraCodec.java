package fr.fistin.hydra.api.protocol.packet.codec;

import fr.fistin.hydra.api.HydraAPI;
import fr.fistin.hydra.api.protocol.HydraProtocol;
import fr.fistin.hydra.api.protocol.packet.HydraPacket;

import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Project: Hydra
 * Created by AstFaster
 * on 27/12/2021 at 09:02
 */
public class HydraCodec implements IHydraCodec {

    /** {@link HydraAPI} instance */
    private final HydraAPI hydraAPI;

    /**
     * Constructor of {@link HydraCodec}
     *
     * @param hydraAPI {@link HydraAPI} instance
     */
    public HydraCodec(HydraAPI hydraAPI) {
        this.hydraAPI = hydraAPI;
    }

    /**
     * Decode a given message to a packet object<br>
     * This default decoder decode the message from base64 and check also if it's a jws
     *
     * @param message Message to decode
     * @return Decoded {@link HydraPacket}
     */
    @Override
    public HydraPacket decode(String message) {
        try {
            final String pattern = "^[A-Za-z0-9-_=]+\\.[A-Za-z0-9-_=]+\\.[A-Za-z0-9-_.+/=]*$";
            final Matcher matcher = Pattern.compile(pattern).matcher(message);

            if (matcher.find()) {
                final String content = this.hydraAPI.getJWTs().jwsToContent(message);

                if (content != null) {
                    message = content;
                } else {
                    return null;
                }
            } else {
                if (this.hydraAPI.getType() != HydraAPI.Type.SERVER) {
                    return null;
                }
            }

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
     * This default encoder encode the message to base64 and also check if the encoded packet need to be converted to jws
     *
     * @param packet Packet to encode
     * @return Encoded packet
     */
    @Override
    public String encode(HydraPacket packet) {
        final Base64.Encoder encoder = Base64.getEncoder();
        final String encodedPacket = HydraProtocol.getPacketIdByClass(packet.getClass()) + HydraProtocol.SPLIT_CHAR + encoder.encodeToString(HydraAPI.GSON.toJson(packet).getBytes());

        if (this.hydraAPI.getType() == HydraAPI.Type.SERVER && this.hydraAPI.getPrivateKey() != null) {
            return this.hydraAPI.getJWTs().contentToJWS(encodedPacket);
        }

        return encodedPacket;
    }
}
