package fr.fistin.hydra.api.protocol;

import fr.fistin.hydra.api.HydraAPI;

/**
 * Project: Hydra
 * Created by AstFaster
 * on 24/11/2021 at 12:36
 */
public class HydraChannel {

    /** Channel used to send a query to Hydra */
    public static final String QUERY = name("query");
    /** Channel used by servers to interact with Hydra */
    public static final String SERVERS = name("servers");
    /** Channel used by proxies to interact with Hydra */
    public static final String PROXIES = name("proxies");
    /** Channel used to send events */
    public static final String EVENTS = name("events");

    /**
     * Method used to format a given channel name
     *
     * @param name Channel's name to format
     * @return Formatted channel's name
     */
    private static String name(String name) {
        return HydraAPI.HYDRA_NAME + name.substring(0, 1).toUpperCase() + name.substring(1);
    }

}
