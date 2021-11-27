package fr.fistin.hydra.api.event.model;

import fr.fistin.hydra.api.event.HydraEvent;

/**
 * Project: Hydra
 * Created by AstFaster
 * on 24/11/2021 at 16:02
 */
public class HydraProxyStopEvent extends HydraEvent {

    /** If of the proxy */
    private final String proxyId;

    /**
     * Constructor of {@link HydraProxyStopEvent}
     *
     * @param proxyId Proxy's id
     */
    public HydraProxyStopEvent(String proxyId) {
        this.proxyId = proxyId;
    }

    /**
     * Get proxy's id
     *
     * @return An id
     */
    public String getProxyId() {
        return this.proxyId;
    }

}