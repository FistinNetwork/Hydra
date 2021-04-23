package fr.fistin.hydra.packet.model;

import fr.fistin.hydra.packet.HydraPacket;

public class StartServerPacket extends HydraPacket {

    private final String name;

    public StartServerPacket(String id, String name) {
        super(id);
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}
