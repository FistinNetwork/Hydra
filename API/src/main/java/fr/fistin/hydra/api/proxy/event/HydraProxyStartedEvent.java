package fr.fistin.hydra.api.proxy.event;

import fr.fistin.hydra.api.event.HydraEvent;
import fr.fistin.hydra.api.proxy.HydraProxy;

/**
 * Created by AstFaster
 * on 02/11/2022 at 10:11
 *
 * Event fired each time a new proxy is started.
 */
public class HydraProxyStartedEvent extends HydraEvent {

    /** The created proxy */
    private final HydraProxy proxy;

    /**
     * Constructor of a {@link HydraProxyStartedEvent}
     *
     * @param proxy The created proxy
     */
    public HydraProxyStartedEvent(HydraProxy proxy) {
        this.proxy = proxy;
    }

    /**
     * Get the created proxy
     *
     * @return The created {@link HydraProxy}
     */
    public HydraProxy getProxy() {
        return this.proxy;
    }

}
