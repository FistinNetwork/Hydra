package fr.fistin.hydra.api.protocol;

import fr.fistin.hydra.api.HydraAPI;
import fr.fistin.hydra.api.protocol.packet.HydraPacket;
import fr.fistin.hydra.api.protocol.packet.HydraPacketRequest;
import fr.fistin.hydra.api.protocol.packet.model.HydraResponsePacket;
import fr.fistin.hydra.api.protocol.receiver.IHydraPacketReceiver;
import fr.fistin.hydra.api.protocol.response.HydraResponse;
import fr.fistin.hydra.api.protocol.response.HydraResponseType;
import fr.fistin.hydra.api.redis.receiver.IHydraChannelReceiver;

import java.util.HashMap;
import java.util.Map;

/**
 * Project: Hydra
 * Created by AstFaster
 * on 23/11/2021 at 07:15
 */
public class HydraConnection {

    /** {@link IHydraPacketReceiver} of each {@link IHydraChannelReceiver} */
    private final Map<IHydraPacketReceiver, IHydraChannelReceiver> packetReceivers;

    /** {@link HydraAPI} instance */
    private final HydraAPI hydraAPI;

    /**
     * Constructor of {@link HydraConnection}
     *
     * @param hydraAPI {@link HydraAPI} instance
     */
    public HydraConnection(HydraAPI hydraAPI) {
        this.hydraAPI = hydraAPI;
        this.packetReceivers = new HashMap<>();
    }

    /**
     * Register a packet receiver on a given channel to listen for incoming packet
     *
     * @param channel Channel to listen
     * @param packetReceiver Receiver to subscribe
     */
    public void registerReceiver(String channel, IHydraPacketReceiver packetReceiver) {
        if (!this.packetReceivers.containsKey(packetReceiver)) {
            final IHydraChannelReceiver receiver = (ch, message) -> {
                final HydraPacket packet = this.decode(message);

                if (packet != null) {
                    final HydraResponse response = packetReceiver.receive(channel, packet);

                    if (response != null) {
                        final HydraResponseType type = response.getType();

                        if (type != HydraResponseType.NONE) {
                            final HydraResponsePacket responsePacket = new HydraResponsePacket(packet.getUniqueId(), type, response.getMessage());

                            this.sendPacket(channel, responsePacket).exec();
                        }
                    }
                }
            };

            this.packetReceivers.put(packetReceiver, receiver);
            this.hydraAPI.getPubSub().subscribe(channel, receiver);
        }
    }

    /**
     * Unregister a packet receiver from a channel
     *
     * @param channel Receiver's channel
     * @param packetReceiver Packet receiver to unregister
     */
    public void unregisterReceiver(String channel, IHydraPacketReceiver packetReceiver) {
        this.hydraAPI.getPubSub().unsubscribe(channel, this.packetReceivers.remove(packetReceiver));
    }

    /**
     * Send a packet on a given channel
     *
     * @param channel Channel
     * @param packet Packet to send
     */
    public HydraPacketRequest sendPacket(String channel, HydraPacket packet) {
        return new HydraPacketRequest(this.hydraAPI).withChannel(channel).withPacket(packet);
    }

    /**
     * Encode a packet to a string
     *
     * @param packet Packet to encode
     * @return Encoded packet
     */
    public String encode(HydraPacket packet) {
        return this.hydraAPI.getProvider().getPacketEncoder().encode(packet);
    }

    /**
     * Decode a message to a packet
     *
     * @param message Message to decode
     * @return Decoded packet
     */
    public HydraPacket decode(String message) {
        return this.hydraAPI.getProvider().getPacketDecoder().decode(message);
    }

}
