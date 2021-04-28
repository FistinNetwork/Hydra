package fr.fistin.hydra.packet.codec;

import fr.fistin.hydra.Hydra;
import fr.fistin.hydra.packet.HydraPacket;
import fr.fistin.hydra.util.References;

import java.util.Base64;

public class PacketDecoder implements IPacketDecoder {

    private final Hydra hydra;

    public PacketDecoder(Hydra hydra) {
        this.hydra = hydra;
    }

    @Override
    public HydraPacket decode(String message) {
        final Base64.Decoder decoder = Base64.getDecoder();
        final String json = new String(decoder.decode(message));
        final Class<? extends HydraPacket> packetClass = this.hydra.getPacketManager().getPacketClassByType(References.GSON.fromJson(json, HydraPacket.class).getType());

        if (packetClass != null) {
            return References.GSON.fromJson(json, packetClass);
        }

        return null;
    }

}
