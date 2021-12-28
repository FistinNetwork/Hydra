package fr.fistin.hydra.api.protocol;

import fr.fistin.hydra.api.protocol.packet.HydraPacket;
import fr.fistin.hydra.api.protocol.packet.model.HydraHeartbeatPacket;
import fr.fistin.hydra.api.protocol.packet.model.HydraResponsePacket;
import fr.fistin.hydra.api.protocol.packet.model.proxy.HydraProxyServerActionPacket;
import fr.fistin.hydra.api.protocol.packet.model.proxy.HydraStartProxyPacket;
import fr.fistin.hydra.api.protocol.packet.model.proxy.HydraStopProxyPacket;
import fr.fistin.hydra.api.protocol.packet.model.server.HydraStartServerPacket;
import fr.fistin.hydra.api.protocol.packet.model.server.HydraStopServerPacket;

/**
 * Project: Hydra
 * Created by AstFaster
 * on 23/11/2021 at 07:15
 */
public enum HydraProtocol {

    /** Basics */
    RESPONSE(0, HydraResponsePacket.class),
    HEARTBEAT(1, HydraHeartbeatPacket.class),

    /** Server */
    START_SERVER(10, HydraStartServerPacket.class),
    STOP_SERVER(11, HydraStopServerPacket.class),

    /** Proxy */
    START_PROXY(30, HydraStartProxyPacket.class),
    STOP_PROXY(31, HydraStopProxyPacket.class),
    PROXY_SERVER_ACTION(32, HydraProxyServerActionPacket.class),

    ;

    /** Char used to split a message with its id */
    public static final String SPLIT_CHAR = "&";

    /** The id of the packet */
    private final int packetId;

    /** The class of the packet (the packet need to inherit of {@link HydraPacket}) */
    private final Class<? extends HydraPacket> packetClass;

    /**
     * Constructor of {@link HydraProtocol}
     *
     * @param packetId The id of the packet
     * @param packetClass The class of the packet
     */
    HydraProtocol(int packetId, Class<? extends HydraPacket> packetClass) {
        this.packetId = packetId;
        this.packetClass = packetClass;
    }

    /**
     * Get packet id
     *
     * @return An id
     */
    public int getPacketId() {
        return this.packetId;
    }

    /**
     * Get packet class
     *
     * @return {@link Class}
     */
    public Class<? extends HydraPacket> getPacketClass() {
        return this.packetClass;
    }

    /**
     * Get packet id by its class
     *
     * @param clazz Packet class
     * @return Packet id
     */
    public static int getPacketIdByClass(Class<? extends HydraPacket> clazz) {
        for (HydraProtocol protocol : values()) {
            if (protocol.getPacketClass() == clazz) {
                return protocol.getPacketId();
            }
        }
        return -1;
    }

    /**
     * Get packet class by its id
     *
     * @param id Packet id
     * @return Packet class
     */
    public static Class<? extends HydraPacket> getPacketClassById(int id) {
        for (HydraProtocol protocol : values()) {
            if (protocol.getPacketId() == id) {
                return protocol.getPacketClass();
            }
        }
        return null;
    }

}
