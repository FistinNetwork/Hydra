package fr.fistin.hydra.api.server;

import fr.fistin.hydra.api.protocol.data.HydraData;

/**
 * Created by AstFaster
 * on 02/11/2022 at 09:31
 *
 * Represents the request to send to Hydra to create a server.
 */
public class HydraServerCreationInfo {

    /** The type of server to create */
    private String type = null;
    /** The type of game to create */
    private String gameType = null;
    /** The map of the server to create */
    private String map = null;
    /** The accessibility of the server to create */
    private HydraServer.Accessibility accessibility = HydraServer.Accessibility.PUBLIC;
    /** The process of the server to create */
    private HydraServer.Process process = HydraServer.Process.TEMPORARY;
    /** The option of the server to create */
    private HydraServerOptions options = new HydraServerOptions();
    /** The data of the server to create */
    private HydraData data = new HydraData();
    /** The slots of the server to create */
    private int slots = -1;

    /**
     * Get the type of the server to create
     *
     * @return A type
     */
    public String getType() {
        return this.type;
    }

    /**
     * Set the type of server to create
     *
     * @param type The new type of the server
     * @return This {@link HydraServerCreationInfo} instance
     */
    public HydraServerCreationInfo withType(String type) {
        this.type = type;
        return this;
    }

    /**
     * Get the type of the game to create
     *
     * @return A game type
     */
    public String getGameType() {
        return this.gameType;
    }

    /**
     * Set the type of the game to create
     *
     * @param gameType The new game type
     * @return This {@link HydraServerCreationInfo} instance
     */
    public HydraServerCreationInfo withGameType(String gameType) {
        this.gameType = gameType;
        return this;
    }

    /**
     * Get the map of the server to create
     *
     * @return A map name
     */
    public String getMap() {
        return this.map;
    }

    /**
     * Set the map of the server to create
     *
     * @param map The new map
     * @return This {@link HydraServerCreationInfo} instance
     */
    public HydraServerCreationInfo withMap(String map) {
        this.map = map;
        return this;
    }

    /**
     * Get the accessibility of the server to create
     *
     * @return The {@link fr.fistin.hydra.api.server.HydraServer.Accessibility} of the server
     */
    public HydraServer.Accessibility getAccessibility() {
        return this.accessibility;
    }

    /**
     * Set the accessibility of the server to create
     *
     * @param accessibility The new {@link fr.fistin.hydra.api.server.HydraServer.Accessibility} of the server
     * @return This {@link HydraServerCreationInfo} instance
     */
    public HydraServerCreationInfo withAccessibility(HydraServer.Accessibility accessibility) {
        this.accessibility = accessibility;
        return this;
    }

    /**
     * Get the process of the server to create
     *
     * @return The {@link fr.fistin.hydra.api.server.HydraServer.Process} of the server
     */
    public HydraServer.Process getProcess() {
        return this.process;
    }

    /**
     * Set the process of the server to create
     *
     * @param process The new {@link fr.fistin.hydra.api.server.HydraServer.Process} of the server
     * @return This {@link HydraServerCreationInfo} instance
     */
    public HydraServerCreationInfo withProcess(HydraServer.Process process) {
        this.process = process;
        return this;
    }

    /**
     * Get the options of the server to create
     *
     * @return The {@linkplain HydraServerOptions server's options}
     */
    public HydraServerOptions getOptions() {
        return this.options;
    }

    /**
     * Set the options of the server to create
     *
     * @param options The new {@link HydraServerOptions} of the server
     * @return This {@link HydraServerCreationInfo} instance
     */
    public HydraServerCreationInfo withOptions(HydraServerOptions options) {
        this.options = options;
        return this;
    }

    /**
     * Get the data of the server to create
     *
     * @return The {@linkplain HydraData server's data}
     */
    public HydraData getData() {
        return this.data;
    }

    /**
     * Set the data of the server to create
     *
     * @param data The new {@linkplain HydraData server's data}
     * @return This {@link HydraServerCreationInfo} instance
     */
    public HydraServerCreationInfo withData(HydraData data) {
        this.data = data;
        return this;
    }

    /**
     * Get the slots of the server to create
     *
     * @return The server's slots
     */
    public int getSlots() {
        return this.slots;
    }

    /**
     * Set the slots of the server to create
     *
     * @param slots The new slots
     * @return This {@link HydraServerCreationInfo} instance
     */
    public HydraServerCreationInfo withSlots(int slots) {
        this.slots = slots;
        return this;
    }

}
