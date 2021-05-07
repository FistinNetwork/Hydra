package fr.fistin.hydraconnector.protocol.packet.server;

import fr.fistin.hydraconnector.protocol.packet.HydraPacket;

public class StartServerPacket extends HydraPacket {

    private final String templateName;

    public StartServerPacket(String templateName) {
        this.templateName = templateName;
    }

    public String getTemplateName() {
        return this.templateName;
    }
}
