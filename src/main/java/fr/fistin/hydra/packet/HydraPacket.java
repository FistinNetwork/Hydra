package fr.fistin.hydra.packet;

import fr.fistin.hydra.util.References;

public class HydraPacket {

    private String id;

    public HydraPacket(String id) {
        this.id = id;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String toJson() {
        return References.GSON.toJson(this);
    }

}
