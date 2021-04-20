package fr.fistin.hydra.packet.codec;

import fr.fistin.hydra.packet.HydraPacket;

public interface IPacketEncoder {

    String encode(HydraPacket packet);

}
