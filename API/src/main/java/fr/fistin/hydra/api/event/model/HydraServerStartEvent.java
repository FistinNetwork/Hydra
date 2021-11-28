package fr.fistin.hydra.api.event.model;

import fr.fistin.hydra.api.event.HydraEvent;

/**
 * Project: Hydra
 * Created by AstFaster
 * on 24/11/2021 at 16:02
 */
public class HydraServerStartEvent extends HydraEvent {

    /** Name of the server */
    private final String serverName;

    /**
     * Constructor of {@link HydraServerStartEvent}
     *
     * @param serverName Server's name
     */
    public HydraServerStartEvent(String serverName) {
        this.serverName = serverName;
    }

    /**
     * Get server's name
     *
     * @return A name
     */
    public String getServerId() {
        return this.serverName;
    }

}
