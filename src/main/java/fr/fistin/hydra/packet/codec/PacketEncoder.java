package fr.fistin.hydra.packet.codec;

import fr.fistin.hydra.packet.HydraPacket;

import java.util.Base64;

public class PacketEncoder implements IPacketEncoder {

    @Override
    public String encode(HydraPacket packet) {
        final Base64.Encoder encoder = Base64.getEncoder();

        return encoder.encodeToString(packet.toJson().getBytes());
    }

}
