package fr.fistin.hydraconnector.protocol.packet.server;

import fr.fistin.hydraconnector.protocol.packet.HydraPacket;

public class StopServerPacket extends HydraPacket {

    private final String serverId;

    public StopServerPacket(String serverId) {
        this.serverId = serverId;
    }

    public String getServerId() {
        return this.serverId;
    }
}
