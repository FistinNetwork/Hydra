package fr.fistin.hydraconnector.protocol.codec;

import fr.fistin.hydraconnector.protocol.packet.HydraPacket;

public interface IPacketDecoder {

    HydraPacket decode(String message);

}
