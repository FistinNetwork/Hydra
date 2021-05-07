package fr.fistin.hydraconnector.protocol.packet.event;

import fr.fistin.hydraconnector.protocol.packet.HydraPacket;

public class ServerStartedPacket extends HydraPacket {

    private final String serverId;
    private final String templateName;

    public ServerStartedPacket(String serverId, String templateName) {
        this.serverId = serverId;
        this.templateName = templateName;
    }

    public String getServerId() {
        return this.serverId;
    }

    public String getTemplateName() {
        return this.templateName;
    }
}
