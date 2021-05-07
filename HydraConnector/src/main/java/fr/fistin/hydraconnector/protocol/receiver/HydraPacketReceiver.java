package fr.fistin.hydraconnector.protocol.receiver;

import fr.fistin.hydraconnector.protocol.packet.HydraPacket;

@FunctionalInterface
public interface HydraPacketReceiver {

    void receive(HydraPacket packet);

}
