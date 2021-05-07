package fr.fistin.hydraconnector.protocol.packet.common;

import fr.fistin.hydraconnector.protocol.packet.HydraPacket;

public class HeartbeatPacket extends HydraPacket {

    private final String id;

    public HeartbeatPacket(String id) {
        this.id = id;
    }

    public String getId() {
        return this.id;
    }
}
