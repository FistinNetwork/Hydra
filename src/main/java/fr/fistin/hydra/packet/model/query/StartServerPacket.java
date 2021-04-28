package fr.fistin.hydra.packet.model.query;

import fr.fistin.hydra.packet.HydraPacket;

public class StartServerPacket extends HydraPacket {

    private final String templateName;

    public StartServerPacket(String type, String templateName) {
        super(type);
        this.templateName = templateName;
    }

    public String getTemplateName() {
        return this.templateName;
    }
}
