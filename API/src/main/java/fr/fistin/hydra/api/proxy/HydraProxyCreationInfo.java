package fr.fistin.hydra.api.proxy;

import fr.fistin.hydra.api.protocol.data.HydraData;

/**
 * Created by AstFaster
 * on 02/11/2022 at 09:31
 *
 * Represents the request to send to Hydra to create a proxy.
 */
public class HydraProxyCreationInfo {

    /** The data of the proxy to create */
    private HydraData data = new HydraData();

    /**
     * Get the data of the proxy to create
     *
     * @return The {@linkplain HydraData proxy's data}
     */
    public HydraData getData() {
        return this.data;
    }

    /**
     * Set the data of the proxy to create
     *
     * @param data The new {@linkplain HydraData proxy's data}
     * @return This {@link HydraProxyCreationInfo} instance
     */
    public HydraProxyCreationInfo withData(HydraData data) {
        this.data = data;
        return this;
    }

}
