package fr.fistin.hydra.api.event.model;

import fr.fistin.hydra.api.event.HydraEvent;

/**
 * Project: Hydra
 * Created by AstFaster
 * on 24/11/2021 at 16:02
 */
public class HydraServerStopEvent extends HydraEvent {

    /** If of the server */
    private final String serverId;

    /**
     * Constructor of {@link HydraServerStopEvent}
     *
     * @param serverId Server's id
     */
    public HydraServerStopEvent(String serverId) {
        this.serverId = serverId;
    }

    /**
     * Get server's id
     *
     * @return An id
     */
    public String getServerId() {
        return this.serverId;
    }

}
