package fr.fistin.hydra.packet;

public interface IPacketDecoder {

    HydraPacket decode(String message);

}
