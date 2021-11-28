package fr.fistin.hydra.api.proxy;

import java.util.UUID;

/**
 * Project: Hydra
 * Created by AstFaster
 * on 24/11/2021 at 18:24
 */
public class HydraProxy {

    /** Proxy id */
    protected final UUID uuid;

    /** Proxy started time */
    protected final long startedTime;

    /** Proxy port */
    protected int port;

    /** Proxy state */
    protected HydraProxyState state;

    /** Proxy players */
    protected int players;

    /**
     * Constructor of {@link HydraProxy}
     *
     * @param startedTime Proxy started time
     */
    public HydraProxy(long startedTime) {
        this.uuid = UUID.randomUUID();
        this.startedTime = startedTime;
        this.state = HydraProxyState.CREATING;
    }

    /**
     * Constructor of {@link HydraProxy}
     *
     */
    public HydraProxy() {
        this(System.currentTimeMillis());
    }

    /**
     * Get proxy's unique id
     *
     * @return {@link UUID}
     */
    public UUID getUniqueId() {
        return this.uuid;
    }

    /**
     * Get proxy's name
     *
     * @return Proxy's name
     */
    public String getName() {
        return this.toString();
    }

    /**
     * Get proxy's started time in millis
     *
     * @return Proxy's started time
     */
    public long getStartedTime() {
        return this.startedTime;
    }

    /**
     * Get proxy's open port
     *
     * @return Proxy's port
     */
    public int getPort() {
        return this.port;
    }

    /**
     * Set proxy's open port<br>
     * WARNING: If the proxy is already running, it will have no impact on the proxy
     *
     * @param port New proxy's port
     */
    public void setPort(int port) {
        this.port = port;
    }

    /**
     * Get proxy's state
     *
     * @return {@link HydraProxyState}
     */
    public HydraProxyState getState() {
        return this.state;
    }

    /**
     * Set proxy's state
     *
     * @param state New {@link HydraProxyState}
     */
    public void setState(HydraProxyState state) {
        this.state = state;
    }

    /**
     * Get players count on the proxy
     *
     * @return Players count
     */
    public int getPlayers() {
        return this.players;
    }

    /**
     * Set proxy's players
     *
     * @param players New value
     */
    public void setPlayers(int players) {
        this.players = players;
    }

    /**
     * Override {@link Object#toString()}
     *
     * @return Formatted proxy's name
     */
    @Override
    public String toString() {
        return "proxy-" + this.uuid.toString().split("-")[0];
    }

}
