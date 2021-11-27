package fr.fistin.hydra.api.protocol;

import fr.fistin.hydra.api.HydraAPI;
import fr.fistin.hydra.api.protocol.packet.HydraPacket;
import fr.fistin.hydra.api.protocol.packet.HydraPacketRequest;
import fr.fistin.hydra.api.protocol.packet.model.HydraResponsePacket;
import fr.fistin.hydra.api.protocol.receiver.IHydraPacketReceiver;
import fr.fistin.hydra.api.util.Pair;

/**
 * Project: Hydra
 * Created by AstFaster
 * on 23/11/2021 at 07:15
 */
public class HydraConnection {

    /** {@link HydraAPI} instance */
    private final HydraAPI hydraAPI;

    /**
     * Constructor of {@link HydraConnection}
     *
     * @param hydraAPI {@link HydraAPI} instance
     */
    public HydraConnection(HydraAPI hydraAPI) {
        this.hydraAPI = hydraAPI;
    }

    /**
     * Register a packet receiver on a given channel to listen for incoming packet
     *
     * @param channel Channel to listen
     * @param receiver Receiver to subscribe
     */
    public void registerReceiver(String channel, IHydraPacketReceiver receiver) {
        this.hydraAPI.getPubSub().subscribe(channel, (ch, message) -> {
            final HydraPacket packet = this.decode(message);

            if (packet != null) {
                final Pair<HydraResponse, String> pair = receiver.receive(channel, packet);
                final HydraResponse response = pair.getFirst();

                if (response != HydraResponse.NONE) {
                    final HydraResponsePacket responsePacket = new HydraResponsePacket(packet.getUniqueId(), response, pair.getSecond());

                    this.sendPacket(channel, responsePacket).exec();
                }
            }
        });
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
