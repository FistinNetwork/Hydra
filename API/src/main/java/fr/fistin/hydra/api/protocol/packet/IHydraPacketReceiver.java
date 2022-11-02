package fr.fistin.hydra.api.protocol.packet;

import fr.fistin.hydra.api.protocol.HydraChannel;
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
     * @param channel The channel where the paket has been received
     * @param header The header of the received packet
     * @param packet The received packet
     * @return The response to send back
     */
    HydraResponse receive(HydraChannel channel, HydraPacketHeader header, HydraPacket packet);

}
