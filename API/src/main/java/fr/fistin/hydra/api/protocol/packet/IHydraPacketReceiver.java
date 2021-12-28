package fr.fistin.hydra.api.protocol.packet;

import fr.fistin.hydra.api.protocol.response.HydraResponse;

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
     * @return A response to the packet with type and message
     */
    HydraResponse receive(String channel, HydraPacket packet);

}
