package fr.fistin.hydra.api.server;

import java.util.UUID;

/**
 * Project: Hydra
 * Created by AstFaster
 * on 24/11/2021 at 18:24
 */
public class HydraServer {

    /** Server id */
    protected final UUID uuid;

    /** Server type. Example: tnttag, lobby, etc */
    protected final String type;

    /** Server started time (in millis) */
    protected final long startedTime;

    /** Server state */
    protected HydraServerState state;

    /** Server options */
    protected HydraServerOptions options;

    /** Count of players on the server */
    protected int players;

    /**
     * Constructor of {@link HydraServer}
     *
     * @param type Server type
     * @param startedTime Server started time
     */
    public HydraServer(String type, long startedTime) {
        this.uuid = UUID.randomUUID();
        this.type = type;
        this.startedTime = startedTime;
        this.state = HydraServerState.CREATING;
        this.options = new HydraServerOptions();
    }

    /**
     * Constructor of {@link HydraServer}
     *
     * @param type Server type
     */
    public HydraServer(String type) {
        this(type, System.currentTimeMillis());
    }

    /**
     * Get server unique id
     *
     * @return {@link UUID}
     */
    public UUID getUniqueId() {
        return this.uuid;
    }

    /**
     * Get server type
     *
     * @return Server type
     */
    public String getType() {
        return this.type;
    }

    /**
     * Get server name. Example: lobby-vfd54
     *
     * @return Server name
     */
    public String getName() {
        return this.toString();
    }

    /**
     * Get server started time (in millis)
     *
     * @return Server started time
     */
    public long getStartedTime() {
        return this.startedTime;
    }

    /**
     * Get current server state
     *
     * @return {@link HydraServerState}
     */
    public HydraServerState getState() {
        return this.state;
    }

    /**
     * Set server state
     *
     * @param state New {@link HydraServerState}
     */
    public void setState(HydraServerState state) {
        this.state = state;
    }

    /**
     * Get server options
     *
     * @return {@link HydraServerOptions}
     */
    public HydraServerOptions getOptions() {
        return this.options;
    }

    /**
     * Set server options
     *
     * @param options New {@link HydraServerOptions}
     */
    public void setOptions(HydraServerOptions options) {
        this.options = options;
    }

    /**
     * Get count of players on the server
     *
     * @return Count of players
     */
    public int getPlayers() {
        return this.players;
    }

    /**
     * Set new count of players on the server
     *
     * @param players New count of players
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
        return this.type + "-" + this.uuid.toString().split("-")[0];
    }

}
