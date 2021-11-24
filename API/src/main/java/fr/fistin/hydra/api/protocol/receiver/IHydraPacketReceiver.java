package fr.fistin.hydra.api.protocol.receiver;

import fr.fistin.hydra.api.protocol.packet.HydraPacket;

/**
 * Project: Hydra
 * Created by AstFaster
 * on 23/11/2021 at 07:21
 */
public interface IHydraPacketReceiver {

    /**
     * Called when a packet is received on Hydra connection
     *
     * @param channel - Received message channel
     * @param packet - Received packet
     */
    void receive(String channel, HydraPacket packet);

}
