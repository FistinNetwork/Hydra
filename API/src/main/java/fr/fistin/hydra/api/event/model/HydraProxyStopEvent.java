package fr.fistin.hydra.api.event.model;

import fr.fistin.hydra.api.event.HydraEvent;

/**
 * Project: Hydra
 * Created by AstFaster
 * on 24/11/2021 at 16:02
 */
public class HydraProxyStopEvent extends HydraEvent {

    /** Name of the proxy */
    private final String proxyName;

    /**
     * Constructor of {@link HydraProxyStopEvent}
     *
     * @param proxyName Proxy's name
     */
    public HydraProxyStopEvent(String proxyName) {
        this.proxyName = proxyName;
    }

    /**
     * Get proxy's name
     *
     * @return A name
     */
    public String getProxyName() {
        return this.proxyName;
    }

}
