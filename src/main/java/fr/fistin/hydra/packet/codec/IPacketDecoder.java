package fr.fistin.hydra.packet.codec;

import fr.fistin.hydra.packet.HydraPacket;

public interface IPacketDecoder {

    HydraPacket decode(String message);

}
