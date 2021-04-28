package fr.fistin.hydra.packet.model.event;

import fr.fistin.hydra.packet.HydraPacket;

public class ServerStartedPacket extends HydraPacket {

    private final String serverName;
    private final String templateName;

    public ServerStartedPacket(String type, String serverName, String templateName) {
        super(type);
        this.serverName = serverName;
        this.templateName = templateName;
    }

    public String getServerName() {
        return this.serverName;
    }

    public String getTemplateName() {
        return this.templateName;
    }
}
