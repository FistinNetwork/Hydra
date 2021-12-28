package fr.fistin.hydra.api.protocol.packet.model.proxy;

import fr.fistin.hydra.api.protocol.packet.HydraPacket;

/**
 * Project: Hydra
 * Created by AstFaster
 * on 28/12/2021 at 10:15
 */
public class HydraProxyServerActionPacket extends HydraPacket {

    /** The action to do */
    private final Action action;
    /** The name of the server (ex: lobby-fsd874s */
    private final String serverName;
    /** The port of the server */
    private final int port;

    /**
     * Constructor of {@link HydraProxyServerActionPacket}
     *
     * @param action The action to do
     * @param serverName The name of the server
     * @param port The port of the server
     */
    public HydraProxyServerActionPacket(Action action, String serverName, int port) {
        this.action = action;
        this.serverName = serverName;
        this.port = port;
    }

    /**
     * Constructor of {@link HydraProxyServerActionPacket}
     *
     * @param action The action to do
     * @param serverName The name of the server
     */
    public HydraProxyServerActionPacket(Action action, String serverName) {
        this(action, serverName, 25565);
    }

    /**
     * Get the action to do
     *
     * @return An {@link Action}
     */
    public Action getAction() {
        return this.action;
    }

    /**
     * Get the name of the server
     *
     * @return A name (ex: tnttag-sdg4f9o)
     */
    public String getServerName() {
        return this.serverName;
    }

    /**
     * Get the port of the server
     *
     * @return A port
     */
    public int getPort() {
        return this.port;
    }

    /**
     * The possible actions to do
     */
    public enum Action {
        /** The action to add the server to the proxy */
        ADD,
        /** The action to remove the server from the proxy */
        REMOVE
    }

}
