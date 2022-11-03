package fr.fistin.hydra.api.server;

import fr.fistin.hydra.api.protocol.data.HydraData;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Project: Hydra
 * Created by AstFaster
 * on 24/11/2021 at 18:24
 */
public class HydraServer {

    /** The name of the server. E.g. lobby-c7e6s */
    private final String name;
    /** Server type. Example: tnttag, lobby, etc */
    private final String type;

    /** The type of the game (optional). E.g. type=tnttag and gameType=normal */
    private String gameType;

    /** The map of the server */
    private String map;

    /** The accessibility level of the server */
    private Accessibility accessibility;
    /** The type of process the servers is running */
    private Process process;

    /** Server started time (in millis) */
    protected final long startedTime;

    /** Server state */
    private State state;

    /** Server options */
    private final HydraServerOptions options;
    /** Server's data */
    private HydraData data;

    /** The players that are on the server */
    private final Set<UUID> players;
    /** The maximum amount of players that can connect on the server */
    private int slots;

    /** The last heartbeat of the server */
    private long lastHeartbeat = -1;

    /**
     * Full constructor of a {@link HydraServer}
     *
     * @param name The name of the server
     * @param type The type of the server
     * @param gameType The type of the game
     * @param map The map used on the server
     * @param accessibility The accessibility of the server
     * @param process The process the server is running
     * @param startedTime The time when the server started
     * @param state The state of the server
     * @param options The options of the server
     * @param data The data of the server
     * @param players The players that are on the server
     * @param slots The maximum amount of players the server can handle
     */
    public HydraServer(String name, String type, String gameType, String map,
                       Accessibility accessibility, Process process, long startedTime, State state,
                       HydraServerOptions options, HydraData data, Set<UUID> players, int slots) {
        this.name = name;
        this.type = type;
        this.gameType = gameType;
        this.map = map;
        this.accessibility = accessibility;
        this.process = process;
        this.startedTime = startedTime;
        this.state = state;
        this.options = options;
        this.data = data;
        this.players = players;
        this.slots = slots;
    }

    /**
     * Simple constructor of a {@link HydraServer}
     *
     * @param type The type of the server
     * @param gameType The type of the game
     * @param map The map used on the server
     * @param accessibility The accessibility of the server
     * @param process The process the server is running
     * @param options The options of the server
     * @param data The data of the server
     * @param slots The maximum amount of players the server can handle
     */
    public HydraServer(String type, String gameType, String map, Accessibility accessibility, Process process, HydraServerOptions options, HydraData data, int slots) {
        this(type + "-" + UUID.randomUUID().toString().split("-")[0].substring(0, 5), type, gameType, map, accessibility, process, System.currentTimeMillis(), State.CREATING, options, data, new HashSet<>(), slots);
    }

    /**
     * Create a {@link HydraServer} from {@link HydraServerCreationInfo}
     *
     * @param serverInfo The information of the server to create
     */
    public HydraServer(HydraServerCreationInfo serverInfo) {
        this(serverInfo.getType(), serverInfo.getGameType(), serverInfo.getMap(), serverInfo.getAccessibility(), serverInfo.getProcess(), serverInfo.getOptions(), serverInfo.getData(), serverInfo.getSlots());
    }

