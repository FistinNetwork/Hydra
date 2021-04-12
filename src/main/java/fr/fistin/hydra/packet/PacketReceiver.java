package fr.fistin.hydra.packet;

@FunctionalInterface
public interface PacketReceiver {

    void receive(HydraPacket packet);

}
