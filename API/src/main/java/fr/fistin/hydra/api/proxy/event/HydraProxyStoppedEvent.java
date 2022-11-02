package fr.fistin.hydra.api.proxy.event;

import fr.fistin.hydra.api.event.HydraEvent;
import fr.fistin.hydra.api.proxy.HydraProxy;

/**
 * Created by AstFaster
 * on 02/11/2022 at 10:11
 *
 * Event fired each time a proxy is stopped.
 */
public class HydraProxyStoppedEvent extends HydraEvent {

    /** The stopped proxy */
    private final HydraProxy proxy;

    /**
     * Constructor of a {@link HydraProxyStoppedEvent}
     *
     * @param proxy The stopped proxy
     */
    public HydraProxyStoppedEvent(HydraProxy proxy) {
        this.proxy = proxy;
    }

    /**
     * Get the stopped proxy
     *
     * @return The stopped {@link HydraProxy}
     */
    public HydraProxy getProxy() {
        return this.proxy;
    }

}
