package fr.fistin.hydra.api.server.event;

import fr.fistin.hydra.api.event.HydraEvent;
import fr.fistin.hydra.api.server.HydraServer;

/**
 * Created by AstFaster
 * on 02/11/2022 at 10:11
 *
 * Event fired each time a server is updated.
 */
public class HydraServerUpdatedEvent extends HydraEvent {

    /** The updated server */
    private final HydraServer server;

    /**
     * Constructor of a {@link HydraServerUpdatedEvent}
     *
     * @param server The updated server
     */
    public HydraServerUpdatedEvent(HydraServer server) {
        this.server = server;
    }

    /**
     * Get the updated server
     *
     * @return The updated {@link HydraServer}
     */
    public HydraServer getServer() {
        return this.server;
    }

}
