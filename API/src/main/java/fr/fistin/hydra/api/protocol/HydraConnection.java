package fr.fistin.hydra.api.protocol;

import fr.fistin.hydra.api.HydraAPI;
import fr.fistin.hydra.api.protocol.packet.HydraPacket;
import fr.fistin.hydra.api.protocol.packet.HydraPacketRequest;
import fr.fistin.hydra.api.protocol.packet.IHydraPacketReceiver;
import fr.fistin.hydra.api.protocol.packet.codec.HydraCodec;
import fr.fistin.hydra.api.protocol.response.HydraResponse;
import fr.fistin.hydra.api.protocol.response.HydraResponsePacket;
import fr.fistin.hydra.api.protocol.response.HydraResponseType;
import fr.fistin.hydra.api.redis.IHydraReceiver;

import java.util.HashMap;
import java.util.Map;

/**
 * Project: Hydra
 * Created by AstFaster
 * on 23/11/2021 at 07:15
 */
public class HydraConnection {

    /** {@link IHydraPacketReceiver} of each {@link IHydraReceiver} */
    private final Map<IHydraPacketReceiver, IHydraReceiver> packetReceivers;

    /** The packets encoder and decoder instance */
    private final HydraCodec codec;
    /** {@link HydraAPI} instance */
    private final HydraAPI hydraAPI;

    /**
     * Constructor of {@link HydraConnection}
     *
     * @param hydraAPI {@link HydraAPI} instance
     */
    public HydraConnection(HydraAPI hydraAPI) {
        this.hydraAPI = hydraAPI;
        this.codec = new HydraCodec(this.hydraAPI);
        this.packetReceivers = new HashMap<>();
    }

    /**
     * Register a packet receiver on a given channel to listen for incoming packet
     *
     * @param channel Channel to listen
     * @param packetReceiver Receiver to subscribe
     */
    public void registerReceiver(HydraChannel channel, IHydraPacketReceiver packetReceiver) {
        if (!this.packetReceivers.containsKey(packetReceiver)) {
            final IHydraReceiver receiver = (ch, message) -> {
                final HydraCodec.DecodingResult decodingResult = this.codec.decode(message);

                if (decodingResult != null) {
                    final HydraPacket packet = decodingResult.getPacket();
                    final HydraResponse response = packetReceiver.receive(channel, decodingResult.getPacketHeader(), packet);

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
            this.hydraAPI.getPubSub().subscribe(channel.getName(), receiver);
        }
    }

    /**
     * Unregister a packet receiver from a channel
     *
     * @param channel Receiver's channel
     * @param packetReceiver Packet receiver to unregister
     */
    public void unregisterReceiver(HydraChannel channel, IHydraPacketReceiver packetReceiver) {
        this.hydraAPI.getPubSub().unsubscribe(channel.getName(), this.packetReceivers.remove(packetReceiver));
    }

    /**
     * Send a packet on a given channel
     *
     * @param channel Channel
     * @param packet Packet to send
     * @return The created {@linkplain HydraPacketRequest packet request}
     */
    public HydraPacketRequest sendPacket(HydraChannel channel, HydraPacket packet) {
        return new HydraPacketRequest(this.hydraAPI)
                .withChannel(channel)
                .withPacket(packet);
    }

    /**
     * Get the coded used to encode and decode packets
     *
     * @return The {@link HydraCodec} instance
     */
    public HydraCodec getCodec() {
        return this.codec;
    }

}
