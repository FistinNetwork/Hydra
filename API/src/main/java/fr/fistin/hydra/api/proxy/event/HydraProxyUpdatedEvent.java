package fr.fistin.hydra.api.proxy.event;

import fr.fistin.hydra.api.event.HydraEvent;
import fr.fistin.hydra.api.proxy.HydraProxy;

/**
 * Created by AstFaster
 * on 02/11/2022 at 10:11
 *
 * Event fired each time a proxy is updated.
 */
public class HydraProxyUpdatedEvent extends HydraEvent {

    /** The updated proxy */
    private final HydraProxy proxy;

    /**
     * Constructor of a {@link HydraProxyUpdatedEvent}
     *
     * @param proxy The updated proxy
     */
    public HydraProxyUpdatedEvent(HydraProxy proxy) {
        this.proxy = proxy;
    }

    /**
     * Get the updated proxy
     *
     * @return The updated {@link HydraProxy}
     */
    public HydraProxy getProxy() {
        return this.proxy;
    }

}
