package fr.fistin.hydra.api.protocol.packet.codec;

import fr.fistin.hydra.api.HydraAPI;
import fr.fistin.hydra.api.HydraException;
import fr.fistin.hydra.api.protocol.HydraProtocol;
import fr.fistin.hydra.api.protocol.packet.HydraPacket;
import fr.fistin.hydra.api.protocol.packet.HydraPacketHeader;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * Project: Hydra
 * Created by AstFaster
 * on 27/12/2021 at 09:02
 */
public class HydraCodec {

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
     * Decode a given message to a {@linkplain HydraPacket packet} object
     *
     * @param message Message to decode
     * @return Decoded {@link HydraPacket}
     */
    public DecodingResult decode(String message) {
        try {
            final Base64.Decoder decoder = Base64.getDecoder();
            final String[] splitRaw  = message.split(HydraProtocol.SPLIT_CHAR);
            final HydraPacketHeader header = HydraAPI.GSON.fromJson(new String(decoder.decode(splitRaw[0]), StandardCharsets.UTF_8), HydraPacketHeader.class);
            final HydraPacket packet = HydraAPI.GSON.fromJson(new String(decoder.decode(splitRaw[1]), StandardCharsets.UTF_8), HydraProtocol.getPacketClassById(header.getPacketId()));

            return new DecodingResult(header, packet);
        } catch (Exception e) {
            throw new HydraException("An error occurred while decoding a received message. Message: " + message + ".", e);
        }
    }

    /**
     * Encode a given packet to a {@link String}
     *
     * @param packet Packet to encode
     * @return Encoded packet
     */
    public String encode(HydraPacket packet) {
        final Base64.Encoder encoder = Base64.getEncoder();
        final int id = HydraProtocol.getPacketIdByClass(packet.getClass());

        if (id == -1) {
            throw new HydraException("Couldn't find the id of the provided packet to encode! Packet: " + packet.getClass().getName() + ".");
        }

        try {
            final HydraPacketHeader header = new HydraPacketHeader(id, this.hydraAPI.getType(), this.hydraAPI.getApplication());

            return encoder.encodeToString(HydraAPI.GSON.toJson(header).getBytes(StandardCharsets.UTF_8)) +
                   HydraProtocol.SPLIT_CHAR +
                   encoder.encodeToString(HydraAPI.GSON.toJson(packet).getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            throw new HydraException("An error occurred while encoding a packet! Packet: " + packet.getClass().getName(), e);
        }
    }

    /** The object that represents the result of decoding process */
    public static final class DecodingResult {

        /** The header of the decoded packet */
        private final HydraPacketHeader packetHeader;
        /** The decoded packet */
        private final HydraPacket packet;

        /**
         * Constructor of {@link DecodingResult} object
         *
         * @param packetHeader The header of the packet
         * @param packet The decoded packet
         */
        public DecodingResult(HydraPacketHeader packetHeader, HydraPacket packet) {
            this.packetHeader = packetHeader;
            this.packet = packet;
        }

        /**
         * Get the header of the packet
         *
         * @return The {@link HydraPacketHeader} object
         */
        public HydraPacketHeader getPacketHeader() {
            return this.packetHeader;
        }

        /**
         * Get the decoded packet
         *
         * @return The decoded {@link HydraPacket}
         */
        public HydraPacket getPacket() {
            return this.packet;
        }

    }

}
