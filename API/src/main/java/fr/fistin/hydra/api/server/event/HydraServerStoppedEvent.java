package fr.fistin.hydra.api.server.event;

import fr.fistin.hydra.api.event.HydraEvent;
import fr.fistin.hydra.api.server.HydraServer;

/**
 * Created by AstFaster
 * on 02/11/2022 at 10:11
 *
 * Event fired each time a server is stopped.
 */
public class HydraServerStoppedEvent extends HydraEvent {

    /** The stopped server */
    private final HydraServer server;

    /**
     * Constructor of a {@link HydraServerStoppedEvent}
     *
     * @param server The stopped server
     */
    public HydraServerStoppedEvent(HydraServer server) {
        this.server = server;
    }

    /**
     * Get the stopped server
     *
     * @return The stopped {@link HydraServer}
     */
    public HydraServer getServer() {
        return this.server;
    }

}
