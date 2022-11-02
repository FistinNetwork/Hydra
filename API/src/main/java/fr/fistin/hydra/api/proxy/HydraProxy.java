package fr.fistin.hydra.api.proxy;

import fr.fistin.hydra.api.protocol.data.HydraData;
import fr.fistin.hydra.api.server.HydraServer;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Project: Hydra
 * Created by AstFaster
 * on 24/11/2021 at 18:24
 */
public class HydraProxy {

    /** The maximum amount of players on a proxy */
    public static final int SLOTS = 750;

    /** The name of the proxy */
    private final String name;

    /** The time when the proxy started (in milliseconds) */
    private final long startedTime;

    /** The current state of the proxy */
    private State state;

    /** The data of the proxy */
    private HydraData data;

    /** The players that are currently connected to the proxy */
    private final Set<UUID> players;

    /** The port of the proxy outside the cluster */
    private final int port;

    /** The last heartbeat of the proxy */
    private long lastHeartbeat;

    /**
     * Full constructor of a {@link HydraProxy}
     *
     * @param name The name of the proxy
     * @param startedTime The time when the proxy started (in milliseconds)
     * @param state The current state of the server
     * @param data The data of the proxy
     * @param players The player that are on the proxy
     * @param port The port of the proxy
     */
    public HydraProxy(String name, long startedTime, State state, HydraData data, Set<UUID> players, int port) {
        this.name = name;
        this.startedTime = startedTime;
        this.state = state;
        this.data = data;
        this.players = players;
        this.port = port;
    }

    /**
     * Simple constructor of a {@link HydraProxy}
     *
     * @param data The data of the proxy
     * @param port The port of the proxy
     */
    public HydraProxy(HydraData data, int port) {
        this("proxy-" + UUID.randomUUID().toString().split("-")[0].substring(0, 5), System.currentTimeMillis(), State.CREATING, data, new HashSet<>(), port);
    }

    /**
     * Get the name of the proxy.<br>
     * E.g. proxy-5vs4x
     *
     * @return A name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Get the time when the proxy started
     *
     * @return A time (in milliseconds)
     */
    public long getStartedTime() {
        return this.startedTime;
    }

    /**
     * Get the current state of the proxy
     *
     * @return A {@link State}
     */
    public State getState() {
        return this.state;
    }

    /**
     * Set the current state of the proxy
     *
     * @param state The new {@link State}
     */
    public void setState(State state) {
        this.state = state;
    }

    /**
     * Get the data of the proxy
     *
     * @return The {@link HydraData} object
     */
    public HydraData getData() {
        return this.data;
    }

    /**
     * Set the data of the proxy
     *
     * @param data The new {@link HydraData} object
     */
    public void setData(HydraData data) {
        this.data = data;
    }

    /**
     * Get the players that are currently connected to the proxy
     *
     * @return A set of player {@link UUID}
     */
    public Set<UUID> getPlayers() {
        return this.players;
    }

    /**
     * Add a player connected to the proxy
     *
     * @param player The {@link UUID} of the player to add
     */
    public void addPlayer(UUID player) {
        this.players.add(player);
    }

    /**
     * Remove a player connected to the proxy
     *
     * @param player The {@link UUID} of the player to remove
     */
    public void removePlayer(UUID player) {
        this.players.remove(player);
    }

    /**
     * Get the port of the proxy outside the cluster
     *
     * @return A port number
     */
    public int getPort() {
        return this.port;
    }

    /**
     * Get the last heartbeat of the proxy
     *
     * @return A heartbeat timestamp (milliseconds)
     */
    public long getLastHeartbeat() {
        return this.lastHeartbeat;
    }

    /**
     * The proxy just sent a heartbeat.
     *
     * @return <code>true</code> if it's the first heartbeat of the proxy
     */
    public boolean heartbeat() {
        final long oldHeartbeat = this.lastHeartbeat;

        if (oldHeartbeat == -1) {
            this.state = State.STARTING;
        }

        this.lastHeartbeat = System.currentTimeMillis();

        return oldHeartbeat == -1;
    }

    /**
     * Override {@link Object#toString()}
     *
     * @return The name of the proxy
     */
    @Override
    public String toString() {
        return this.name;
    }

    /** This enum represents the different states of a proxy */
    public enum State {

        /** Proxy is creating (when Docker/K8s just created it) */
        CREATING,
        /** Proxy is starting (when the onEnable method is fired in Core plugin) */
        STARTING,
        /** Proxy is ready to support players */
        READY,
        /** Proxy is stopping */
        SHUTDOWN,
        /** Proxy is idling (an error occurred or freezing) */
        IDLE

    }

}
