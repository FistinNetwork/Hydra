package fr.fistin.hydraconnector.protocol;

import fr.fistin.hydraconnector.protocol.packet.HydraPacket;
import fr.fistin.hydraconnector.protocol.packet.common.HeartbeatPacket;
import fr.fistin.hydraconnector.protocol.packet.event.ServerStartedPacket;
import fr.fistin.hydraconnector.protocol.packet.event.ServerStoppedPacket;
import fr.fistin.hydraconnector.protocol.packet.server.StartServerPacket;
import fr.fistin.hydraconnector.protocol.packet.server.StopServerPacket;

import java.util.HashMap;
import java.util.Map;

public enum HydraProtocol {

    // Common
    HEARTH_BEAT_PACKET(1, HeartbeatPacket.class),

    // Event
    SERVER_STARTED_PACKET(10, ServerStartedPacket.class),
    SERVER_STOPPED_PACKET(11,ServerStoppedPacket .class),

    // Server
    START_SERVER_PACKET(40, StartServerPacket.class),
    STOP_SERVER_PACKET(41, StopServerPacket.class),

    // Proxy
    ;

    public static final String SPLIT_CHAR = "&";

    private final int packetId;
    private final Class<? extends HydraPacket> packetClass;

    HydraProtocol(int packetId, Class<? extends HydraPacket> packetClass) {
        this.packetId = packetId;
        this.packetClass = packetClass;
    }

    public int getPacketId() {
        return this.packetId;
    }

    public Class<? extends HydraPacket> getPacketClass() {
        return this.packetClass;
    }

    public static int getPacketIdByClass(Class<? extends HydraPacket> packetClass) {
        for (HydraProtocol protocol : values()) {
            if (protocol.getPacketClass() == packetClass) {
                return protocol.getPacketId();
            }
        }
        return -1;
    }

    public static Class<? extends HydraPacket> getPacketClassById(int id) {
        for (HydraProtocol protocol : values()) {
            if (protocol.getPacketId() == id) {
                return protocol.getPacketClass();
            }
        }
        return null;
    }

}
