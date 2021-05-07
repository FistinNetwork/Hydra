package fr.fistin.hydraconnector.protocol.codec;

import fr.fistin.hydraconnector.HydraConnector;
import fr.fistin.hydraconnector.protocol.HydraProtocol;
import fr.fistin.hydraconnector.protocol.packet.HydraPacket;

import java.util.Base64;

public class PacketDecoder implements IPacketDecoder {

    private final HydraConnector hydraConnector;

    public PacketDecoder(HydraConnector hydraConnector) {
        this.hydraConnector = hydraConnector;
    }

    @Override
    public HydraPacket decode(String message) {
        final Base64.Decoder decoder = Base64.getDecoder();
        final String[] splitedRaw  = message.split(HydraProtocol.SPLIT_CHAR);
        final int id = Integer.parseInt(splitedRaw[0]);
        final String json = new String(decoder.decode(splitedRaw[1]));

        return this.hydraConnector.getGson().fromJson(json, HydraProtocol.getPacketClassById(id));
    }

}
