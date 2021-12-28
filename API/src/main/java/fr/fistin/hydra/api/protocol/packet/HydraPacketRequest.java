package fr.fistin.hydra.api.protocol.packet;

import fr.fistin.hydra.api.HydraAPI;
import fr.fistin.hydra.api.protocol.HydraConnection;
import fr.fistin.hydra.api.protocol.packet.model.HydraResponsePacket;
import fr.fistin.hydra.api.protocol.response.HydraResponse;
import fr.fistin.hydra.api.protocol.response.HydraResponseCallback;
import fr.fistin.hydra.api.protocol.response.HydraResponseReceiver;
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
    private String channel;
    /** Callback to fire after sending the packet */
    private Runnable sendingCallback;
    /** Response callback to fire when a response of this request is received */
    private HydraResponseCallback responseCallback;
    /** The maximum of responses to handle */
    private int maxResponses = 1;
    /** The maximum of time to wait for responses (in millis) */
    private long responsesTime = 5000;

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
     * @param channel Channel to send the packet
     * @return {@link HydraPacketRequest} instance
     */
    public HydraPacketRequest withChannel(String channel) {
        this.channel = channel;
        return this;
    }

    /**
     * Get the channel used for the request
     *
     * @return The request's channel
     */
    public String getChannel() {
        return this.channel;
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
     * Get the callback fired after executing the request
     *
     * @return A {@link Runnable}
     */
    public Runnable getSendingCallback() {
        return this.sendingCallback;
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
     * Set the maximum of responses to handle
     *
     * @param maxResponses New maximum of responses
     * @return {@link HydraPacketRequest} instance
     */
    public HydraPacketRequest withMaxResponses(int maxResponses) {
        this.maxResponses = maxResponses;
        return this;
    }

    /**
     * Get the maximum of responses the request can handle
     *
     * @return THe maximum of responses
     */
    public int getMaxResponses() {
        return this.maxResponses;
    }

    /**
     * Set the maximum of time to wait for responses
     *
     * @param responsesTime The time to wait
     * @param unit The unit of the provided time
     * @return {@link HydraPacketRequest} instance
     */
    public HydraPacketRequest withResponsesTime(long responsesTime, TimeUnit unit) {
        this.responsesTime = unit.toMillis(responsesTime);
        return this;
    }

    /**
     * Get the maximum of time to wait for all responses
     *
     * @return A time in milliseconds
     */
    public long getResponsesTime() {
        return this.responsesTime;
    }

    /**
     * Execute the request by sending the packet and registering response callback
     */
    public void exec() {
        final HydraConnection connection = this.hydraAPI.getConnection();

        if (this.packet != null) {
            if (this.responseCallback != null) {
                connection.registerReceiver(this.channel, new HydraResponseReceiver(this.hydraAPI, this));
            }

            this.hydraAPI.getPubSub().send(this.channel, this.hydraAPI.getCodec().encode(packet), this.sendingCallback);
        }
    }

}
