package fr.fistin.hydra.api.protocol.response;

import fr.fistin.hydra.api.HydraAPI;
import fr.fistin.hydra.api.protocol.packet.HydraPacket;
import fr.fistin.hydra.api.protocol.packet.HydraPacketRequest;
import fr.fistin.hydra.api.protocol.packet.model.HydraResponsePacket;
import fr.fistin.hydra.api.protocol.receiver.IHydraPacketReceiver;

import java.util.concurrent.TimeUnit;

/**
 * Project: Hydra
 * Created by AstFaster
 * on 26/12/2021 at 21:41
 */
public class HydraResponseReceiver implements IHydraPacketReceiver {

    /** Responses received */
    private int responses = 0;

    /** {@link HydraAPI} instance */
    private final HydraAPI hydraAPI;
    /** Initial request */
    private final HydraPacketRequest request;

    /**
     * Constructor of {@link HydraResponseReceiver}
     *
     * @param hydraAPI {@link HydraAPI} instance
     * @param request The initial request
     */
    public HydraResponseReceiver(HydraAPI hydraAPI, HydraPacketRequest request) {
        this.hydraAPI = hydraAPI;
        this.request = request;

        this.hydraAPI.getExecutorService().schedule(() -> this.unregister(this.request.getChannel()), this.request.getResponsesTime(), TimeUnit.MILLISECONDS);
    }

    @Override
    public HydraResponse receive(String channel, HydraPacket packet) {
        if (packet instanceof HydraResponsePacket) {
            final HydraResponsePacket responsePacket = (HydraResponsePacket) packet;

            if (responsePacket.getRespondedPacketUniqueId().equals(this.request.getPacket().getUniqueId())) {
                final HydraResponseCallback responseCallback = this.request.getResponseCallback();

                this.responses++;

                if (this.responses >= this.request.getMaxResponses()) {
                    this.unregister(this.request.getChannel());
                }

                if (responseCallback != null) {
                    responseCallback.call(responsePacket.getResponse(), responsePacket.getMessage());
                }
            }
        }
        return HydraResponseType.NONE.toResponse();
    }

    /**
     * Unregister the receiver
     *
     * @param channel Receiver channel
     */
    private void unregister(String channel) {
        this.hydraAPI.getConnection().unregisterReceiver(channel, this);
    }

}
