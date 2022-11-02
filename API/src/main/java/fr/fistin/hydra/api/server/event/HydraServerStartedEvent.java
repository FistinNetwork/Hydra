package fr.fistin.hydra.api.server.event;

import fr.fistin.hydra.api.event.HydraEvent;
import fr.fistin.hydra.api.server.HydraServer;

/**
 * Created by AstFaster
 * on 02/11/2022 at 10:11
 *
 * Event fired each time a new server is started.
 */
public class HydraServerStartedEvent extends HydraEvent {

    /** The created server */
    private final HydraServer server;

    /**
     * Constructor of a {@link HydraServerStartedEvent}
     *
     * @param server The created server
     */
    public HydraServerStartedEvent(HydraServer server) {
        this.server = server;
    }

    /**
     * Get the created server
     *
     * @return The created {@link HydraServer}
     */
    public HydraServer getServer() {
        return this.server;
    }

}