    /**
     * Get server name. Example: lobby-vfd54
     *
     * @return Server name
     */
    public String getName() {
        return this.name;
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
     * Get the type of the game. E.g. normal, solo, duos, etc.<br>
     *
     * @return A game type; or <code>null</code> if no game is running on this server.
     */
    public String getGameType() {
        return this.gameType;
    }

    /**
     * Set the type of the game.
     *
     * @param gameType The new type of the game
     */
    public void setGameType(String gameType) {
        this.gameType = gameType;
    }

    /**
     * Get the map used by the server.
     *
     * @return A map name
     */
    public String getMap() {
        return this.map;
    }

    /**
     * Set the map used by the server.
     *
     * @param map The new map
     */
    public void setMap(String map) {
        this.map = map;
    }

    /**
     * Check whether the server is running a game or not.
     *
     * @return <code>true</code> if the server is running a game
     */
    public boolean isGame() {
        return this.gameType != null;
    }

    /**
     * Get the current accessibility of the server.
     *
     * @return An {@link Accessibility}
     */
    public Accessibility getAccessibility() {
        return this.accessibility;
    }

    /**
     * Set the new accessibility of the server
     *
     * @param accessibility The new {@link Accessibility}
     */
    public void setAccessibility(Accessibility accessibility) {
        this.accessibility = accessibility;
    }

    /**
     * Get the type of process the server is running.
     *
     * @return A {@link Process}
     */
    public Process getProcess() {
        return this.process;
    }

    /**
     * Set the type of process the server is running.
     *
     * @param process The new {@link Process}
     */
    public void setProcess(Process process) {
        this.process = process;
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
     * Get the current state of the server
     *
     * @return A {@link State}
     */
    public State getState() {
        return this.state;
    }

    /**
     * Set the current stat of the server
     *
     * @param state The new {@link State}
     */
    public void setState(State state) {
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
     * Get the data linked to the server.
     *
     * @return The {@link HydraData} object of the server
     */
    public HydraData getData() {
        return this.data;
    }

    /**
     * Set the data linked to the server
     *
     * @param data The new {@link HydraData} of the server
     */
    public void setData(HydraData data) {
        this.data = data;
    }

    /**
     * Get players on the server
     *
     * @return A set of player {@link UUID}
     */
    public Set<UUID> getPlayers() {
        return this.players;
    }

    /**
     * Add a player connected to the server.
     *
     * @param player The {@link UUID} of the player to add
     */
    public void addPlayer(UUID player) {
        this.players.add(player);
    }

    /**
     * Remove a player connected to the server
     *
     * @param player THe {@link UUID} of the player to remove
     */
    public void removePlayer(UUID player) {
        this.players.remove(player);
    }

    /**
     * Get the maximum amount of players the server can handle.
     *
     * @return A number representing the slots of the server
     */
    public int getSlots() {
        return this.slots;
    }

    /**
     * Set the maximum amount of players the server can handle.
     *
     * @param slots The new slots of the server
     */
    public void setSlots(int slots) {
        this.slots = slots;
    }

    /**
     * Get the last heartbeat of the server
     *
     * @return A heartbeat timestamp (in milliseconds)
     */
    public long getLastHeartbeat() {
        return this.lastHeartbeat;
    }

    /**
     * The server just sent a heartbeat.
     *
     * @return <code>true</code> if it's the first heartbeat of the server
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
     * @return The name of the server
     */
    @Override
    public String toString() {
        return this.name;
    }

    /** This enum represents the accessibility of the server through network. */
    public enum Accessibility {

        /** The server is public. Everyone have access to it. */
        PUBLIC,
        /** The server is a host. Only the owner and whitelisted players can join it (except if the whitelist is disabled) */
        HOST,
        /** The server is a beta server running new features that cannot be accessible to players. */
        BETA,
        /** The server is private. Only the owner and whitelisted players can join it. */
        PRIVATE

    }

    /** This enum represents the type of process a server is running. */
    public enum Process {

        /** The server is running a "permanent" process. The server doesn't stop by itself. E.g. a lobby or FFA server */
        PERMANENT,
        /** The server is running a "temporary" process. The server stops by itself. E.g. a basic game server */
        TEMPORARY

    }

    /** This enum represents the different states of a server */
    public enum State {

        /** Server is creating (when Docker/K8s just created it) */
        CREATING,
        /** Server is starting (when the onEnable method is fired in core plugin) */
        STARTING,
        /** Server is ready to support players */
        READY,
        /** Server is currently running a game */
        PLAYING,
        /** Server is stopping */
        SHUTDOWN,
        /** Server is idling (an error occurred or freezing) */
        IDLE

    }

}
