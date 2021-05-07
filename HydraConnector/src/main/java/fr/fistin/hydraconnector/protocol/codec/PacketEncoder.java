package fr.fistin.hydraconnector.protocol.codec;

import fr.fistin.hydraconnector.HydraConnector;
import fr.fistin.hydraconnector.protocol.HydraProtocol;
import fr.fistin.hydraconnector.protocol.packet.HydraPacket;

import java.util.Base64;

public class PacketEncoder implements IPacketEncoder {

    private final HydraConnector hydraConnector;

    public PacketEncoder(HydraConnector hydraConnector) {
        this.hydraConnector = hydraConnector;
    }

    @Override
    public String encode(HydraPacket packet) {
        final Base64.Encoder encoder = Base64.getEncoder();

        return HydraProtocol.getPacketIdByClass(packet.getClass()) + HydraProtocol.SPLIT_CHAR + encoder.encodeToString(this.hydraConnector.getGson().toJson(packet).getBytes());
    }

}
