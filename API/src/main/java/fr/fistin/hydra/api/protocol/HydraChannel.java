package fr.fistin.hydra.api.protocol;

import fr.fistin.hydra.api.HydraAPI;

/**
 * Project: Hydra
 * Created by AstFaster
 * on 24/11/2021 at 12:36
 */
public enum HydraChannel {

    /** Channel used to send a query to Hydra */
    QUERY("query"),
    /** Channel used by servers to interact with Hydra */
    SERVERS("servers"),
    /** Channel used by proxies to interact with Hydra */
    PROXIES("proxies"),
    /** Channel used to send events */
    EVENTS("events");

    private final String name;

    HydraChannel(String name) {
        this.name = HydraAPI.HYDRA_NAME + name.substring(0, 1).toUpperCase() + name.substring(1);
    }

    public String getName() {
        return this.name;
    }

}
