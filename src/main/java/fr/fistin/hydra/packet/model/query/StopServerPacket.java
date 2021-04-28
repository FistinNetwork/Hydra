package fr.fistin.hydra.packet.model.query;

import fr.fistin.hydra.packet.HydraPacket;

public class StopServerPacket extends HydraPacket {

    private final String serverName;

    public StopServerPacket(String type, String serverName) {
        super(type);
        this.serverName = serverName;
    }

    public String getServerName() {
        return this.serverName;
    }
}
