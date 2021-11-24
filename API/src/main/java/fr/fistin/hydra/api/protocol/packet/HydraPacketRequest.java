package fr.fistin.hydra.api.protocol.packet;

import fr.fistin.hydra.api.HydraAPI;
import fr.fistin.hydra.api.protocol.HydraConnection;
import fr.fistin.hydra.api.protocol.HydraResponseCallback;
import fr.fistin.hydra.api.protocol.packet.model.HydraResponsePacket;

/**
 * Project: Hydra
 * Created by AstFaster
 * on 24/11/2021 at 13:10
 */
public class HydraPacketRequest {

    /** Packet to send */
    private HydraPacket packet;

    /** Channel to send the packet */
    private String channel;

    /** Callback to fire after sending the packet */
    private Runnable sendingCallback;

    /** Response callback to fire when a response of this request is received */
    private HydraResponseCallback responseCallback;

    /** {@link HydraAPI} instance */
    private final HydraAPI hydraAPI;

    /**
     * Constructor of {@link HydraPacketRequest}
     *
     * @param hydraAPI {@link HydraAPI} instance
     */
    public HydraPacketRequest(HydraAPI hydraAPI) {
        this.hydraAPI = hydraAPI;
    }

    /**
     * Set the request's packet
     *
     * @param packet Packet to send
     * @return {@link HydraPacketRequest} instance
     */
    public HydraPacketRequest withPacket(HydraPacket packet) {
        this.packet = packet;
        return this;
    }

    /**
     * Set the request's channel
     *
     * @param channel Channel to send the packet
     * @return {@link HydraPacketRequest} instance
     */
    public HydraPacketRequest withChannel(String channel) {
        this.channel = channel;
        return this;
    }

    /**
     * Set the request's callback
     *
     * @param sendingCallback Callback fired after sending the packet
     * @return {@link HydraPacketRequest} instance
     */
    public HydraPacketRequest withSendingCallback(Runnable sendingCallback) {
        this.sendingCallback = sendingCallback;
        return this;
    }

    /**
     * Set the request's response callback
     *
     * @param responseCallback Callback fired after received request response
     * @return {@link HydraPacketRequest} instance
     */
    public HydraPacketRequest withResponseCallback(HydraResponseCallback responseCallback) {
        this.responseCallback = responseCallback;
        return this;
    }

    /**
     * Execute the request by sending the packet and registering response callback
     */
    public void exec() {
        final HydraConnection connection = this.hydraAPI.getConnection();

        this.hydraAPI.getPubSub().send(this.channel, connection.encode(packet), this.sendingCallback);

        connection.registerReceiver(this.channel, (channel, packet) -> {
            if (packet instanceof HydraResponsePacket) {
                final HydraResponsePacket responsePacket = (HydraResponsePacket) packet;

                if (packet.getUniqueId().equals(this.packet.getUniqueId())) {
                    this.responseCallback.call(responsePacket.getResponse(), responsePacket.getMessage());
                }
            }
        });
    }

}
