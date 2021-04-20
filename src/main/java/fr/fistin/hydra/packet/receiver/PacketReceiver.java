package fr.fistin.hydra.packet.receiver;

import fr.fistin.hydra.packet.HydraPacket;

@FunctionalInterface
public interface PacketReceiver {

    void receive(HydraPacket packet);

}
