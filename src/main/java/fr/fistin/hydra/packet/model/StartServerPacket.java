package fr.fistin.hydra.packet.model;

import fr.fistin.hydra.packet.HydraPacket;

public class StartServerPacket extends HydraPacket {

    private final String type;

    public StartServerPacket(String id, String type) {
        super(id);
        this.type = type;
    }

    public String getType() {
        return this.type;
    }
}
