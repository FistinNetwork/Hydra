package fr.fistin.hydra.api.protocol.packet;

import fr.fistin.hydra.api.HydraAPI;
import fr.fistin.hydra.api.protocol.HydraChannel;
import fr.fistin.hydra.api.protocol.HydraConnection;
import fr.fistin.hydra.api.protocol.response.HydraResponse;
import fr.fistin.hydra.api.protocol.response.HydraResponseCallback;
import fr.fistin.hydra.api.protocol.response.HydraResponsePacket;
import fr.fistin.hydra.api.protocol.response.HydraResponseType;

import java.util.concurrent.TimeUnit;

/**
 * Project: Hydra
 * Created by AstFaster
 * on 24/11/2021 at 13:10
 */
public class HydraPacketRequest {

    /** Packet to send */
    private HydraPacket packet;
    /** Channel to send the packet */
    private HydraChannel channel;
    /** Response callback to fire when a response of this request is received */
    private HydraResponseCallback responseCallback;
    /** The maximum of time to wait for responses (in millis) */
    private long timeout = 5000;

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
     * Get request's packet
     *
     * @return {@link HydraPacket} object
     */
    public HydraPacket getPacket() {
        return this.packet;
    }

    /**
     * Set the request's channel
     *
     * @param channel The channel to send the packet
     * @return {@link HydraPacketRequest} instance
     */
    public HydraPacketRequest withChannel(HydraChannel channel) {
        this.channel = channel;
        return this;
    }

    /**
     * Get the channel used for the request
     *
     * @return The request's channel
     */
    public HydraChannel getChannel() {
        return this.channel;
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
     * Get the callback fired each time that a response is received
     *
     * @return {@link HydraResponseCallback} object
     */
    public HydraResponseCallback getResponseCallback() {
        return this.responseCallback;
    }

    /**
     * Set the timeout to stop handling responses
     *
     * @param timeout The timeout value
     * @return This {@link HydraPacketRequest} object
     */
    public HydraPacketRequest withTimeout(long timeout) {
        this.timeout = timeout;
        return this;
    }

    /**
     * Set the timeout to stop handling responses
     *
     * @param timeout The timeout value
     * @param unit The unit of the timeout value
     * @return This {@link HydraPacketRequest} object
     */
    public HydraPacketRequest withTimeout(long timeout, TimeUnit unit) {
        return this.withTimeout(unit.toMillis(timeout));
    }

    /**
     * Get the timeout of responses handling
     *
     * @return A timeout (in milliseconds)
     */
    public long getTimeout() {
        return this.timeout;
    }

    /**
     * Execute the request by sending the packet and registering response callback
     */
    public void exec() {
        final HydraConnection connection = this.hydraAPI.getConnection();

        if (this.packet != null) {
            if (this.responseCallback != null) {
                connection.registerReceiver(this.channel, new ResponseReceiver());
            }

            this.hydraAPI.getPubSub().send(this.channel.getName(), connection.getCodec().encode(this.packet));
        }
    }

    private class ResponseReceiver implements IHydraPacketReceiver {

        /**
         * Constructor of {@link ResponseReceiver}
         */
        public ResponseReceiver() {
            hydraAPI.getExecutorService().schedule(() -> hydraAPI.getConnection().unregisterReceiver(channel, this), timeout, TimeUnit.MILLISECONDS);
        }

        @Override
        public HydraResponse receive(HydraChannel channel, HydraPacketHeader header, HydraPacket packet) {
            if (packet instanceof HydraResponsePacket) {
                final HydraResponsePacket responsePacket = (HydraResponsePacket) packet;

                if (responsePacket.getRespondedPacketUniqueId().equals(packet.getUniqueId())) {
                    if (responseCallback != null) {
                        responseCallback.call(new HydraResponse(responsePacket.getResponse(), responsePacket.getMessage()));
                    }
                }
            }
            return HydraResponseType.NONE.asResponse();
        }

    }

}
