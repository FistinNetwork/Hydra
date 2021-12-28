package fr.fistin.hydra.api.protocol.response;

/**
 * Project: Hydra
 * Created by AstFaster
 * on 24/11/2021 at 13:25
 */
public interface HydraResponseCallback {

    /**
     * Fired when a response is received for a packet sent
     *
     * @param response The received response
     */
    void call(HydraResponse response);

}
