package fr.fistin.hydraconnector.protocol.codec;

import fr.fistin.hydraconnector.protocol.packet.HydraPacket;

public interface IPacketEncoder {

    String encode(HydraPacket packet);

}
